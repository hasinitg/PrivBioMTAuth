package lib.zkp4.identity.verify;

/**
 * Created with IntelliJ IDEA.
 * User: hasini
 * Date: 12/15/14
 * Time: 8:05 PM
 */

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import org.crypto.lib.commitments.pedersen.PedersenCommitment;

import java.math.BigInteger;

/**
 * This represents the information kept in in-memory until the process of identity proof is completed.
 */
public class ProofInfo {
    private String sessionID;
    private IdentityProof identityProof;
    /*Challenge sent by the verifier*/
    private BigInteger challenge;
    private int verificationStatus;

    public IdentityProof getIdentityProof() {
        return identityProof;
    }

    public void setIdentityProof(IdentityProof identityProof) {
        this.identityProof = identityProof;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public BigInteger getChallengeValue() {
        return challenge;
    }

    public void setChallengeValue(BigInteger challenge) {
        this.challenge = challenge;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(int verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

}
