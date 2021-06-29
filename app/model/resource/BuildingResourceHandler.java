package model.resource;

import com.palominolabs.http.url.UrlBuilder;
import model.BuildingData;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import repository.building.BuildingRepository;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Post resources, which map to JSON.
 */
public class BuildingResourceHandler {

    private final BuildingRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public BuildingResourceHandler(BuildingRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<BuildingResource>> find(Http.Request request) {
        return repository.list().thenApplyAsync(buildingDataStream -> buildingDataStream.map(data -> new BuildingResource(data, link(request, data))), ec.current());
    }

    public CompletionStage<BuildingResource> create(Http.Request request, BuildingResource resource) {
        final BuildingData data = new BuildingData(resource.getName(), resource.getStreetName(), resource.getNumber(), resource.getPostalCode(), resource.getCity(), resource.getCountry(), resource.getDescription(), resource.getCoordinates());
        return repository.create(data).thenApplyAsync(savedData -> new BuildingResource(savedData, link(request, savedData)), ec.current());
    }

    public CompletionStage<Stream<BuildingResource>> createAll(Http.Request request, List<BuildingResource> resource) {
        for(BuildingResource r: resource){
            BuildingData buildingData = new BuildingData(r.getName(), r.getStreetName(), r.getNumber(), r.getPostalCode(), r.getCity(), r.getCountry(), r.getDescription(), r.getCoordinates());
            repository.create(buildingData).thenApplyAsync(savedData -> new BuildingResource(savedData, link(request, savedData)), ec.current());
        }
        return repository.list().thenApplyAsync(buildingDataStream -> buildingDataStream.map(data -> new BuildingResource(data, link(request, data))), ec.current());
    }

    public CompletionStage<Optional<BuildingResource>> lookup(Http.Request request, String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> optionalData.map(data -> new BuildingResource(data, link(request, data))), ec.current());
    }

    public CompletionStage<Optional<BuildingResource>> update(Http.Request request, String id, BuildingResource resource) {
        final BuildingData data = new BuildingData(resource.getName(), resource.getStreetName(), resource.getNumber(),resource.getPostalCode(),resource.getCity(),resource.getCountry(), resource.getDescription(), resource.getCoordinates());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> optionalData.map(op -> new BuildingResource(op, link(request, op))), ec.current());
    }

    private String link(Http.Request request, BuildingData data) {
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port)
                .pathSegments("v1", "buildings", data.getId().toString())
                .toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
