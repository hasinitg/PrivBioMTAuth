package org.biomt.auth.authapp;

/**
 * Created by hasini on 8/20/16.
 */
public class Config {
    /*IDP URL*/
    //public static final String IDP_URL = "http://10.186.83.83:8080/idp/enroll/post";
    // public static final String IDP_URL = "http://192.168.0.101:8080/idp/enroll/post";
    public static final String IDP_URL = "http://10.186.92.87:8080/idp/enroll/post";

    //public static final String TEST_URL = "http://10.186.83.83:8080/abcbank/auth";
    //public static final String TEST_URL = "http://192.168.0.101:8080/abcbank/auth";
    public static final String TEST_URL = "http://10.186.92.87:8080/abcbank/auth";

    public static final String LOG_DELIMITTER = ":";
    public static final String TAG_MAIN = "PERF_TEST";
    public static final String TAG_START_IDT_REQ = "START_IDT_REQ";
    public static final String TAG_INT_IDT_REQ = "INT_IDT_REQ";
    public static final String TAG_END_IDT_REQ = "END_IDT_REQ";

    public static final String TAG_START_IDT_TRNS = "START_IDT_TRNS";
    public static final String TAG_INT_IDT_TRNS = "INT_IDT_TRNS";
    public static final String TAG_END_IDT_TRNS = "END_IDT_TRNS";

    public static final String TAG_START_AUTH = "START_AUTH";
    public static final String TAG_CHALLENGE_RECEIVED = "CHLLNG_RECEIVED";
    public static final String TAG_START_PROOF = "START_PROOF";
    public static final String TAG_END_AUTH = "END_AUTH";
}
