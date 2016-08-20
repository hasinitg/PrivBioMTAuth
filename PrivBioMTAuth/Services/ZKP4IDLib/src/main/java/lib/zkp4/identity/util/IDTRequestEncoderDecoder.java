package lib.zkp4.identity.util;

import lib.zkp4.identity.commit.IDTRequest;

/**
 * Created by hasini on 5/7/16.
 */

/*This interface needs to be implemented for the specific encoding decoding format of the identity token request.*/

public interface IDTRequestEncoderDecoder {

    public Object encodeIDTRequest (IDTRequest idtRequest) throws ZKP4IDException;

    public IDTRequest decodeIDTRequest (Object IDTRequest) throws ZKP4IDException;

}
