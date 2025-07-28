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
    public String idBuild;

    @Lob
    @Column(name = "content")
    public String content;

    @Column(name = "author")
    public String author;

    @Column(name = "firstname")
    public String firstname;

    @Column(name = "title")
    public String title;

    @Column(name = "recommended_level")
    public Integer recommendedLevel;

    @Column(name = "description")
    public String description;
}
