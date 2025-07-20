package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Picto;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PictoRepository implements PanacheRepositoryBase<Picto, String> {
}
