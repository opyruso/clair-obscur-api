package com.opyruso.coh.repository;

import com.opyruso.coh.entity.PictoDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PictoDetailsRepository implements PanacheRepositoryBase<PictoDetails, PictoDetails.PK> {
}
