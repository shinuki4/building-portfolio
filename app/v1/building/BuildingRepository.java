package v1.building;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface BuildingRepository {

    CompletionStage<Stream<BuildingData>> list();

    CompletionStage<BuildingData> create(BuildingData buildingData);

    CompletionStage<Optional<BuildingData>> get(Long id);

    CompletionStage<Optional<BuildingData>> update(Long id, BuildingData buildingData);
}

