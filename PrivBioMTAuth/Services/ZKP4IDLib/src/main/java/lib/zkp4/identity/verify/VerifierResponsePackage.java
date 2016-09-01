package lib.zkp4.identity.verify;

/**
 * Created by hasini on 9/1/16.
 */
public class VerifierResponsePackage {
    private ProofInfo proofInfo;
    private boolean authResult;
    private String responseString;

    public ProofInfo getProofInfo() {
        return proofInfo;
    }

    public void setProofInfo(ProofInfo proofInfo) {
        this.proofInfo = proofInfo;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public boolean getAuthResult() {
        return authResult;
    }

    public void setAuthResult(boolean authResult) {
        this.authResult = authResult;
    }
}
