package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.acme.GreetingService;
import org.acme.UserRequest;
import jakarta.inject.Inject;

@Path("/hello")
public class GreetingResource {

    private static final Logger log = Logger.getLogger(GreetingResource.class);
    @Inject
    GreetingService greetingService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
log.info("Received a request to /hello endpoint");
        simulateLatency();
        return "Hello from RESTEasy Reactive";
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(UserRequest userRequest, @HeaderParam("Authorization") String authHeader) {
        if (!greetingService.isAuthorized(authHeader)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Missing or invalid Authorization header")
                    .build();
        }
        log.infof("Received POST /hello/register with name=%s, age=%d, auth=%s", userRequest.name, userRequest.age, authHeader);
        String greeting = greetingService.createGreeting(userRequest);
        return Response.ok(greeting).build();
    }

    private void simulateLatency() {
        log.info("Simulating latency for 5 seconds");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted", e);
        }
    }
}
