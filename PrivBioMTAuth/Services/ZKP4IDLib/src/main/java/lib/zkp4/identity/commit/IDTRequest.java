package lib.zkp4.identity.commit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasini on 4/6/16.
 */

/*This is the request sent by the client to initiate obtaining the IDT.*/

public class IDTRequest {
    /*following are the default set of params, any additional ones according to the implementation
        * can be added to additional param request.*/
    private String userName;
    private String attributeName;
    private String encryptedSecret;
    private String fromField;
    private String toField;
    //this is optional.
    private String attributeValue;

    private Map<String, Object> additionalReqParams = new HashMap<>();

    public Map<String, Object> getAdditionalReqParams() {
        return additionalReqParams;
    }

    public void setAdditionalReqParams(Map<String, Object> additionalReqParams) {
        this.additionalReqParams = additionalReqParams;
    }

    public void addAdditionalReqParam (String paramName, Object paramObject){
        this.additionalReqParams.put(paramName, paramObject);
    }

    public Object getAdditionalReqParam (String paramName) {
        return this.additionalReqParams.get(paramName);
    }

    public String getEncryptedSecret() {
        return encryptedSecret;
    }

    public void setEncryptedSecret(String encryptedSecret) {
        this.encryptedSecret = encryptedSecret;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getFromField() {
        return fromField;
    }

    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    public String getToField() {
        return toField;
    }

    public void setToField(String toField) {
        this.toField = toField;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
