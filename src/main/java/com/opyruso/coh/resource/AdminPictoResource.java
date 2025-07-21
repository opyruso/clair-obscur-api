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
        Picto picto = new Picto();
        picto.idPicto = payload.idPicto;
        picto.level = payload.level;
        picto.bonusDefense = payload.bonusDefense;
        picto.bonusSpeed = payload.bonusSpeed;
        picto.bonusCritChance = payload.bonusCritChance;
        picto.bonusHealth = payload.bonusHealth;
        picto.luminaCost = payload.luminaCost;

        PictoDetails details = new PictoDetails();
        details.idPicto = payload.idPicto;
        details.lang = payload.lang;
        details.name = payload.name;
        details.region = payload.region;
        details.descrptionBonusLumina = payload.descrptionBonusLumina;
        details.unlockDescription = payload.unlockDescription;
        details.picto = picto;

        picto.details = new java.util.ArrayList<>(java.util.List.of(details));

        repository.persist(picto);
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
        entity.level = payload.level;
        entity.bonusDefense = payload.bonusDefense;
        entity.bonusSpeed = payload.bonusSpeed;
        entity.bonusCritChance = payload.bonusCritChance;
        entity.bonusHealth = payload.bonusHealth;
        entity.luminaCost = payload.luminaCost;

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

        details.name = payload.name;
        details.region = payload.region;
        details.descrptionBonusLumina = payload.descrptionBonusLumina;
        details.unlockDescription = payload.unlockDescription;

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
