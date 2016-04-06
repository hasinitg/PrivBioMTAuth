package lib.zkp4.identity.commit;

import org.crypto.lib.commitments.pedersen.PedersenPublicParams;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by hasini on 4/6/16.
 */
public class IdentityToken {
    private String fromIdentity;
    private String toIdentity;
    private String attributeName;
    private BigInteger identityCommitment;
    private Timestamp creationTimestamp;
    private PedersenPublicParams pedersenParams;
    private String signature;
    private String publicCertAlias;

    //Todo: add additional params.

    public PedersenPublicParams getPedersenParams() {
        return pedersenParams;
    }

    public void setPedersenParams(PedersenPublicParams pedersenParams) {
        this.pedersenParams = pedersenParams;
    }

    public String getPublicCertAlias() {
        return publicCertAlias;
    }

    public void setPublicCertAlias(String publicCertAlias) {
        this.publicCertAlias = publicCertAlias;
    }

    public BigInteger getIdentityCommitment() {
        return identityCommitment;
    }

    public void setIdentityCommitment(BigInteger identityCommitment) {
        this.identityCommitment = identityCommitment;
    }

    public String getFromIdentity() {
        return fromIdentity;
    }

    public void setFromIdentity(String fromIdentity) {
        this.fromIdentity = fromIdentity;
    }

    public String getToIdentity() {
        return toIdentity;
    }

    public void setToIdentity(String toIdentity) {
        this.toIdentity = toIdentity;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
