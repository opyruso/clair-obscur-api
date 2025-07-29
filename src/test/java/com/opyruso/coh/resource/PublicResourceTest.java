package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.repository.CohBuildRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PublicResourceTest {

    @Inject
    CohBuildRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        CohBuild b = new CohBuild();
        b.idBuild = "b1";
        b.content = ""; // minimal content
        repository.persist(b);
    }

    @Test
    public void latestReplacesNullWithBlank() {
        given()
                .get("/public/builds/latest")
                .then()
                .statusCode(200)
                .body("[0].title", is(""))
                .body("[0].description", is(""))
                .body("[0].recommendedLevel", is(""))
                .body("[0].author", is(""))
                .body("[0].firstname", is(""));
    }
}
