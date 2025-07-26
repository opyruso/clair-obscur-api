package com.opyruso.coh.resource;

import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.DamageTypeDetails;
import com.opyruso.coh.repository.DamageTypeRepository;
import com.opyruso.coh.model.dto.DamageTypeWithDetails;
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
    public Response create(DamageTypeWithDetails payload) {
        DamageType damageType = repository.findById(payload.idDamageType);
        boolean isNew = false;
        if (damageType == null) {
            damageType = new DamageType();
            damageType.idDamageType = payload.idDamageType;
            repository.persist(damageType);
            isNew = true;
        }

        if (damageType.details == null) {
            damageType.details = new java.util.ArrayList<>();
        }

        DamageTypeDetails details = new DamageTypeDetails();
        details.idDamageType = payload.idDamageType;
        details.lang = payload.lang;
        details.name = payload.name;
        details.damageType = damageType;

        damageType.details.add(details);
        if (!isNew) {
            repository.getEntityManager().persist(details);
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(damageType).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, DamageTypeWithDetails payload) {
        DamageType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        DamageTypeDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    DamageTypeDetails d = new DamageTypeDetails();
                    d.idDamageType = id;
                    d.lang = payload.lang;
                    d.damageType = entity;
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
        DamageType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                DamageTypeDetails details = entity.details.stream()
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
