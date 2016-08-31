package lib.zkp4.identity.verify;

/**
 * Created with IntelliJ IDEA.
 * User: hasini
 * Date: 12/10/14
 * Time: 12:43 PM
 */

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONMiscEncoderDecoder;
import lib.zkp4.identity.util.MiscEncoderDecoder;
import lib.zkp4.identity.util.ZKP4IDException;
import org.crypto.lib.PKC.RSA.SignerVerifier;
import org.crypto.lib.commitments.pedersen.PedersenCommitment;
import org.crypto.lib.commitments.pedersen.PedersenPublicParams;
import org.crypto.lib.exceptions.CryptoAlgorithmException;
import org.crypto.lib.zero.knowledge.proof.PedersenCommitmentProof;
import org.crypto.lib.zero.knowledge.proof.ZKPPedersenCommitment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This performs the verification part of each protocol.
 */
public class IdentityVerificationHandler {

    public ProofInfo handleInitialZKPIRequest(IdentityProof proof) throws ZKP4IDException {
        try {
            IdentityToken IDT = proof.getIdentityTokenToBeProved();
            //create challenge
            ZKPPedersenCommitment zkp = new ZKPPedersenCommitment(IDT.getPedersenParams());
            BigInteger challenge = zkp.createChallengeForInteractiveZKP();
            //create session-id
            String sessionID = UUID.randomUUID().toString();
            //create proof info and store
            ProofInfo proofInfo = new ProofInfo();
            proofInfo.setIdentityProof(proof);
            proofInfo.setChallengeValue(challenge);
            proofInfo.setSessionID(sessionID);
            proofInfo.setVerificationStatus(Constants.IDENTITY_VERIFICATION_PENDING);
            ProofStoreManager.getInstance().addProofInfo(sessionID, proofInfo);
            return proofInfo;

        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the challenge.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding the identity proof.");
        }
    }

    public boolean verifyZKPI(IdentityProof identityProof, String sessionID) throws ZKP4IDException {
        try {
            //find previous info from proof store
            ProofInfo proofInfo = ProofStoreManager.getInstance().getProofInfo(sessionID);
            IdentityProof savedIdentityProof = proofInfo.getIdentityProof();
            if (proofInfo != null) {
                //verify the proof
                PedersenPublicParams params = savedIdentityProof.getIdentityTokenToBeProved().getPedersenParams();

                ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(params);
                PedersenCommitment helperCommitment = new PedersenCommitment();
                helperCommitment.setCommitment(savedIdentityProof.getHelperCommitment());

                PedersenCommitment originalCommitment = new PedersenCommitment();
                originalCommitment.setCommitment(savedIdentityProof.getIdentityTokenToBeProved().getIdentityCommitment());
                boolean verificationResult = ZKPK.verifyInteractiveZKP(originalCommitment, helperCommitment,
                        proofInfo.getChallengeValue(), identityProof.getProof());
                ProofStoreManager.getInstance().removeProofInfo(sessionID);
                return verificationResult;
            } else {
                throw new ZKP4IDException("No previous records of this proof found.");
            }
        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in verifying the zero knowledge proof.");
        }
    }
    //TODO: Fix other types of ZKP later
    /*public String verifyZKPNI(JSONObject jsonReq) throws RahasNymException {
        try {
            IdentityMessagesEncoderDecoder encoderDecoder = new IdentityMessagesEncoderDecoder();

            IdentityToken token = encoderDecoder.decodeIdentityTokenContent((JSONObject) jsonReq.opt(Constants.IDT));
            verifySignatureOnIDT(token);
            PedersenCommitment originalCommitment = new PedersenCommitment();
            originalCommitment.setCommitment(token.getIdentityCommitment());

            JSONObject proofContent = (JSONObject) jsonReq.opt(Constants.PROOF);
            IdentityProof proof = encoderDecoder.decodeIdentityProofContent(proofContent, Constants.ZKP_NI);
            List<BigInteger> challenges = proof.getChallenges();
            List<BigInteger> helperCommitments = proof.getHelperCommitments();
            List<PedersenCommitmentProof> proofs = proof.getProofs();
            List<PedersenCommitment> helperCommitmentsList = new ArrayList<>();
            for (BigInteger helperCommitment : helperCommitments) {
                PedersenCommitment commitment = new PedersenCommitment();
                commitment.setCommitment(helperCommitment);
                helperCommitmentsList.add(commitment);
            }
            PedersenPublicParams params = token.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(params);
            boolean result = ZKPK.verifyNonInteractiveZKP(originalCommitment, helperCommitmentsList, challenges, proofs);

            return encoderDecoder.createAuthResultMessage(result);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in decoding identity proof.");
        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying identity proof.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying identity proof.");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in decoding identity token or identity proof.");
        }
    }

    public String verifyZKPNIS(JSONObject jsonProof) throws RahasNymException {
        try {
            IdentityMessagesEncoderDecoder encoderDecoder = new IdentityMessagesEncoderDecoder();

            IdentityToken token = encoderDecoder.decodeIdentityTokenContent((JSONObject) jsonProof.opt(Constants.IDT));
            verifySignatureOnIDT(token);
            PedersenCommitment originalCommitment = new PedersenCommitment();
            originalCommitment.setCommitment(token.getIdentityCommitment());

            JSONObject proofContent = (JSONObject) jsonProof.opt(Constants.PROOF);

            IdentityProof proof = encoderDecoder.decodeIdentityProofContent(proofContent, Constants.ZKP_NI_S);
            List<BigInteger> challenges = proof.getChallenges();
            List<BigInteger> helperCommitments = proof.getHelperCommitments();
            List<PedersenCommitmentProof> proofs = proof.getProofs();
            List<PedersenCommitment> helperCommitmentsList = new ArrayList<>();
            for (BigInteger helperCommitment : helperCommitments) {
                PedersenCommitment commitment = new PedersenCommitment();
                commitment.setCommitment(helperCommitment);
                helperCommitmentsList.add(commitment);
            }
            PedersenPublicParams params = token.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(params);
            boolean result = ZKPK.verifyNonInteractiveZKPWithSignature(originalCommitment, helperCommitmentsList,
                    receipt.getBytes(), challenges, proofs);

            return encoderDecoder.createAuthResultMessage(result);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in decoding identity proof.");
        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying identity proof.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying identity proof.");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in decoding identity token or identity proof.");
        }
    }*/

    //TODO: verify signature on the IDT.
    /*public void verifySignatureOnIDT(IdentityToken identityToken) throws RahasNymException {
        try {
            String concatenatedInfo = new IdentityMessagesEncoderDecoder().getConcatenatedInfoFromIDT(identityToken);
            SignerVerifier verifier = new SignerVerifier();
            Certificate publicCert = VerifierCallBackManager.getTrustedCert(identityToken.getPublicCertAlias());

            boolean sigVerified = verifier.verifySignature(concatenatedInfo, identityToken.getSignature(), publicCert);

            if (!sigVerified) {
                throw new RahasNymException("Signature verification on the identity token failed.");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying the signature on IDT");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying the signature on IDT");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying the signature on IDT");
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RahasNymException("Error in verifying the signature on IDT");
        }
    }*/

}
