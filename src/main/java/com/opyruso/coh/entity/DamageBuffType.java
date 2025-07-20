package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "damage_buff_type")
public class DamageBuffType extends PanacheEntityBase {

    @Id
    @Column(name = "id_damage_buff_type")
    public Integer idDamageBuffType;

    @OneToMany(mappedBy = "damageBuffType", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DamageBuffTypeDetails> details;
}
