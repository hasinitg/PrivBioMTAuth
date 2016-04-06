package org.biomt.auth.IDP;

import lib.zkp4.identity.commit.IDTRequest;
import lib.zkp4.identity.commit.IdentityToken;
import org.crypto.lib.commitments.pedersen.PedersenCommitment;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by hasini on 4/6/16.
 */
@Path("/enroll")
public class IDPService {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response enrollIdentity(@HeaderParam("") String userName, IDTRequest idtRequest) {
        IDTManager manager = new IDTManager();
        try {
            IdentityToken identityToken = manager.createIDT(idtRequest, userName);
            return Response.status(200).entity(identityToken).build();
        } catch (BioMtAuthException e) {
            return Response.status(500).entity(e).build();
        }
    }


}
