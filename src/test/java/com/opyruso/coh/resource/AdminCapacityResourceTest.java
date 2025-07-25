package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.Capacity;
import com.opyruso.coh.entity.CapacityDetails;
import com.opyruso.coh.entity.CapacityType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.model.dto.CapacityWithDetails;
import com.opyruso.coh.repository.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AdminCapacityResourceTest {

    @Inject
    CapacityRepository capacityRepository;
    @Inject
    CharacterRepository characterRepository;
    @Inject
    DamageTypeRepository damageTypeRepository;
    @Inject
    CapacityTypeRepository capacityTypeRepository;

    @BeforeEach
    void setUp() {
        capacityRepository.deleteAll();
        capacityTypeRepository.deleteAll();
        damageTypeRepository.deleteAll();
        characterRepository.deleteAll();

        Character c = new Character();
        c.idCharacter = "c1";
        characterRepository.persist(c);

        DamageType dt = new DamageType();
        dt.idDamageType = "dt1";
        damageTypeRepository.persist(dt);

        CapacityType ct = new CapacityType();
        ct.idCapacityType = "ct1";
        capacityTypeRepository.persist(ct);

        Capacity capacity = new Capacity();
        capacity.idCapacity = "cap1";
        capacity.character = c;
        capacity.damageType = dt;
        capacity.type = ct;

        CapacityDetails cd = new CapacityDetails();
        cd.idCapacity = "cap1";
        cd.lang = "en";
        cd.name = "name";
        cd.capacity = capacity;

        capacity.details = new java.util.ArrayList<>(java.util.List.of(cd));
        capacityRepository.persist(capacity);
    }

    @Test
    @TestSecurity(roles = "admin")
    public void createInvalidCharacter() {
        CapacityWithDetails dto = new CapacityWithDetails();
        dto.idCapacity = "cap2";
        dto.character = "unknown";
        dto.damageType = "dt1";
        dto.type = "ct1";
        dto.lang = "en";
        dto.name = "name";

        given()
          .contentType("application/json")
          .body(dto)
          .post("/admin/capacities")
          .then()
          .statusCode(400)
          .body(is("Invalid character"));
    }

    @Test
    @TestSecurity(roles = "admin")
    public void updateInvalidDamageType() {
        CapacityWithDetails dto = new CapacityWithDetails();
        dto.character = "c1";
        dto.damageType = "bad";
        dto.type = "ct1";
        dto.lang = "en";
        dto.name = "name";

        given()
          .contentType("application/json")
          .body(dto)
          .put("/admin/capacities/cap1")
          .then()
          .statusCode(400)
          .body(is("Invalid damage type"));
    }
}
