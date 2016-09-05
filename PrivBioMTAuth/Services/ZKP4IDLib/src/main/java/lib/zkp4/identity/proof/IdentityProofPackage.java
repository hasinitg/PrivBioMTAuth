package lib.zkp4.identity.proof;

import java.math.BigInteger;

/**
 * Created by hasini on 9/5/16.
 */
/*This is to enclose 1) the IdentityProof - which is to be sent to the verifier and 2) the secrets of helper commitment
* to be kept at the prover.*/
public class IdentityProofPackage {

    private IdentityProof identityProof;
    private BigInteger helperX;
    private BigInteger helperR;

    public IdentityProof getIdentityProof() {
        return identityProof;
    }

    public void setIdentityProof(IdentityProof identityProof) {
        this.identityProof = identityProof;
    }

    public BigInteger getHelperX() {
        return helperX;
    }

    public void setHelperX(BigInteger helperX) {
        this.helperX = helperX;
    }

    public BigInteger getHelperR() {
        return helperR;
    }

    public void setHelperR(BigInteger helperR) {
        this.helperR = helperR;
    }

}
