package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Weapon;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeaponRepository implements PanacheRepositoryBase<Weapon, String> {
}
