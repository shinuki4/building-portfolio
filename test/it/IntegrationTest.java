package it;

import com.fasterxml.jackson.databind.JsonNode;
import model.BuildingData;
import model.resource.BuildingResource;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import repository.building.BuildingRepository;


import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class IntegrationTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testList() {
        BuildingRepository repository = app.injector().instanceOf(BuildingRepository.class);
        repository.create(new BuildingData("White House", "Pennsylvania Ave NW", 1600, 20500, "DC", "USA", "this is the white house", "[ 50, 60]"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/v1/buildings");

        Result result = route(app, request);

        assertEquals(200, result.status());

        JsonNode listOfBuildings = contentAsJson(result);
        Optional<BuildingResource> post = findPostByTitle(listOfBuildings, "White House");
        assertTrue(post.isPresent());
    }

    private Optional<BuildingResource> findPostByTitle(JsonNode listOfBuildings, String buildingName) {
        Iterator<JsonNode> elements = listOfBuildings.elements();
        // spliterator dance to build a Stream from an Iterator
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                elements,
                Spliterator.ORDERED),
            false)
            .map(jsonNode -> Json.fromJson(jsonNode, BuildingResource.class))
            .filter(b -> b.getName().equals(buildingName))
            .findFirst();
    }

    private JsonNode contentAsJson(Result result) {
        final String responseBody = contentAsString(result);
        return Json.parse(responseBody);
    }

    @Test
    public void testListWithTrailingSlashIsUnknown() {
        BuildingRepository repository = app.injector().instanceOf(BuildingRepository.class);
        repository.create(new BuildingData("White House", "Pennsylvania Ave NW", 1600, 20500, "DC", "USA", "this is the white house", "[ 50, 60]"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/v1/buildings/");

        Result result = route(app, request);
        MatcherAssert.assertThat(result.status(), equalTo(NOT_FOUND));
    }

    @Test
    public void testUpdate() {
        BuildingRepository repository = app.injector().instanceOf(BuildingRepository.class);
        repository.create(new BuildingData("White House", "Pennsylvania Ave NW", 1600, 20500, "DC", "USA", "this is the white house", "[ 50, 60]"));

        JsonNode json = Json.toJson(new BuildingResource("21", "White House", "Pennsylvania Ave NW", 1600, 20500, "DC", "USA", "this is the white house", "[ 50, 60]"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(json)
                .uri("/v1/buildings/21");

        Result result = route(app, request);
        MatcherAssert.assertThat(result.status(), equalTo(OK));
    }
//
//    @Test
//    public void testCircuitBreakerOnShow() {
//        PostRepository repository = app.injector().instanceOf(PostRepository.class);
//        repository.create(new PostData("title-testCircuitBreakerOnShow", "body-testCircuitBreakerOnShow"));
//
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/v1/posts/1");
//
//        Result result = route(app, request);
//        org.hamcrest.MatcherAssert.assertThat(result.status(), equalTo(SERVICE_UNAVAILABLE));
//    }


}
