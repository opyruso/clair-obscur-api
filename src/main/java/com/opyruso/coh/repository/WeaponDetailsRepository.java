package com.opyruso.coh.repository;

import com.opyruso.coh.entity.WeaponDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeaponDetailsRepository implements PanacheRepositoryBase<WeaponDetails, WeaponDetails.PK> {
}
