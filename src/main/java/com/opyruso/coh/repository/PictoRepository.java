package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Picto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PictoRepository implements PanacheRepository<Picto> {
}
