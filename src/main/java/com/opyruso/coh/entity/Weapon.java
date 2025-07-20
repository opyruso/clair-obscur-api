package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "weapons")
public class Weapon extends PanacheEntityBase {

    @Id
    @Column(name = "id_weapon")
    public String idWeapon;

    @ManyToOne
    @JoinColumn(name = "id_character")
    public Character character;

    @ManyToOne
    @JoinColumn(name = "id_damage_type")
    public DamageType damageType;

    @ManyToOne
    @JoinColumn(name = "id_damage_buff_type_1")
    public DamageBuffType damageBuffType1;

    @ManyToOne
    @JoinColumn(name = "id_damage_buff_type_2")
    public DamageBuffType damageBuffType2;

    @OneToMany(mappedBy = "weapon", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<WeaponDetails> details;
}
