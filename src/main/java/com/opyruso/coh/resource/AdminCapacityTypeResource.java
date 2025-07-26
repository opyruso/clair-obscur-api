package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CapacityType;
import com.opyruso.coh.entity.CapacityTypeDetails;
import com.opyruso.coh.repository.CapacityTypeRepository;
import com.opyruso.coh.model.dto.CapacityTypeWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/capacitytypes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminCapacityTypeResource {

    @Inject
    CapacityTypeRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(CapacityTypeWithDetails payload) {
        CapacityType type = repository.findById(payload.idCapacityType);
        boolean isNew = false;
        if (type == null) {
            type = new CapacityType();
            type.idCapacityType = payload.idCapacityType;
            repository.persist(type);
            isNew = true;
        }

        if (type.details == null) {
            type.details = new java.util.ArrayList<>();
        }

        CapacityTypeDetails details = new CapacityTypeDetails();
        details.idCapacityType = payload.idCapacityType;
        details.lang = payload.lang;
        details.name = payload.name;
        details.capacityType = type;

        type.details.add(details);
        if (!isNew) {
            repository.getEntityManager().persist(details);
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(type).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, CapacityTypeWithDetails payload) {
        CapacityType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        CapacityTypeDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    CapacityTypeDetails d = new CapacityTypeDetails();
                    d.idCapacityType = id;
                    d.lang = payload.lang;
                    d.capacityType = entity;
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
        CapacityType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                CapacityTypeDetails details = entity.details.stream()
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
