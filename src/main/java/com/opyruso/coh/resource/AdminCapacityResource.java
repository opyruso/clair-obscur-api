package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.Capacity;
import com.opyruso.coh.entity.CapacityDetails;
import com.opyruso.coh.entity.CapacityType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.repository.*;
import com.opyruso.coh.model.dto.CapacityWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/capacities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminCapacityResource {

    @Inject
    CapacityRepository repository;
    @Inject
    CharacterRepository characterRepository;
    @Inject
    DamageTypeRepository damageTypeRepository;
    @Inject
    CapacityTypeRepository capacityTypeRepository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(CapacityWithDetails payload) {
        Character character = characterRepository.findById(payload.character);
        if (character == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid character").build();
        }
        DamageType damageType = payload.damageType == null ? null : damageTypeRepository.findById(payload.damageType);
        if (payload.damageType != null && damageType == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage type").build();
        }
        CapacityType type = payload.type == null ? null : capacityTypeRepository.findById(payload.type);
        if (payload.type != null && type == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid capacity type").build();
        }

        Capacity capacity = repository.findById(payload.idCapacity);
        if (capacity == null) {
            capacity = new Capacity();
            capacity.idCapacity = payload.idCapacity;
            capacity.character = character;
            capacity.energyCost = payload.energyCost;
            capacity.canBreak = payload.canBreak;
            capacity.damageType = damageType;
            capacity.type = type;
            capacity.isMultiTarget = payload.isMultiTarget;
            capacity.gridPositionX = payload.gridPositionX;
            capacity.gridPositionY = payload.gridPositionY;
            capacity.details = new java.util.ArrayList<>();
            repository.persist(capacity);
        }
        if (capacity.details == null) {
            capacity.details = new java.util.ArrayList<>();
        }
        boolean exists = capacity.details.stream()
                .anyMatch(d -> d.lang.equals(payload.lang));
        if (exists) {
            return Response.status(Response.Status.CONFLICT).entity("Details already exist").build();
        }

        CapacityDetails details = new CapacityDetails();
        details.idCapacity = payload.idCapacity;
        details.lang = payload.lang;
        details.name = payload.name;
        details.effectPrimary = payload.effectPrimary;
        details.effectSecondary = payload.effectSecondary;
        details.bonusDescription = payload.bonusDescription;
        details.additionnalDescription = payload.additionnalDescription;
        details.capacity = capacity;

        capacity.details.add(details);
        repository.getEntityManager().persist(details);
        repository.getEntityManager().flush();

        return Response.status(Response.Status.CREATED).entity(capacity).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, CapacityWithDetails payload) {
        Capacity entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Character character = null;
        if (payload.character != null) {
            character = characterRepository.findById(payload.character);
            if (character == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid character").build();
            }
            entity.character = character;
        }
        if (payload.energyCost != null) {
            entity.energyCost = payload.energyCost;
        }
        if (payload.canBreak != null) {
            entity.canBreak = payload.canBreak;
        }
        if (payload.damageType != null) {
            DamageType damageType = damageTypeRepository.findById(payload.damageType);
            if (damageType == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage type").build();
            }
            entity.damageType = damageType;
        }
        if (payload.type != null) {
            CapacityType type = capacityTypeRepository.findById(payload.type);
            if (type == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid capacity type").build();
            }
            entity.type = type;
        }
        if (payload.isMultiTarget != null) {
            entity.isMultiTarget = payload.isMultiTarget;
        }
        if (payload.gridPositionX != null) {
            entity.gridPositionX = payload.gridPositionX;
        }
        if (payload.gridPositionY != null) {
            entity.gridPositionY = payload.gridPositionY;
        }

        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        CapacityDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    CapacityDetails d = new CapacityDetails();
                    d.idCapacity = id;
                    d.lang = payload.lang;
                    d.capacity = entity;
                    entity.details.add(d);
                    return d;
                });

        if (payload.name != null) {
            details.name = payload.name;
        }
        if (payload.effectPrimary != null) {
            details.effectPrimary = payload.effectPrimary;
        }
        if (payload.effectSecondary != null) {
            details.effectSecondary = payload.effectSecondary;
        }
        if (payload.bonusDescription != null) {
            details.bonusDescription = payload.bonusDescription;
        }
        if (payload.additionnalDescription != null) {
            details.additionnalDescription = payload.additionnalDescription;
        }

        repository.getEntityManager().flush();

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}/{lang}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @PathParam("lang") String lang) {
        Capacity entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CapacityDetails detail = entity.details.stream()
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
