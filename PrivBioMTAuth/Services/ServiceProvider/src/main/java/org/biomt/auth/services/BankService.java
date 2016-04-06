package org.biomt.auth.services;

import org.biomt.auth.communication.AuthResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by hasini on 3/16/16.
 */
@Path("/account")
public class BankService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setSessionId("rytuiop456789");
        return Response.status(200).entity(authResponse).build();
    }
}
