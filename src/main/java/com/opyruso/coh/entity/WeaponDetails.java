package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "weapons_details")
@IdClass(WeaponDetails.PK.class)
public class WeaponDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_weapon")
    public String idWeapon;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "name")
    public String name;

    @Column(name = "region")
    public String region;

    @Column(name = "unlock_description")
    public String unlockDescription;

    @Column(name = "weapon_effect_1")
    public String weaponEffect1;

    @Column(name = "weapon_effect_2")
    public String weaponEffect2;

    @Column(name = "weapon_effect_3")
    public String weaponEffect3;

    @ManyToOne
    @JoinColumn(name = "id_weapon", referencedColumnName = "id_weapon", insertable = false, updatable = false)
    @JsonIgnore
    public Weapon weapon;

    public static class PK implements Serializable {
        public String idWeapon;
        public String lang;

        @Override
        public int hashCode() {
            return (idWeapon + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idWeapon.equals(other.idWeapon) && lang.equals(other.lang);
        }
    }
}
