package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.model.dto.BuildPayload;
import com.opyruso.coh.repository.CohBuildRepository;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/builds")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class BuildResource {

    @Inject
    CohBuildRepository repository;

    @Inject
    SecurityIdentity identity;

    private String getUserId() {
        return identity.getPrincipal().getName();
    }

    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    @POST
    @Transactional
    public Response create(BuildPayload payload) {
        String id = generateKey();
        CohBuild build = new CohBuild();
        build.idBuild = id;
        build.author = getUserId();
        Object fn = identity.getAttribute("given_name");
        build.firstname = fn != null ? fn.toString() : null;
        build.title = payload.title;
        build.description = payload.description;
        build.recommendedLevel = payload.recommendedLevel;
        build.content = Base64.getEncoder().encodeToString(payload.content.getBytes(StandardCharsets.UTF_8));
        repository.persist(build);
        return Response.status(Response.Status.CREATED).entity(Map.of("id", id)).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") String id, BuildPayload payload) {
        CohBuild build = repository.findById(id);
        if (build == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!getUserId().equals(build.author)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (payload.title != null) build.title = payload.title;
        if (payload.description != null) build.description = payload.description;
        if (payload.recommendedLevel != null) build.recommendedLevel = payload.recommendedLevel;
        if (payload.content != null) build.content = Base64.getEncoder().encodeToString(payload.content.getBytes(StandardCharsets.UTF_8));
        repository.getEntityManager().flush();
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        CohBuild build = repository.findById(id);
        if (build == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!getUserId().equals(build.author)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        repository.delete(build);
        return Response.noContent().build();
    }

    @GET
    public Response listIds() {
        List<String> ids = repository.find("author", getUserId())
                .list()
                .stream()
                .map(b -> b.idBuild)
                .toList();
        return Response.ok(ids).build();
    }
}
