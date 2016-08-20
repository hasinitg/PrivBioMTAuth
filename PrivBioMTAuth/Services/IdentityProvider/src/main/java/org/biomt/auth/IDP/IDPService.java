package org.biomt.auth.IDP;

import lib.zkp4.identity.commit.IDTRequest;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIDTRequestEncoderDecoder;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;
import org.crypto.lib.CryptoLibConstants;
import org.crypto.lib.util.CryptoUtil;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hasini on 4/6/16.
 */
@Path("/enroll")
public class IDPService {
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response enrollIdentity() {
        IDTManager manager = new IDTManager();
        try {
            IDTRequest idtRequest = new IDTRequest();
            idtRequest.setAttributeName("email");
            idtRequest.setFromField("hasini");
            idtRequest.setEncryptedSecret(CryptoUtil.getCommittableThruHash("htghh",
                    CryptoLibConstants.SECRET_BIT_LENGTH).toString());
            idtRequest.setToField("abcbank");

            IdentityToken identityToken = manager.createIDT(idtRequest, "HASINI");
            JSONObject idtJSON = new JSONIdentityTokenEncoderDecoder().encodeIdentityToken(identityToken);
            return Response.status(200).entity(idtJSON.toString()).build();

        } catch (BioMtAuthException e) {
            return Response.status(500).entity(e).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }*/


    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enrollIdentity(@HeaderParam(Constants.USER_NAME) String userName, String payload) {
        IDTManager manager = new IDTManager();
        try {
            /*IDTRequest idtRequest = new IDTRequest();
            idtRequest.setAttributeName("email");
            idtRequest.setFromField("hasini");
            idtRequest.setEncryptedSecret(CryptoUtil.getCommittableThruHash("htghh",
                    CryptoLibConstants.SECRET_BIT_LENGTH).toString());
            idtRequest.setToField("abcbank");*/

            //decode the payload into IDTRequest object
            IDTRequest idtRequest = new JSONIDTRequestEncoderDecoder().decodeIDTRequest(payload);
            //create IDT
            IdentityToken identityToken = manager.createIDT(idtRequest, userName);
            //encode IDT
            JSONObject idtJSON = new JSONIdentityTokenEncoderDecoder().encodeIdentityToken(identityToken);
            //return IDT
            return Response.status(200).entity(idtJSON.toString()).build();

        } catch (BioMtAuthException e) {
            return Response.status(500).entity(e).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }


}
