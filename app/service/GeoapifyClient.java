package service;

import model.resource.BuildingResource;
import model.resource.BuildingResourceHandler;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class GeoapifyClient {
    private final HttpExecutionContext ec;
    private final WSClient ws;

    @Inject
    public GeoapifyClient(HttpExecutionContext ec, WSClient ws) {
        this.ec = ec;
        this.ws = ws;
    }

    public CompletionStage<BuildingResource> searchBuildingCoordinateIntoGeopapify(BuildingResource resource){
        return ws.url("https://api.geoapify.com/v1/geocode/search")
                .addQueryParameter("name", resource.getName())
                .addQueryParameter("housenumber", String.valueOf(resource.getNumber()))
                .addQueryParameter("street", resource.getStreetName())
                .addQueryParameter("postcode", String.valueOf(resource.getPostalCode()))
                .addQueryParameter("city", resource.getCity())
                .addQueryParameter("country", resource.getCountry())
                .addQueryParameter("apiKey", "d1696f39cf824843ae2d77a894eca1f1")
                .get()
                .thenApplyAsync(wsResponse -> wsResponse.asJson().findPath("features").findPath("geometry").findPath("coordinates").toString(), ec.current())
                .thenApplyAsync(coordinates -> new BuildingResource(resource, coordinates), ec.current());
    }

}
