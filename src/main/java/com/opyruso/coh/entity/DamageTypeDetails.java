package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "damage_type_details")
@IdClass(DamageTypeDetails.PK.class)
public class DamageTypeDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_damage_type")
    public String idDamageType;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "name")
    public String name;

    @ManyToOne
    @JoinColumn(name = "id_damage_type", insertable = false, updatable = false)
    @JsonIgnore
    public DamageType damageType;

    public static class PK implements Serializable {
        public String idDamageType;
        public String lang;

        @Override
        public int hashCode() {
            return (idDamageType + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idDamageType.equals(other.idDamageType) && lang.equals(other.lang);
        }
    }
}
