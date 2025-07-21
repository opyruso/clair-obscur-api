package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.model.PublicData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/public/data")
@Produces(MediaType.APPLICATION_JSON)
public class PublicDataResource {

    @GET
    @Path("{lang}")
    @Transactional
    public Response getAll(@PathParam("lang") String lang) {
        PublicData data = new PublicData();
        data.characters = Character
                .find(
                        "select distinct c from Character c " +
                                "left join fetch c.details d " +
                                "where d.lang = ?1 or d.lang is null",
                        lang)
                .list();
        data.damageTypes = DamageType
                .find(
                        "select distinct d from DamageType d " +
                                "left join fetch d.details dd " +
                                "where dd.lang = ?1 or dd.lang is null",
                        lang)
                .list();
        data.damageBuffTypes = DamageBuffType
                .find(
                        "select distinct d from DamageBuffType d " +
                                "left join fetch d.details dd " +
                                "where dd.lang = ?1 or dd.lang is null",
                        lang)
                .list();
        data.pictos = Picto
                .find(
                        "select distinct p from Picto p " +
                                "left join fetch p.details d " +
                                "where d.lang = ?1 or d.lang is null",
                        lang)
                .list();
        data.weapons = Weapon
                .find(
                        "select distinct w from Weapon w " +
                                "left join fetch w.details d " +
                                "where d.lang = ?1 or d.lang is null",
                        lang)
                .list();

        data.characters.forEach(c -> {
            if (c.details != null) {
                c.details.removeIf(d -> !lang.equals(d.lang));
            }
            if (c.details == null || c.details.isEmpty()) {
                var d = new com.opyruso.coh.entity.CharacterDetails();
                d.idCharacter = c.idCharacter;
                d.lang = lang;
                d.name = "";
                d.story = "";
                c.details = new java.util.ArrayList<>(java.util.List.of(d));
            }
        });

        data.damageTypes.forEach(dt -> {
            if (dt.details != null) {
                dt.details.removeIf(d -> !lang.equals(d.lang));
            }
            if (dt.details == null || dt.details.isEmpty()) {
                var dd = new com.opyruso.coh.entity.DamageTypeDetails();
                dd.idDamageType = dt.idDamageType;
                dd.lang = lang;
                dd.name = "";
                dt.details = new java.util.ArrayList<>(java.util.List.of(dd));
            }
        });

        data.damageBuffTypes.forEach(db -> {
            if (db.details != null) {
                db.details.removeIf(d -> !lang.equals(d.lang));
            }
            if (db.details == null || db.details.isEmpty()) {
                var dd = new com.opyruso.coh.entity.DamageBuffTypeDetails();
                dd.idDamageBuffType = db.idDamageBuffType;
                dd.lang = lang;
                dd.name = "";
                db.details = new java.util.ArrayList<>(java.util.List.of(dd));
            }
        });

        data.pictos.forEach(p -> {
            if (p.details != null) {
                p.details.removeIf(d -> !lang.equals(d.lang));
            }
            if (p.details == null || p.details.isEmpty()) {
                var pd = new com.opyruso.coh.entity.PictoDetails();
                pd.idPicto = p.idPicto;
                pd.lang = lang;
                pd.name = "";
                pd.region = "";
                pd.descrptionBonusLumina = "";
                pd.unlockDescription = "";
                p.details = new java.util.ArrayList<>(java.util.List.of(pd));
            }
        });

        data.weapons.forEach(w -> {
            if (w.details != null) {
                w.details.removeIf(d -> !lang.equals(d.lang));
            }
            if (w.details == null || w.details.isEmpty()) {
                var wd = new com.opyruso.coh.entity.WeaponDetails();
                wd.idWeapon = w.idWeapon;
                wd.lang = lang;
                wd.name = "";
                wd.region = "";
                wd.unlockDescription = "";
                wd.weaponEffect1 = "";
                wd.weaponEffect2 = "";
                wd.weaponEffect3 = "";
                w.details = new java.util.ArrayList<>(java.util.List.of(wd));
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.serverError().build();
        }
    }
}
