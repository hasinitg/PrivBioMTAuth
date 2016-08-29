package lib.zkp4.identity.proof;

/**
 * Created with IntelliJ IDEA.
 * User: hasini
 * Date: 12/14/14
 * Time: 1:02 PM
 */

import org.crypto.lib.zero.knowledge.proof.PedersenCommitmentProof;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents the identity information sent in the proof of identity in each ZKPK protocol.
 */
public class IdentityProof {
    //multiple helper commitments, proofs and challenges are included to support non-interactive proof if required.
    private String proofType;
    List<BigInteger> helperCommitments = new ArrayList<>();
    private List<PedersenCommitmentProof> proofs = new ArrayList<>();
    private List<BigInteger> challenges = new ArrayList<>();
    private Timestamp timestampAtProofCreation;

    public List<BigInteger> getHelperCommitments() {
        return helperCommitments;
    }

    public void setHelperCommitments(List<BigInteger> helperCommitments) {
        this.helperCommitments = helperCommitments;
    }

    public void addHelperCommitment(BigInteger helperCommitment) {
        this.helperCommitments.add(helperCommitment);
    }

    public BigInteger getHelperCommitment() {
        if (helperCommitments != null && helperCommitments.size() != 0) {
            return this.helperCommitments.get(0);
        } else {
            return null;
        }
    }

    public List<PedersenCommitmentProof> getProofs() {
        return proofs;
    }

    public void setProofs(List<PedersenCommitmentProof> proofs) {
        this.proofs = proofs;
    }

    public void addProof(PedersenCommitmentProof proof) {
        proofs.add(proof);
    }

    public PedersenCommitmentProof getProof() {
        if (proofs != null && proofs.size() != 0) {
            return proofs.get(0);
        } else {
            return null;
        }
    }

    public Timestamp getTimestampAtProofCreation() {
        return timestampAtProofCreation;
    }

    public void setTimestampAtProofCreation(Timestamp timestampAtProofCreation) {
        this.timestampAtProofCreation = timestampAtProofCreation;
    }

    public List<BigInteger> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<BigInteger> challenges) {
        this.challenges = challenges;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

}
