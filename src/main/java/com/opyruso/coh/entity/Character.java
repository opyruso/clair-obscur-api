package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "characters")
public class Character extends PanacheEntityBase {

    @Id
    @Column(name = "id_character")
    public String idCharacter;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CharacterDetails> details;
}
