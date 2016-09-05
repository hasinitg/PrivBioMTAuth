package lib.zkp4.identity.verify;

/**
 * Created by hasini on 9/5/16.
 */
public class AuthResult {
    private String sessionID;
    private Boolean authResult;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Boolean getAuthResult() {
        return authResult;
    }

    public void setAuthResult(Boolean authResult) {
        this.authResult = authResult;
    }
}
