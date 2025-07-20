package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "coh_builds")
public class CohBuild extends PanacheEntityBase {

    @Id
    @Column(name = "id_build")
    public Long idBuild;

    @Lob
    @Column(name = "content")
    public String content;
}
