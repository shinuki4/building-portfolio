package v1.building;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPABuildingRepository implements BuildingRepository {

    private final JPAApi jpaApi;
    private final BuildingExecutionContext ec;
    private final CircuitBreaker<Optional<BuildingData>> circuitBreaker = new CircuitBreaker<Optional<BuildingData>>().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPABuildingRepository(JPAApi api, BuildingExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<BuildingData>> list() {
        return supplyAsync(() -> wrap(this::select), ec);
    }

    @Override
    public CompletionStage<BuildingData> create(BuildingData buildingData) {
        return supplyAsync(() -> wrap(em -> insert(em, buildingData)), ec);
    }

    @Override
    public CompletionStage<Optional<BuildingData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<BuildingData>> update(Long id, BuildingData buildingData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, buildingData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<BuildingData> lookup(EntityManager em, Long id) throws SQLException {
        throw new SQLException("Call this to cause the circuit breaker to trip");
        //return Optional.ofNullable(em.find(PostData.class, id));
    }

    private Stream<BuildingData> select(EntityManager em) {
        TypedQuery<BuildingData> query = em.createQuery("SELECT p FROM BuildingData p", BuildingData.class);
        return query.getResultList().stream();
    }

    private Optional<BuildingData> modify(EntityManager em, Long id, BuildingData buildingData) throws InterruptedException {
        final BuildingData data = em.find(BuildingData.class, id);
        if (data != null) {
            data.setName(buildingData.getName());
            data.setCity(buildingData.getCity());
            data.setCountry(buildingData.getCountry());
            data.setDescription(buildingData.getDescription());
            data.setPostalCode(buildingData.getPostalCode());
            data.setStreetName(buildingData.getStreetName());
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private BuildingData insert(EntityManager em, BuildingData buildingData) {
        return em.merge(buildingData);
    }
}
