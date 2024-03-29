package org.biomt.auth.authapp;

/**
 * Created by hasini on 3/15/16.
 */
public class AuthConstants {
    public static final String SP_URL_NAME = "SP_URL";
    public static final String USER_NAME_NAME = "USER_NAME";
    public static final String BIOMETRIC_ATTRIBUTE_NAME = "Biometrics";
    public static final String IDENTITY_TOKEN_STRING_NAME = "IDT_STRING";
    public static final String IDENTITY_TOKEN_NAME = "IDT";
    public static final String IDENTITY_PROOF_NAME = "IdentityProof";

    /*Intent Actions (Results)*/
    public static final String ACTION_RESULT_AUTH_ZKP = "org.biomt.auth.authapp.ACTION_RESULT_AUTH_ZKP";
    public static final String ACTION_RESULT_ENROLLMENT = "org.biomt.auth.authapp.ACTION_RESULT_ENROLLMENT";

    public static final String ENCODING = "UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json";

    /*Intent Request Codes*/
    public static final int REQUEST_CODE_ENROLL = 1;
    public static final int REQUEST_CODE_ZKP_INITIAL = 2;
    public static final int REQUEST_CODE_ZKP_CHALLENGE_RESPONSE = 3;

    /*Constants defining different information passed back and forth through different intents of AuthApp*/
    public static final String INFO_CODE_ENROLL_RESP = "ENROLL_RESPONSE";
    public static final String INFO_CODE_AUTH_RESP_ZKP_I_INITIAL = "ZKP_I_INITIAL_RESPONSE";
    public static final String INFO_CODE_AUTH_RESP_ZKP_I_CHALLENGE_RESPONSE =  "ZKP_I_CHALLENGE_RESPONSE";

    public static final String INITIAL_ID_PROOF_NAME = "INITIAL_IDENTITY_PROOF_NAME";
    public static final String HELPER_X_NAME = "HELPER_X";
    public static final String HELPER_R_NAME = "HELPER_R";

    /*Constant defining auth result information passed from auth app to client app*/
    public static final String INFO_CODE_AUTH_RESP = "ZKP_AUTH_RESP";

    /*Result Codes*/
    public static final int SUCCESS = 11;
    public static final int FAILURE = 00;

    /*Result Signals*/
    public static final String SUCCESS_NAME = "SUCCESS: ";
    public static final String ERROR_NAME = "ERROR: ";
}
