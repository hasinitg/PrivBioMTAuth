package org.biomt.auth.session;

import lib.zkp4.identity.util.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hasini on 8/31/16.
 */
public class SessionStore<String, SessionInfo> extends LinkedHashMap {
    private int maxSize = Constants.MAX_SIZE_PROOF_MAP_IN_MEMORY;

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
}
