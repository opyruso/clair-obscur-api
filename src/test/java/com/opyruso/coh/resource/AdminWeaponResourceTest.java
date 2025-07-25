package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.entity.WeaponDetails;
import com.opyruso.coh.model.dto.WeaponWithDetails;
import com.opyruso.coh.repository.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AdminWeaponResourceTest {

    @Inject
    WeaponRepository weaponRepository;
    @Inject
    CharacterRepository characterRepository;
    @Inject
    DamageTypeRepository damageTypeRepository;
    @Inject
    DamageBuffTypeRepository damageBuffTypeRepository;

    @BeforeEach
    void setUp() {
        weaponRepository.deleteAll();
        damageBuffTypeRepository.deleteAll();
        damageTypeRepository.deleteAll();
        characterRepository.deleteAll();

        Character c = new Character();
        c.idCharacter = "c1";
        characterRepository.persist(c);

        DamageType dt = new DamageType();
        dt.idDamageType = "dt1";
        damageTypeRepository.persist(dt);

        DamageBuffType db = new DamageBuffType();
        db.idDamageBuffType = "db1";
        damageBuffTypeRepository.persist(db);

        Weapon w = new Weapon();
        w.idWeapon = "w1";
        w.character = c;
        w.damageType = dt;
        w.damageBuffType1 = db;

        WeaponDetails wd = new WeaponDetails();
        wd.idWeapon = "w1";
        wd.lang = "en";
        wd.name = "name";
        wd.weapon = w;

        w.details = new java.util.ArrayList<>(java.util.List.of(wd));
        weaponRepository.persist(w);
    }

    @Test
    @TestSecurity(roles = "admin")
    public void createInvalidCharacter() {
        WeaponWithDetails dto = new WeaponWithDetails();
        dto.idWeapon = "w2";
        dto.character = "unknown";
        dto.damageType = "dt1";
        dto.lang = "en";
        dto.name = "bad";

        given()
          .contentType("application/json")
          .body(dto)
          .post("/admin/weapons")
          .then()
          .statusCode(400)
          .body(is("Invalid character"));
    }

    @Test
    @TestSecurity(roles = "admin")
    public void updateInvalidDamageType() {
        WeaponWithDetails dto = new WeaponWithDetails();
        dto.character = "c1";
        dto.damageType = "wrong";
        dto.lang = "en";
        dto.name = "name";

        given()
          .contentType("application/json")
          .body(dto)
          .put("/admin/weapons/w1")
          .then()
          .statusCode(400)
          .body(is("Invalid damage type"));
    }
}
