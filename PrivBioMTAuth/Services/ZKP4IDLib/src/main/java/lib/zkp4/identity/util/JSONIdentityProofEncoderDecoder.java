package lib.zkp4.identity.util;

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import org.crypto.lib.zero.knowledge.proof.PedersenCommitmentProof;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasini on 8/28/16.
 */
public class JSONIdentityProofEncoderDecoder implements IdentityProofEncoderDecoder {
    @Override
    public JSONObject encodeIdentityProof(IdentityProof identityProof) throws Exception {

        JSONObject proofContent = new JSONObject();
        proofContent.put(Constants.PROOF_TYPE_NAME, identityProof.getProofType());
        proofContent.put(Constants.IDENTITY_TOKEN_NAME, identityProof.getIdentityTokenStringToBeProved());
        //TODO: if identity token is present in object representation, encode it and put it in here.
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
        JSONObject jsonProof = new JSONObject(new JSONTokener(identityProofRepresentation));
        IdentityProof proof = new IdentityProof();
        if (jsonProof.optString(Constants.IDENTITY_TOKEN_NAME)!=null){
            IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(
                    jsonProof.optString(Constants.IDENTITY_TOKEN_NAME));
            proof.setIdentityTokenToBeProved(IDT);
        }
        if (Constants.ZKP_I.equals(jsonProof.optString(Constants.PROOF_TYPE_NAME))) {
            String helperCommitmentString = jsonProof.optString(Constants.HELPER_COMMITMENT_NAME);
            if (helperCommitmentString != null) {
                BigInteger helperCommitment = new BigInteger(helperCommitmentString);
                proof.addHelperCommitment(helperCommitment);
            }
        }
        if ((Constants.ZKP_NI.equals(jsonProof.optString(Constants.HELPER_COMMITMENT_NAME))) ||
                (Constants.ZKP_NI_S.equals(jsonProof.optString(Constants.HELPER_COMMITMENT_NAME)))) {

            JSONArray helperCommitmentsString = jsonProof.optJSONArray(Constants.HELPER_COMMITMENTS_NAME);

            if (helperCommitmentsString != null && helperCommitmentsString.length() != 0) {
                JSONArray uValuesString = jsonProof.optJSONArray(Constants.U_VALUES_NAME);
                JSONArray vValuesString = jsonProof.optJSONArray(Constants.V_VALUES_NAME);
                for (int i = 0; i < 3; i++) {
                    proof.addHelperCommitment(new BigInteger(helperCommitmentsString.getString(i)));
                    PedersenCommitmentProof idProof = new PedersenCommitmentProof();
                    idProof.setU(new BigInteger(uValuesString.getString(i)));
                    idProof.setV(new BigInteger(vValuesString.getString(i)));
                    proof.addProof(idProof);
                }
            }
            JSONArray challenges = jsonProof.optJSONArray(Constants.CHALLENGES_NAME);
            if (challenges != null) {
                List<BigInteger> challengeBIG = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    challengeBIG.add(new BigInteger(challenges.getString(i)));
                }
                proof.setChallenges(challengeBIG);
            }
            //String timestamp = proofContent.optString(Constants.TIMESTAMP_AT_PROOF_CREATION);
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            //Date date = df.parse(timestamp);
            //Timestamp ts = new Timestamp(date.getTime());
            //proof.setTimestampAtProofCreation(ts);
        }
        return proof;
    }
}
