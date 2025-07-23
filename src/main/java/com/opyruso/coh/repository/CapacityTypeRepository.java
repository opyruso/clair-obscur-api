package com.opyruso.coh.repository;

import com.opyruso.coh.entity.CapacityType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CapacityTypeRepository implements PanacheRepositoryBase<CapacityType, String> {
}
