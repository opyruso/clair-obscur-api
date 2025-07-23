package com.opyruso.coh.repository;

import com.opyruso.coh.entity.CohBuild;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CohBuildRepository implements PanacheRepositoryBase<CohBuild, String> {
}
