package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "damage_type")
public class DamageType extends PanacheEntityBase {

    @Id
    @Column(name = "id_damage_type")
    public String idDamageType;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "damageType", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<DamageTypeDetails> details;
}
