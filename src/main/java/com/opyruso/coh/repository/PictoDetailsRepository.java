package com.opyruso.coh.repository;

import com.opyruso.coh.entity.PictoDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PictoDetailsRepository implements PanacheRepository<PictoDetails> {
}
