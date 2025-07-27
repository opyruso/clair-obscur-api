package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "capacity")
public class Capacity extends PanacheEntityBase {

    @Id
    @Column(name = "id_capacity")
    public String idCapacity;

    @ManyToOne
    @JoinColumn(name = "id_character")
    public Character character;

    @Column(name = "energy_cost")
    public Integer energyCost;

    @Column(name = "can_break")
    public Integer canBreak;

    @ManyToOne
    @JoinColumn(name = "damage_type")
    public DamageType damageType;

    @ManyToOne
    @JoinColumn(name = "type")
    public CapacityType type;

    @Column(name = "is_multi_target")
    public Integer isMultiTarget;

    @Column(name = "grid_position_x")
    public Integer gridPositionX;

    @Column(name = "grid_position_y")
    public Integer gridPositionY;

    @Column(name = "name")
    public String name;

    @Column(name = "effect_primary")
    public String effectPrimary;

    @Column(name = "effect_secondary")
    public String effectSecondary;

    @OneToMany(mappedBy = "capacity", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<CapacityDetails> details;
}
