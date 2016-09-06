package lib.zkp4.identity.util;

import javax.xml.soap.SAAJResult;

/**
 * Created by hasini on 4/6/16.
 */
public class Constants {
    public static final String CONCATENATION = "|";

    /*String names of IdentityToken class parameters.*/
    public static final String FROM_IDENTITY = "fromIdentity";
    public static final String TO_IDENTITY = "toIdentity";
    public static final String ATTRIBUTE_NAME = "attributeName";
    public static final String IDENTITY_COMMITMENT = "identityCommitment";
    public static final String CREATION_TIMESTAMP = "creationTimestamp";
    public static final String PEDERSEN_PARAMS = "pedersenParams";
        public static final String P_PARAM = "p";
        public static final String Q_PARAM = "q";
        public static final String G_PARAM = "g";
        public static final String H_PARAM = "h";
    public static final String SIGNATURE = "signature";
    public static final String PUBLIC_CERT_ALIAS = "publicCertAlias";

    /*String names for IDT Request parameters*/
    public static final String ATTRIBUTE_VALUE = "attributeValue";
    public static final String ENCRYPTED_SECRET = "encryptedSecret";
    public static final String USER_NAME = "userName";

    /*ZKP Proof Types*/
    public static String ZKP_I = "ZKP_I";
    public static String ZKP_NI = "ZKP_NI";
    public static String ZKP_NI_S = "ZKP_NI_S";

    /*Proof request types*/
    public static final String REQUEST_TYPE_NAME = "RequestName";
    public static final String REQ_ZKP_I_INITIAL = "ZKP_I_Initial_Request";
    public static final String REQ_ZKP_I_CHALLENGE_RESPONSE = "ZKP_I_Challenge_Response";
    public static final String REQ_ZKP_NI = "ZKP_NI";
    public static final String REQ_ZKP_NI_S = "ZKP_NI_S";

    /*Constants related to Identity Proofs*/
    public static final String PROOF_TYPE_NAME = "proofType";
    public static final String IDENTITY_TOKEN_NAME = "identityToken";
    public static final String HELPER_COMMITMENT_NAME = "helper";
    public static final String HELPER_COMMITMENTS_NAME = "helpers";
    public static final String U_VALUE_NAME = "U_Value";
    public static final String U_VALUES_NAME = "U_Values";
    public static final String V_VALUE_NAME = "V_Value";
    public static final String V_VALUES_NAME = "V_Values";
    public static final String CHALLENGE_NAME = "challenge";
    public static final String CHALLENGES_NAME = "challenges";
    public static final String TIMESTAMP_AT_PROOF_CREATION = "timestampAtProofCreation";
    public static final String SESSION_ID_NAME = "session_id";

    /*Identity verification status at verifier.*/
    public static final int IDENTITY_VERIFIED = 1;
    public static final int IDENTITY_VERIFICATION_PENDING = 0;

    public static final String AUTH_RESULT_NAME = "Auth_Result";
    public static final String AUTH_SUCCESS = "true";
    public static final String AUTH_FAILURE = "false";

    /*Constants related with proof store*/
    public static final int MAX_SIZE_PROOF_MAP_IN_MEMORY = 10000;

    //http response codes
    public static final int HTTP_CODE_ERROR = 500;
    public static final int HTTP_CODE_OK = 200;
}
