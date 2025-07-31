package com.opyruso.coh.resource;

import com.opyruso.coh.repository.SuggestionRepository;
import com.opyruso.coh.entity.Suggestion;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class SuggestionResourceTest {

    @Inject
    SuggestionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @TestSecurity(user = "u1")
    public void createSuggestion() {
        var payload = new java.util.HashMap<String, String>();
        payload.put("type", "Bug");
        payload.put("title", "Issue");
        payload.put("description", "desc");

        given()
          .contentType("application/json")
          .body(payload)
          .post("/suggestions")
          .then()
          .statusCode(201)
          .body("id", org.hamcrest.Matchers.notNullValue());

        org.junit.jupiter.api.Assertions.assertEquals(1, repository.count());
        Suggestion s = repository.findAll().firstResult();
        org.junit.jupiter.api.Assertions.assertEquals("u1", s.userId);
        org.junit.jupiter.api.Assertions.assertEquals("Bug", s.type);
        org.junit.jupiter.api.Assertions.assertEquals("Issue", s.title);
        org.junit.jupiter.api.Assertions.assertEquals("desc", s.description);
    }
}
