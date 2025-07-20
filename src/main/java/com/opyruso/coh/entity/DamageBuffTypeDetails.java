package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "damage_buff_type_details")
@IdClass(DamageBuffTypeDetails.PK.class)
public class DamageBuffTypeDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_damage_buff_type")
    public Integer idDamageBuffType;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "name")
    public String name;

    @ManyToOne
    @JoinColumn(name = "id_damage_buff_type", insertable = false, updatable = false)
    @JsonIgnore
    public DamageBuffType damageBuffType;

    public static class PK implements Serializable {
        public Integer idDamageBuffType;
        public String lang;

        @Override
        public int hashCode() {
            return (idDamageBuffType + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idDamageBuffType.equals(other.idDamageBuffType) && lang.equals(other.lang);
        }
    }
}
