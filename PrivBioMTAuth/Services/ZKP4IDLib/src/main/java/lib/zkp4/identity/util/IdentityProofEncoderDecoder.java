package lib.zkp4.identity.util;

import lib.zkp4.identity.proof.IdentityProof;

/**
 * Created by hasini on 8/28/16.
 */
/*An implementation of this interface should construct an object of IdentityProof from
 *a string representation.*/
public interface IdentityProofEncoderDecoder {

    public Object encodeIdentityProof(IdentityProof identityProof) throws Exception;

    public IdentityProof decodeIdentityProof(String identityProofRepresentation) throws Exception;

}
