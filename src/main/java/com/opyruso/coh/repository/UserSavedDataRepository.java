package com.opyruso.coh.repository;

import com.opyruso.coh.entity.UserSavedData;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserSavedDataRepository implements PanacheRepositoryBase<UserSavedData, String> {
}
