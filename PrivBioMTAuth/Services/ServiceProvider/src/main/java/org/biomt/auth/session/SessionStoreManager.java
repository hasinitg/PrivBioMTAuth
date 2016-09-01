package org.biomt.auth.session;

import lib.zkp4.identity.verify.ProofInfo;
import lib.zkp4.identity.verify.ProofStore;

/**
 * Created by hasini on 8/31/16.
 */
public class SessionStoreManager {
    private static volatile SessionStoreManager sessionStoreManager;
    private static SessionStore<String, SessionInfo> sessionStore;

    private SessionStoreManager() {
        sessionStore = new SessionStore<>();
    }

    public static SessionStoreManager getInstance() {
        if (sessionStoreManager == null) {
            synchronized (SessionStoreManager.class) {
                if (sessionStoreManager == null) {
                    sessionStoreManager = new SessionStoreManager();
                    return sessionStoreManager;
                }
            }
        }
        return sessionStoreManager;
    }

    public void addSessionInfo(String userName, SessionInfo sessionInfo) {
        sessionStore.put(userName, sessionInfo);
    }

    public SessionInfo getSessionInfo(String userName) {
        return (SessionInfo) sessionStore.get(userName);
    }

    public void removeSessionInfo(String userName) {
        sessionStore.remove(userName);
    }
}
