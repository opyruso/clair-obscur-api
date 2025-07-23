package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Capacity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CapacityRepository implements PanacheRepositoryBase<Capacity, Integer> {
}
