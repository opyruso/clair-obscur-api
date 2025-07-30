package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.repository.CohBuildRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
}
