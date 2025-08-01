package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.repository.CohBuildRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BuildResourceTest {

    @Inject
    CohBuildRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        CohBuild b = new CohBuild();
        b.idBuild = "b1";
        b.author = "u1";
        b.firstname = "John";
        b.title = "My build";
        b.description = "desc";
        b.recommendedLevel = 5;
        b.content = java.util.Base64.getEncoder().encodeToString("data".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        repository.persist(b);
    }

    @Test
    @TestSecurity(user = "u1")
    public void listReturnsDetails() {
        given()
                .get("/builds")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].id", equalTo("b1"))
                .body("[0].title", equalTo("My build"))
                .body("[0].description", equalTo("desc"))
                .body("[0].recommendedLevel", equalTo(5))
                .body("[0].author", equalTo("u1"))
                .body("[0].firstname", equalTo("John"));
    }

    @Test
    @TestSecurity(user = "u1")
    public void postWithIdUpdatesExisting() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "b1");
        payload.put("title", "Updated");
        payload.put("description", "new desc");
        payload.put("recommendedLevel", 10);
        payload.put("content", "new data");

        given()
                .contentType("application/json")
                .body(payload)
                .post("/builds")
                .then()
                .statusCode(200)
                .body("id", equalTo("b1"));

        assertEquals(1L, repository.count());
        CohBuild updated = repository.findById("b1");
        assertEquals("Updated", updated.title);
        assertEquals("new desc", updated.description);
        assertEquals(10, updated.recommendedLevel);
    }

    @Test
    @TestSecurity(user = "u2")
    public void postWithForeignIdCreatesNewBuild() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "b1");
        payload.put("title", "Forked");
        payload.put("description", "desc2");
        payload.put("recommendedLevel", 7);
        payload.put("content", "data2");

        String newId = given()
                .contentType("application/json")
                .body(payload)
                .post("/builds")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");

        assertEquals(2L, repository.count());
        CohBuild created = repository.findById(newId);
        assertEquals("b1", created.buildOrigin);
        assertEquals("u2", created.author);
        assertEquals("Forked", created.title);
        assertEquals("desc2", created.description);
        assertEquals(7, created.recommendedLevel);
    }
}
