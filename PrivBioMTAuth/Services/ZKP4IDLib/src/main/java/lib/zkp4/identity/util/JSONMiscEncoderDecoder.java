package lib.zkp4.identity.util;

import lib.zkp4.identity.verify.ProofInfo;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigInteger;

/**
 * Created by hasini on 8/30/16.
 */
public class JSONMiscEncoderDecoder implements MiscEncoderDecoder {
    @Override
    public Object encodeAuthResult(boolean authResult) throws Exception {
        JSONObject authResponse = new JSONObject();
        if (authResult) {
            authResponse.put(Constants.AUTH_RESULT_NAME, Constants.AUTH_SUCCESS);
        } else {
            authResponse.put(Constants.AUTH_RESULT_NAME, Constants.AUTH_FAILURE);
        }
        return authResponse.toString();
    }

    @Override
    public boolean decodeAuthResult(String authResultString) throws Exception {
        JSONObject authResultObject = new JSONObject(new JSONTokener(authResultString));
        return Boolean.valueOf(authResultObject.optString(Constants.AUTH_RESULT_NAME));
    }

    @Override
    public Object encodeChallengeMessage(String sessionID, String challengeString) throws Exception {
        JSONObject challengeContent = new JSONObject();
        challengeContent.put(Constants.SESSION_ID_NAME, sessionID);
        challengeContent.put(Constants.CHALLENGE_NAME, challengeString);
        return challengeContent.toString();
    }

    @Override
    public ProofInfo decodeChallengeMessage(String challengeMessage) throws Exception {
        JSONObject challengeObject = new JSONObject(new JSONTokener(challengeMessage));
        String challenge = challengeObject.optString(Constants.CHALLENGE_NAME);

        ProofInfo proofInfo = new ProofInfo();
        proofInfo.setChallengeValue(new BigInteger(challenge));

        String sessionId = challengeObject.optString(Constants.SESSION_ID_NAME);
        proofInfo.setSessionID(sessionId);

        return proofInfo;
    }
}
