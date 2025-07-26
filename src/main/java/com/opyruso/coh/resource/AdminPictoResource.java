package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.entity.PictoDetails;
import com.opyruso.coh.repository.PictoRepository;
import com.opyruso.coh.model.dto.PictoWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/pictos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminPictoResource {

    @Inject
    PictoRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(PictoWithDetails payload) {
        Picto picto = repository.findById(payload.idPicto);
        if (picto == null) {
            picto = new Picto();
            picto.idPicto = payload.idPicto;
            picto.level = payload.level;
            picto.bonusDefense = payload.bonusDefense;
            picto.bonusSpeed = payload.bonusSpeed;
            picto.bonusCritChance = payload.bonusCritChance;
            picto.bonusHealth = payload.bonusHealth;
            picto.luminaCost = payload.luminaCost;
            picto.details = new java.util.ArrayList<>();
            repository.persist(picto);
        }
        if (picto.details == null) {
            picto.details = new java.util.ArrayList<>();
        }
        boolean exists = picto.details.stream()
                .anyMatch(d -> d.lang.equals(payload.lang));
        if (exists) {
            return Response.status(Response.Status.CONFLICT).entity("Details already exist").build();
        }

        PictoDetails details = new PictoDetails();
        details.idPicto = payload.idPicto;
        details.lang = payload.lang;
        details.name = payload.name;
        details.region = payload.region;
        details.descrptionBonusLumina = payload.descrptionBonusLumina;
        details.unlockDescription = payload.unlockDescription;
        details.picto = picto;

        picto.details.add(details);
        repository.getEntityManager().persist(details);
        repository.getEntityManager().flush();

        return Response.status(Response.Status.CREATED).entity(picto).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, PictoWithDetails payload) {
        Picto entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (payload.level != null) {
            entity.level = payload.level;
        }
        if (payload.bonusDefense != null) {
            entity.bonusDefense = payload.bonusDefense;
        }
        if (payload.bonusSpeed != null) {
            entity.bonusSpeed = payload.bonusSpeed;
        }
        if (payload.bonusCritChance != null) {
            entity.bonusCritChance = payload.bonusCritChance;
        }
        if (payload.bonusHealth != null) {
            entity.bonusHealth = payload.bonusHealth;
        }
        if (payload.luminaCost != null) {
            entity.luminaCost = payload.luminaCost;
        }

        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        PictoDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    PictoDetails d = new PictoDetails();
                    d.idPicto = id;
                    d.lang = payload.lang;
                    d.picto = entity;
                    entity.details.add(d);
                    return d;
                });

        if (payload.name != null) {
            details.name = payload.name;
        }
        if (payload.region != null) {
            details.region = payload.region;
        }
        if (payload.descrptionBonusLumina != null) {
            details.descrptionBonusLumina = payload.descrptionBonusLumina;
        }
        if (payload.unlockDescription != null) {
            details.unlockDescription = payload.unlockDescription;
        }

        repository.getEntityManager().flush();

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}/{lang}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @PathParam("lang") String lang) {
        Picto entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PictoDetails detail = entity.details.stream()
                .filter(d -> d.lang.equals(lang))
                .findFirst()
                .orElse(null);
        if (detail == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.details.remove(detail);
        repository.getEntityManager().remove(detail);
        if (entity.details.isEmpty()) {
            repository.delete(entity);
        }
        repository.getEntityManager().flush();
        return Response.noContent().build();
    }
}
