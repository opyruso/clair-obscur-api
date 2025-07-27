package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "outfit_details")
@IdClass(OutfitDetails.PK.class)
public class OutfitDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_outfit")
    public String idOutfit;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "description")
    public String description;

    @ManyToOne
    @JoinColumn(name = "id_outfit", insertable = false, updatable = false)
    @JsonIgnore
    public Outfit outfit;

    public static class PK implements Serializable {
        public String idOutfit;
        public String lang;

        @Override
        public int hashCode() {
            return (idOutfit + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idOutfit.equals(other.idOutfit) && lang.equals(other.lang);
        }
    }
}
