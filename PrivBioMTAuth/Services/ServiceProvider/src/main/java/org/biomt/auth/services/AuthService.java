package org.biomt.auth.services;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.ZKP4IDException;
import lib.zkp4.identity.verify.VerifierAPI;

/**
 * Created by hasini on 8/30/16.
 */
@Path("auth")
public class AuthService {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@HeaderParam(Constants.REQUEST_TYPE_NAME) String requestType,
                                 @HeaderParam(Constants.USER_NAME) String userName,
                                 @HeaderParam(Constants.SESSION_ID_NAME) String sessionID, String payload) {

        try {
            VerifierAPI verifier = new VerifierAPI();
            String responseString = verifier.handleZeroKnowledgeProof(payload, requestType, userName);
            return Response.status(Constants.HTTP_CODE_OK).entity(responseString).build();
        } catch (ZKP4IDException e) {
            e.printStackTrace();
            return Response.status(Constants.HTTP_CODE_ERROR).entity(e.getMessage()).build();
        }
    }

}
