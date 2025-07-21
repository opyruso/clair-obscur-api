package com.opyruso.coh.resource;

import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.repository.DamageTypeRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/damagetypes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminDamageTypeResource {

    @Inject
    DamageTypeRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(DamageType damageType) {
        repository.persist(damageType);
        return Response.status(Response.Status.CREATED).entity(damageType).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Transactional
    public Response update(DamageType damageType) {
        DamageType entity = repository.findById(damageType.idDamageType);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.details.clear();
        if (damageType.details != null) {
            damageType.details.forEach(d -> d.idDamageType = damageType.idDamageType);
            entity.details.addAll(damageType.details);
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
