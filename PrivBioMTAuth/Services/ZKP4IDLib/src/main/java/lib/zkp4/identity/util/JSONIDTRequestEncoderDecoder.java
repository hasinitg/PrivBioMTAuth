package lib.zkp4.identity.util;

import lib.zkp4.identity.commit.IDTRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by hasini on 5/7/16.
 */
public class JSONIDTRequestEncoderDecoder implements IDTRequestEncoderDecoder {

    @Override
    public Object encodeIDTRequest(IDTRequest idtRequest) throws ZKP4IDException {
        JSONObject encodedIDTRequest = new JSONObject();
        try {
            encodedIDTRequest.put(Constants.USER_NAME, idtRequest.getUserName());
            encodedIDTRequest.put(Constants.FROM_IDENTITY, idtRequest.getFromField());
            encodedIDTRequest.put(Constants.TO_IDENTITY, idtRequest.getToField());
            encodedIDTRequest.put(Constants.ATTRIBUTE_NAME, idtRequest.getAttributeName());
            encodedIDTRequest.put(Constants.ATTRIBUTE_VALUE, idtRequest.getAttributeValue());
            encodedIDTRequest.put(Constants.ENCRYPTED_SECRET, idtRequest.getEncryptedSecret());
            /*TODO: encode additional parameters*/
            return encodedIDTRequest;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in encoding the IDT request in JSON.");
        }
    }

    @Override
    public IDTRequest decodeIDTRequest(Object IDTRequest) throws ZKP4IDException {
        IDTRequest idtRequest = new IDTRequest();
        try {
            /*TODO: verify that mandatory fields (e.g: attribute name, encrypted secret) are not null*/
            JSONObject jsonRequest = new JSONObject(new JSONTokener((String) IDTRequest));
            idtRequest.setUserName(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.USER_NAME));
            idtRequest.setFromField(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.FROM_IDENTITY));
            idtRequest.setToField(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.TO_IDENTITY));
            idtRequest.setAttributeName(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.ATTRIBUTE_NAME));
            idtRequest.setAttributeValue(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.ATTRIBUTE_VALUE));
            idtRequest.setEncryptedSecret(Utilz.getAttributeFromJSONMessage(jsonRequest, Constants.ENCRYPTED_SECRET));
            /*TODO: set additional parameters*/
            return idtRequest;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding the IDT request.");
        }
    }
}
