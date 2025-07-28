package com.opyruso.coh.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "capacity_details")
@IdClass(CapacityDetails.PK.class)
public class CapacityDetails extends AuditableEntity {

    @Id
    @Column(name = "id_capacity")
    public String idCapacity;

    @Id
    @Column(name = "lang")
    public String lang;


    @Column(name = "bonus_description")
    public String bonusDescription;

    @Column(name = "additionnal_description")
    public String additionnalDescription;

    @ManyToOne
    @JoinColumn(name = "id_capacity", insertable = false, updatable = false)
    @JsonIgnore
    public Capacity capacity;

    public static class PK implements Serializable {
        public String idCapacity;
        public String lang;

        @Override
        public int hashCode() {
            return (idCapacity + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idCapacity.equals(other.idCapacity) && lang.equals(other.lang);
        }
    }
}
