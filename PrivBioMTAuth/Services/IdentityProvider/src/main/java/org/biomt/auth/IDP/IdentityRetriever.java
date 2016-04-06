package org.biomt.auth.IDP;

/**
 * Created by hasini on 4/6/16.
 */
/*IDP implementation specific call back handler to retrieve identity value to create the commitment.*/
public class IdentityRetriever {

    private String BID;

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }
}
