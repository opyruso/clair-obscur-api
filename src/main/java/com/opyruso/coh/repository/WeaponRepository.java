package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Weapon;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeaponRepository implements PanacheRepositoryBase<Weapon, Weapon.PK> {

    public Weapon findByIdWeapon(String idWeapon) {
        return find("idWeapon", idWeapon).firstResult();
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
