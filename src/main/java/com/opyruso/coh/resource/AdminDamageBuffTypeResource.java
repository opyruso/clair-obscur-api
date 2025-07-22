package com.opyruso.coh.resource;

import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageBuffTypeDetails;
import com.opyruso.coh.repository.DamageBuffTypeRepository;
import com.opyruso.coh.model.dto.DamageBuffTypeWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/damagebufftypes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminDamageBuffTypeResource {

    @Inject
    DamageBuffTypeRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(DamageBuffTypeWithDetails payload) {
        DamageBuffType damageBuffType = new DamageBuffType();
        damageBuffType.idDamageBuffType = payload.idDamageBuffType;

        DamageBuffTypeDetails details = new DamageBuffTypeDetails();
        details.idDamageBuffType = payload.idDamageBuffType;
        details.lang = payload.lang;
        details.name = payload.name;
        details.damageBuffType = damageBuffType;

        damageBuffType.details = new java.util.ArrayList<>(java.util.List.of(details));

        repository.persist(damageBuffType);
        return Response.status(Response.Status.CREATED).entity(damageBuffType).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") Integer id, DamageBuffTypeWithDetails payload) {
        DamageBuffType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        DamageBuffTypeDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    DamageBuffTypeDetails d = new DamageBuffTypeDetails();
                    d.idDamageBuffType = id;
                    d.lang = payload.lang;
                    d.damageBuffType = entity;
                    entity.details.add(d);
                    return d;
                });

        details.name = payload.name;

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
