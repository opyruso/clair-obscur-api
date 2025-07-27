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
    @Column(name = "id_character")
    public String idCharacter;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "region")
    public String region;

    @Column(name = "unlock_description")
    public String unlockDescription;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "id_weapon", referencedColumnName = "id_weapon", insertable = false, updatable = false),
        @JoinColumn(name = "id_character", referencedColumnName = "id_character", insertable = false, updatable = false)
    })
    @JsonIgnore
    public Weapon weapon;

    public static class PK implements Serializable {
        public String idWeapon;
        public String idCharacter;
        public String lang;

        @Override
        public int hashCode() {
            return (idWeapon + "#" + idCharacter + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idWeapon.equals(other.idWeapon) && idCharacter.equals(other.idCharacter) && lang.equals(other.lang);
        }
    }
}
