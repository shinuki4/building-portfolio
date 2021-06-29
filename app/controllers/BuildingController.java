package controllers;

import action.BuildingAction;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.*;
import model.resource.BuildingResource;
import model.resource.BuildingResourceHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@With(BuildingAction.class)
public class BuildingController extends Controller {

    private final HttpExecutionContext ec;
    private final BuildingResourceHandler handler;
    private final WSClient ws;

    @Inject
    public BuildingController(HttpExecutionContext ec, BuildingResourceHandler handler, WSClient ws) {
        this.ec = ec;
        this.handler = handler;
        this.ws = ws;
    }

    public CompletionStage<Result> list(Http.Request request) {
        return handler.find(request).thenApplyAsync(buildings -> {
            final List<BuildingResource> buildingList = buildings.collect(Collectors.toList());
            return ok(Json.toJson(buildingList));
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
        BuildingResource[] resource;
        ArrayList<BuildingResource> buildingResources = new ArrayList<>();

        if(json.isArray()){
            resource = Json.fromJson(json, BuildingResource[].class);
        }else{
            resource = new BuildingResource[]{Json.fromJson(json, BuildingResource.class)};
        }
        Arrays.stream(resource).iterator().forEachRemaining(r ->
                {
                    CompletableFuture<BuildingResource> buildingResourceCompletableFuture = ws.url("https://api.geoapify.com/v1/geocode/search")
                            .addQueryParameter("name", r.getName())
                            .addQueryParameter("housenumber", String.valueOf(r.getNumber()))
                            .addQueryParameter("street", r.getStreetName())
                            .addQueryParameter("postcode", String.valueOf(r.getPostalCode()))
                            .addQueryParameter("city", r.getCity())
                            .addQueryParameter("country", r.getCountry())
                            .addQueryParameter("apiKey", "d1696f39cf824843ae2d77a894eca1f1")
                            .get()
                            .thenApply(wsResponse -> wsResponse.asJson().findPath("features").findPath("geometry").findPath("coordinates").toString())
                            .thenApply(coordinates -> new BuildingResource(r, coordinates))
                            .toCompletableFuture();

                    try {
                        buildingResources.add(buildingResourceCompletableFuture.get());
                    } catch (InterruptedException | ExecutionException e) {
                        buildingResourceCompletableFuture.cancel(true);
                    }

                }
        );


       return handler.createAll(request, buildingResources).thenApplyAsync(savedResource -> created(Json.toJson(savedResource)), ec.current());
    }

    public CompletionStage<Result> index(Http.Request request) {
        return handler.find(request).thenApplyAsync(buildings -> {
            final List<BuildingResource> buildingList = buildings.collect(Collectors.toList());
            return ok(views.html.index.render(buildingList));
        }, ec.current());
    }
}
