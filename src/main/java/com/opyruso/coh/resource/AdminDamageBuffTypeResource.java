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
        DamageBuffType damageBuffType = repository.findById(payload.idDamageBuffType);
        boolean isNew = false;
        if (damageBuffType == null) {
            damageBuffType = new DamageBuffType();
            damageBuffType.idDamageBuffType = payload.idDamageBuffType;
            repository.persist(damageBuffType);
            isNew = true;
        }

        if (damageBuffType.details == null) {
            damageBuffType.details = new java.util.ArrayList<>();
        }

        DamageBuffTypeDetails details = new DamageBuffTypeDetails();
        details.idDamageBuffType = payload.idDamageBuffType;
        details.lang = payload.lang;
        details.name = payload.name;
        details.damageBuffType = damageBuffType;

        damageBuffType.details.add(details);
        if (!isNew) {
            repository.getEntityManager().persist(details);
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(damageBuffType).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, DamageBuffTypeWithDetails payload) {
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

        if (payload.name != null) {
            details.name = payload.name;
        }

        repository.getEntityManager().flush();

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @QueryParam("lang") String lang) {
        DamageBuffType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                DamageBuffTypeDetails details = entity.details.stream()
                        .filter(d -> d.lang.equals(lang))
                        .findFirst().orElse(null);
                if (details != null) {
                    entity.details.remove(details);
                    repository.getEntityManager().remove(details);
                }
            }
            if (entity.details == null || entity.details.isEmpty()) {
                repository.delete(entity);
            }
        } else {
            repository.delete(entity);
        }
        return Response.noContent().build();
    }
}
