package lib.zkp4.identity.util;

import lib.zkp4.identity.proof.IdentityProof;
import org.crypto.lib.zero.knowledge.proof.PedersenCommitmentProof;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by hasini on 8/28/16.
 */
public class JSONIdentityProofEncoderDecoder implements IdentityProofEncoderDecoder {
    @Override
    public JSONObject encodeIdentityProof(IdentityProof identityProof) throws Exception {

        JSONObject proofContent = new JSONObject();
        proofContent.put(Constants.PROOF_TYPE_NAME, identityProof.getProofType());

        if (Constants.ZKP_I.equals(identityProof.getProofType())) {

            if (identityProof.getHelperCommitment() != null) {
                proofContent.put(Constants.HELPER_COMMITMENT_NAME, identityProof.getHelperCommitment().toString());
            }
            if (identityProof.getProof() != null) {
                proofContent.put(Constants.U_VALUE_NAME, identityProof.getProof().getU().toString());
                proofContent.put(Constants.V_VALUE_NAME, identityProof.getProof().getV().toString());
            }
        } else if ((Constants.ZKP_NI.equals(identityProof.getProofType())) || (Constants.ZKP_NI_S.equals(identityProof.getProofType()))) {
            if (identityProof.getChallenges() != null) {
                JSONArray challenges = new JSONArray();
                List<BigInteger> proofChallenges = identityProof.getChallenges();
                for (BigInteger proofChallenge : proofChallenges) {
                    challenges.put(proofChallenge.toString());
                }
                proofContent.put(Constants.CHALLENGES_NAME, challenges);
            }
            if (identityProof.getHelperCommitments() != null && identityProof.getHelperCommitments().size() != 0) {
                JSONArray helperCommitmentsArray = new JSONArray();
                List<BigInteger> helperCommitments = identityProof.getHelperCommitments();
                for (BigInteger helperCommitment : helperCommitments) {

                    helperCommitmentsArray.put(helperCommitment.toString());
                }
                proofContent.put(Constants.HELPER_COMMITMENTS_NAME, helperCommitmentsArray);
                List<PedersenCommitmentProof> proofs = identityProof.getProofs();
                JSONArray uValues = new JSONArray();
                JSONArray vValues = new JSONArray();
                for (PedersenCommitmentProof pedersenCommitmentProof : proofs) {
                    uValues.put(pedersenCommitmentProof.getU().toString());
                    vValues.put(pedersenCommitmentProof.getV().toString());
                }
                proofContent.put(Constants.U_VALUES_NAME, uValues);
                proofContent.put(Constants.V_VALUES_NAME, vValues);
                proofContent.put(Constants.TIMESTAMP_AT_PROOF_CREATION, identityProof.getTimestampAtProofCreation().toString());
            }
        }
        return proofContent;
    }

    @Override
    public IdentityProof decodeIdentityProof(String identityProofRepresentation) throws Exception {
        return null;
    }
}
