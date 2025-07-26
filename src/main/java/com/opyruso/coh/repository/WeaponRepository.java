package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Weapon;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeaponRepository implements PanacheRepositoryBase<Weapon, Weapon.PK> {

    public Weapon findByIdWeapon(String idWeapon) {
        return find("idWeapon", idWeapon).firstResult();
    }

    public Weapon findByIdWeaponAndCharacter(String idWeapon, String idCharacter) {
        return find("idWeapon = ?1 and idCharacter = ?2", idWeapon, idCharacter).firstResult();
    }

    public boolean deleteByIdWeapon(String idWeapon) {
        Weapon weapon = findByIdWeapon(idWeapon);
        if (weapon == null) {
            return false;
        }
        delete(weapon);
        return true;
    }
}
