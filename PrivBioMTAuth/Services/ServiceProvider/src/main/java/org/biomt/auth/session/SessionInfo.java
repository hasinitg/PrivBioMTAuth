package org.biomt.auth.session;

/**
 * Created by hasini on 8/31/16.
 */
public class SessionInfo {
    private String userName;
    private  String sessionID;
    private String spGivenSessionID;
    private boolean authStatus;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public boolean isAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(boolean authStatus) {
        this.authStatus = authStatus;
    }

    public String getSpGivenSessionID() {
        return spGivenSessionID;
    }

    public void setSpGivenSessionID(String spGivenSessionID) {
        this.spGivenSessionID = spGivenSessionID;
    }
}
