package com.opyruso.coh.resource;

import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.repository.DamageBuffTypeRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/damageBuffTypes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminDamageBuffTypeResource {

    @Inject
    DamageBuffTypeRepository repository;

    @POST
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response create(DamageBuffType damageBuffType) {
        repository.persist(damageBuffType);
        return Response.status(Response.Status.CREATED).entity(damageBuffType).build();
    }

    @PUT
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response update(DamageBuffType damageBuffType) {
        DamageBuffType entity = repository.findById(damageBuffType.idDamageBuffType);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.details.clear();
        if (damageBuffType.details != null) {
            damageBuffType.details.forEach(d -> d.idDamageBuffType = damageBuffType.idDamageBuffType);
            entity.details.addAll(damageBuffType.details);
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("coh-app:admin")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
