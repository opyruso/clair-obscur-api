package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.CharacterDetails;
import com.opyruso.coh.model.dto.CharacterWithDetails;
import com.opyruso.coh.repository.CharacterRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AdminCharacterResourceTest {

    @Inject
    CharacterRepository characterRepository;

    @BeforeEach
    void setUp() {
        characterRepository.deleteAll();

        Character c = new Character();
        c.idCharacter = "c1";

        CharacterDetails cd = new CharacterDetails();
        cd.idCharacter = "c1";
        cd.lang = "en";
        cd.name = "name";
        cd.character = c;

        c.details = new java.util.ArrayList<>(java.util.List.of(cd));
        characterRepository.persist(c);
    }

    @Test
    @TestSecurity(roles = "admin")
    public void multipleLangUpdatesInsertDetailsOnly() {
        CharacterWithDetails dto = new CharacterWithDetails();
        dto.name = "Nom";
        dto.story = "Histoire";

        dto.lang = "fr";
        given()
                .contentType("application/json")
                .body(dto)
                .put("/admin/characters/c1")
                .then()
                .statusCode(200);

        Assertions.assertEquals(1, characterRepository.count());
        Assertions.assertEquals(2, CharacterDetails.count());

        dto.lang = "de";
        given()
                .contentType("application/json")
                .body(dto)
                .put("/admin/characters/c1")
                .then()
                .statusCode(200);

        Assertions.assertEquals(1, characterRepository.count());
        Assertions.assertEquals(3, CharacterDetails.count());
    }
}
