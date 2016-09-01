package lib.zkp4.identity.verify;

import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: hasini
 * Date: 12/9/14
 * Time: 1:14 PM
 */

/*This is the API to be used by SP for ZKP based identity verification.*/

public class VerifierAPI {
    /**
     * This is the API to be used by SP for ZKP based identity verification.
     * @param identityProofString
     * @param requestType
     * @param sessionID - can be null, this is only required in interactive zero knowledge proof in order to link the proof with the challenge
     * @return
     * @throws ZKP4IDException
     */
    public VerifierResponsePackage handleZeroKnowledgeProof(String identityProofString, String requestType, String sessionID) throws ZKP4IDException {
        //TODO: signature verification and expiration timestamp verification.
        try {
            //TODO: read from config which encoding decoding mech. is used. Assume JSON for now.
            IdentityProofEncoderDecoder identityProofEncoderDecoder = new JSONIdentityProofEncoderDecoder();
            MiscEncoderDecoder miscEncoderDecoder = new JSONMiscEncoderDecoder();

            IdentityProof identityProof = identityProofEncoderDecoder.decodeIdentityProof(identityProofString);
            IdentityVerificationHandler verificationHandler = new IdentityVerificationHandler();

            VerifierResponsePackage resp = new VerifierResponsePackage();
            //call identity verification handler which validates the token and verify the identity proof
            if (Constants.REQ_ZKP_I_INITIAL.equals(requestType)) {
                ProofInfo proofInfo = verificationHandler.handleInitialZKPIRequest(identityProof);
                String proofInfoString = (String) miscEncoderDecoder.encodeChallengeMessage(proofInfo.getSessionID(),
                        proofInfo.getChallengeValue().toString());
                resp.setProofInfo(proofInfo);
                resp.setResponseString(proofInfoString);
                return resp;
            } else if (Constants.REQ_ZKP_I_CHALLENGE_RESPONSE.equals(requestType)) {
                boolean verificationResult = verificationHandler.verifyZKPI(identityProof, sessionID);
                String result = (String) miscEncoderDecoder.encodeAuthResult(verificationResult);
                resp.setAuthResult(verificationResult);
                resp.setResponseString(result);
                return resp;

            } /*else if (Constants.REQ_ZKP_NI.equals(requestType)) {
                return verificationHandler.verifyZKPNI(identityProof);
            } else if (Constants.REQ_ZKP_NI_S.equals(requestType)) {
                return verificationHandler.verifyZKPNIS(identityProof);
            }*/
            throw new ZKP4IDException("Un-identified identity verification request.");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding the identity verification request.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding the identity verification request.");        }
    }

}
