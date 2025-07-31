package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Suggestion;
import com.opyruso.coh.repository.SuggestionRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AdminSuggestionResourceTest {

    @Inject
    SuggestionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        Suggestion s = new Suggestion();
        s.idSuggestion = "s1";
        s.userId = "u1";
        s.type = "Bug";
        s.title = "t";
        s.description = "d";
        repository.persist(s);
    }

    @Test
    @TestSecurity(roles = "admin")
    public void listSuggestions() {
        given()
          .get("/admin/suggestions")
          .then()
          .statusCode(200)
          .body("size()", org.hamcrest.Matchers.equalTo(1));
    }

    @Test
    @TestSecurity(roles = "admin")
    public void deleteSuggestion() {
        given()
          .delete("/admin/suggestions/s1")
          .then()
          .statusCode(204);

        Assertions.assertEquals(0, repository.count());
    }
}
