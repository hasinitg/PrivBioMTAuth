package lib.zkp4.identity.util;

/**
 * Created by hasini on 8/30/16.
 */

import lib.zkp4.identity.verify.ProofInfo;

import java.math.BigInteger;

/**
 * This is the interface to be implemented for encoding and decoding of miscelaneous
 */
public interface MiscEncoderDecoder {

    public Object encodeAuthResult(boolean authResult) throws Exception;

    public boolean decodeAuthResult(String authResultString) throws Exception;

    public Object encodeChallengeMessage(String trackingID, String challengeString) throws Exception;

    public ProofInfo decodeChallengeMessage(String challengeMessage) throws Exception;
}
