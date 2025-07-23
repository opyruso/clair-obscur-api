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

    @OneToMany(mappedBy = "damageType", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DamageTypeDetails> details;
}
