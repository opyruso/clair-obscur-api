package com.opyruso.coh.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "weapons")
@IdClass(Weapon.PK.class)
public class Weapon extends AuditableEntity {

    @Id
    @Column(name = "id_weapon")
    public String idWeapon;

    @Id
    @Column(name = "id_character")
    public String idCharacter;

    @ManyToOne
    @JoinColumn(name = "id_character", insertable = false, updatable = false)
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

    @Column(name = "name")
    public String name;

    @Column(name = "weapon_effect_1")
    public String weaponEffect1;

    @Column(name = "weapon_effect_2")
    public String weaponEffect2;

    @Column(name = "weapon_effect_3")
    public String weaponEffect3;

    @OneToMany(mappedBy = "weapon", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<WeaponDetails> details;

    public static class PK implements java.io.Serializable {
        public String idWeapon;
        public String idCharacter;

        @Override
        public int hashCode() {
            return (idWeapon + "#" + idCharacter).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idWeapon.equals(other.idWeapon) && idCharacter.equals(other.idCharacter);
        }
    }
}
