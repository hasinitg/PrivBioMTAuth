package org.biomt.auth.services;

import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.ZKP4IDException;
import lib.zkp4.identity.verify.VerifierAPI;
import lib.zkp4.identity.verify.VerifierResponsePackage;
import org.biomt.auth.session.SessionInfo;
import org.biomt.auth.session.SessionStoreManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

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

        System.out.println("Auth call received.");
        try {
            VerifierAPI verifier = new VerifierAPI();
            VerifierResponsePackage responsePackage = null;
            String responseString = null;
            SessionStoreManager sessionStoreManager = SessionStoreManager.getInstance();

            if (Constants.REQ_ZKP_I_INITIAL.equals(requestType)) {
                System.out.println("ZKP_I_initial...");
                responsePackage = verifier.handleZeroKnowledgeProof(payload, requestType, null);
                responseString = responsePackage.getResponseString();

                //extract the session id and maintain the session info mapping
                String trackingID = responsePackage.getProofInfo().getSessionID();
                SessionInfo sessionInfo = new SessionInfo();
                sessionInfo.setUserName(userName);
                sessionInfo.setSessionID(trackingID);
                sessionInfo.setAuthStatus(false);
                sessionStoreManager.addSessionInfo(userName, sessionInfo);
                return Response.status(Constants.HTTP_CODE_OK).entity(responseString).build();

            } else if (Constants.REQ_ZKP_I_CHALLENGE_RESPONSE.equals(requestType) && (sessionID != null)) {
                System.out.println("ZKP_I_Challenge_Response...");
                SessionInfo sessionInfo = sessionStoreManager.getSessionInfo(userName);
                if (sessionID.equals(sessionInfo.getSessionID())) {
                    responsePackage = verifier.handleZeroKnowledgeProof(payload, requestType, sessionID);
                    responseString = responsePackage.getResponseString();
                    if (responsePackage.getAuthResult()) {
                        System.out.println("auth success..");
                        String spGenSessionID = UUID.randomUUID().toString();
                        sessionInfo.setSpGivenSessionID(spGenSessionID);
                        sessionInfo.setAuthStatus(true);
                        //add sp given session id to response string (this could have been done in the previous if statement as well)
                        JSONObject response = new JSONObject(new JSONTokener(responseString));
                        response.put(Constants.SESSION_ID_NAME, spGenSessionID);
                        System.out.println("SP generated session id: " + spGenSessionID);
                        responseString = response.toString();
                    } else {
                        System.out.println("Auth failure...");
                    }
                }
                return Response.status(Constants.HTTP_CODE_OK).entity(responseString).build();
            } else {
                return  Response.status(Constants.HTTP_CODE_ERROR).entity("Wrong request type.").build();
            }
        } catch (ZKP4IDException e) {
            e.printStackTrace();
            return Response.status(Constants.HTTP_CODE_ERROR).entity(e.getMessage()).build();
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.status(Constants.HTTP_CODE_ERROR).entity(e.getMessage()).build();
        }
    }
}
