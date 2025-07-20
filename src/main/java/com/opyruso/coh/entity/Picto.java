package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "picto")
public class Picto extends PanacheEntityBase {

    @Id
    @Column(name = "id_picto")
    public String idPicto;

    @Column(name = "level")
    public int level;

    @Column(name = "bonus_defense")
    public int bonusDefense;

    @Column(name = "bonus_speed")
    public int bonusSpeed;

    @Column(name = "bonus_crit_chance")
    public int bonusCritChance;

    @Column(name = "bonus_health")
    public int bonusHealth;

    @Column(name = "lumina_cost")
    public int luminaCost;

    @OneToMany(mappedBy = "picto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PictoDetails> details;
}
