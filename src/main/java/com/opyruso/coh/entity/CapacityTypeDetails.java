package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "capacity_type_details")
@IdClass(CapacityTypeDetails.PK.class)
public class CapacityTypeDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_capacity_type")
    public Integer idCapacityType;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "name")
    public String name;

    @ManyToOne
    @JoinColumn(name = "id_capacity_type", insertable = false, updatable = false)
    @JsonIgnore
    public CapacityType capacityType;

    public static class PK implements Serializable {
        public Integer idCapacityType;
        public String lang;

        @Override
        public int hashCode() {
            return (idCapacityType + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idCapacityType.equals(other.idCapacityType) && lang.equals(other.lang);
        }
    }
}
