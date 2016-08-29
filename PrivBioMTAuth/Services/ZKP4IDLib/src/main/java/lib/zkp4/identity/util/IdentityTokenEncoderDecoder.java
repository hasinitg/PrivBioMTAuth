package lib.zkp4.identity.util;

import lib.zkp4.identity.commit.IdentityToken;

/**
 * Created by hasini on 4/9/16.
 */

/*An implementation of this interface should construct an object of IdentityToken from
* a string representation and vice versa.*/
public interface IdentityTokenEncoderDecoder {

    public Object encodeIdentityToken(IdentityToken identityToken) throws Exception;

    public IdentityToken decodeIdentityToken(String identityTokenStrRepresentation) throws Exception;
}
