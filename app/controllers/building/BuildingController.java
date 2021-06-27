package controllers.building;

import action.BuildingAction;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import model.resource.BuildingResource;
import model.resource.BuildingResourceHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(BuildingAction.class)
public class BuildingController extends Controller {

    private HttpExecutionContext ec;
    private BuildingResourceHandler handler;

    @Inject
    public BuildingController(HttpExecutionContext ec, BuildingResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> list(Http.Request request) {
        return handler.find(request).thenApplyAsync(buildings -> {
            final List<BuildingResource> postList = buildings.collect(Collectors.toList());
            return ok(Json.toJson(postList));
        }, ec.current());
    }

    public CompletionStage<Result> show(Http.Request request, String id) {
        return handler.lookup(request, id).thenApplyAsync(optionalResource -> optionalResource.map(resource ->
            ok(Json.toJson(resource))
        ).orElseGet(Results::notFound), ec.current());
    }

    public CompletionStage<Result> update(Http.Request request, String id) {
        JsonNode json = request.body().asJson();
        BuildingResource resource = Json.fromJson(json, BuildingResource.class);
        return handler.update(request, id, resource).thenApplyAsync(optionalResource -> optionalResource.map(r ->
                ok(Json.toJson(r))
        ).orElseGet(Results::notFound
        ), ec.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        final BuildingResource resource = Json.fromJson(json, BuildingResource.class);
        return handler.create(request, resource).thenApplyAsync(savedResource -> created(Json.toJson(savedResource)), ec.current());
    }
}
