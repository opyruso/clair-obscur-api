package com.opyruso.coh.resource;

import com.opyruso.coh.entity.UserSavedData;
import com.opyruso.coh.repository.UserSavedDataRepository;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user/saved-data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class UserSavedDataResource {

    @Inject
    UserSavedDataRepository repository;

    @Inject
    SecurityIdentity identity;

    private String getUserId() {
        return identity.getPrincipal().getName();
    }

    @GET
    public Response load() {
        UserSavedData data = repository.findById(getUserId());
        if (data == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(data.data).build();
    }

    @POST
    @Transactional
    public Response save(String body) {
        String userId = getUserId();
        UserSavedData data = repository.findById(userId);
        if (data == null) {
            data = new UserSavedData();
            data.userId = userId;
            data.data = body;
            repository.persist(data);
            return Response.status(Response.Status.CREATED).build();
        } else {
            data.data = body;
            repository.getEntityManager().flush();
            return Response.ok().build();
        }
    }
}
