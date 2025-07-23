package com.opyruso.coh.repository;

import com.opyruso.coh.entity.DamageType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DamageTypeRepository implements PanacheRepositoryBase<DamageType, String> {
}
