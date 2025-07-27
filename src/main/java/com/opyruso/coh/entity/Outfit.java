package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "outfit")
public class Outfit extends PanacheEntityBase {

    @Id
    @Column(name = "id_outfit")
    public String idOutfit;

    @ManyToOne
    @JoinColumn(name = "id_character")
    public Character character;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<OutfitDetails> details;
}
