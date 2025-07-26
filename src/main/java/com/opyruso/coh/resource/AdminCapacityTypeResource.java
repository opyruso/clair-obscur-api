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
        if (type == null) {
            type = new CapacityType();
            type.idCapacityType = payload.idCapacityType;
            type.details = new java.util.ArrayList<>();
            repository.persist(type);
        }
        if (type.details == null) {
            type.details = new java.util.ArrayList<>();
        }
        boolean exists = type.details.stream()
                .anyMatch(d -> d.lang.equals(payload.lang));
        if (exists) {
            return Response.status(Response.Status.CONFLICT).entity("Details already exist").build();
        }

        CapacityTypeDetails details = new CapacityTypeDetails();
        details.idCapacityType = payload.idCapacityType;
        details.lang = payload.lang;
        details.name = payload.name;
        details.capacityType = type;

        type.details.add(details);
        repository.getEntityManager().persist(details);
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
    @Path("{id}/{lang}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @PathParam("lang") String lang) {
        CapacityType entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CapacityTypeDetails detail = entity.details.stream()
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
