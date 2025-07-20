package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.repository.PictoRepository;
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
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response create(Picto picto) {
        repository.persist(picto);
        return Response.status(Response.Status.CREATED).entity(picto).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response update(@PathParam("id") String id, Picto picto) {
        Picto entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.level = picto.level;
        entity.bonusDefense = picto.bonusDefense;
        entity.bonusSpeed = picto.bonusSpeed;
        entity.bonusCritChance = picto.bonusCritChance;
        entity.bonusHealth = picto.bonusHealth;
        entity.luminaCost = picto.luminaCost;
        entity.details.clear();
        if (picto.details != null) {
            picto.details.forEach(d -> d.idPicto = id);
            entity.details.addAll(picto.details);
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
