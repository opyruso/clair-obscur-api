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
        data.characters = Character.find("select distinct c from Character c join fetch c.details d where d.lang = ?1", lang).list();
        data.damageTypes = DamageType.find("select distinct d from DamageType d join fetch d.details dd where dd.lang = ?1", lang).list();
        data.damageBuffTypes = DamageBuffType.find("select distinct d from DamageBuffType d join fetch d.details dd where dd.lang = ?1", lang).list();
        data.pictos = Picto.find("select distinct p from Picto p join fetch p.details d where d.lang = ?1", lang).list();
        data.weapons = Weapon.find("select distinct w from Weapon w join fetch w.details d where d.lang = ?1", lang).list();

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.serverError().build();
        }
    }
}
