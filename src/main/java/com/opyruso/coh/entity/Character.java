package com.opyruso.coh.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "characters")
public class Character extends AuditableEntity {

    @Id
    @Column(name = "id_character")
    public String idCharacter;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<CharacterDetails> details;
}
