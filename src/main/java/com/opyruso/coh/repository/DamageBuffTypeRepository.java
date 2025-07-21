package com.opyruso.coh.repository;

import com.opyruso.coh.entity.DamageBuffType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DamageBuffTypeRepository implements PanacheRepositoryBase<DamageBuffType, Integer> {
}
