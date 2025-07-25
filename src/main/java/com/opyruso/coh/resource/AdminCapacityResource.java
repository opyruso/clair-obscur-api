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

        Capacity capacity = new Capacity();
        capacity.idCapacity = payload.idCapacity;
        capacity.character = character;
        capacity.energyCost = payload.energyCost;
        capacity.canBreak = payload.canBreak;
        capacity.damageType = damageType;
        capacity.type = type;
        capacity.isMultiTarget = payload.isMultiTarget;
        capacity.gridPositionX = payload.gridPositionX;
        capacity.gridPositionY = payload.gridPositionY;

        CapacityDetails details = new CapacityDetails();
        details.idCapacity = payload.idCapacity;
        details.lang = payload.lang;
        details.name = payload.name;
        details.effectPrimary = payload.effectPrimary;
        details.effectSecondary = payload.effectSecondary;
        details.bonusDescription = payload.bonusDescription;
        details.additionnalDescription = payload.additionnalDescription;
        details.capacity = capacity;

        capacity.details = new java.util.ArrayList<>(java.util.List.of(details));

        repository.persist(capacity);
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

        entity.character = character;
        entity.energyCost = payload.energyCost;
        entity.canBreak = payload.canBreak;
        entity.damageType = damageType;
        entity.type = type;
        entity.isMultiTarget = payload.isMultiTarget;
        entity.gridPositionX = payload.gridPositionX;
        entity.gridPositionY = payload.gridPositionY;

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

        details.name = payload.name;
        details.effectPrimary = payload.effectPrimary;
        details.effectSecondary = payload.effectSecondary;
        details.bonusDescription = payload.bonusDescription;
        details.additionnalDescription = payload.additionnalDescription;

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
