package lib.zkp4.identity.proof;

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;
import lib.zkp4.identity.util.ZKP4IDException;
import org.crypto.lib.CryptoLibConstants;
import org.crypto.lib.commitments.pedersen.PedersenCommitment;
import org.crypto.lib.commitments.pedersen.PedersenPublicParams;
import org.crypto.lib.exceptions.CryptoAlgorithmException;
import org.crypto.lib.util.CryptoUtil;
import org.crypto.lib.zero.knowledge.proof.PedersenCommitmentProof;
import org.crypto.lib.zero.knowledge.proof.ZKPPedersenCommitment;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hasini on 8/30/16.
 */
public class IdentityProofCreator {

    /*Creates initial request for interactive ZKPK - use this if the caller has string representation of the identity token*/
    public IdentityProof createInitialProofForZKPI(String identityTokenString)
            throws ZKP4IDException {
        try {
            //decode the identity token from string
            IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            IdentityProof proof = new IdentityProof();
            proof.setProofType(Constants.ZKP_I);
            proof.setIdentityTokenStringToBeProved(identityTokenString);
            PedersenPublicParams pedersenPublicParams = IDT.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(pedersenPublicParams);

            PedersenCommitment helperCommitment = ZKPK.createHelperProblem(null);
            proof.addHelperCommitment(helperCommitment.getCommitment());

            return proof;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding identity token.");
        }
    }

    /*Creates initial request for interactive ZKPK - use this if the caller has object representation of the identity token*/
    public IdentityProofPackage createInitialProofForZKPI(IdentityToken identityToken) throws ZKP4IDException {
        try {

            IdentityProof proof = new IdentityProof();
            proof.setProofType(Constants.ZKP_I);
            proof.setIdentityTokenStringToBeProved(new JSONIdentityTokenEncoderDecoder().encodeIdentityToken(identityToken).toString());

            PedersenPublicParams pedersenPublicParams = identityToken.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(pedersenPublicParams);

            PedersenCommitment helperCommitment = ZKPK.createHelperProblem(null);
            proof.addHelperCommitment(helperCommitment.getCommitment());

            IdentityProofPackage identityProofPackage = new IdentityProofPackage();
            identityProofPackage.setIdentityProof(proof);
            identityProofPackage.setHelperX(helperCommitment.getX());
            identityProofPackage.setHelperR(helperCommitment.getR());

            return identityProofPackage;

        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the initial interactive ZKPK.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in encoding the identity token while creating the initial interactive ZKPK.");
        }
    }

    /*Create the challenge response */
    public IdentityProof createProofForZKPI(BigInteger challenge, String secret, String identity, String identityTokenString,
                                            IdentityProof initialIdentityProof, String helperX, String helperR) throws ZKP4IDException {
        try {
            BigInteger secretBIG = CryptoUtil.getCommittableThruHash(secret, CryptoLibConstants.SECRET_BIT_LENGTH);
            BigInteger identityBIG = CryptoUtil.getCommittableThruHash(identity, CryptoLibConstants.SECRET_BIT_LENGTH);

            PedersenCommitment pedersenCommitment = new PedersenCommitment();
            pedersenCommitment.setX(identityBIG);
            pedersenCommitment.setR(secretBIG);

            PedersenCommitment helperCommitment = new PedersenCommitment();
            helperCommitment.setCommitment(initialIdentityProof.getHelperCommitment());
            helperCommitment.setX(new BigInteger(helperX));
            helperCommitment.setR(new BigInteger(helperR));

            IdentityToken identityToken = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);
            PedersenPublicParams pedersenPublicParams = identityToken.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(pedersenPublicParams);

            IdentityProof proof = new IdentityProof();
            proof.setProofType(Constants.ZKP_I);
            proof.setIdentityTokenStringToBeProved(identityTokenString);
            PedersenCommitmentProof commitmentProof = ZKPK.createProofForInteractiveZKP(pedersenCommitment, helperCommitment, challenge);
            proof.addProof(commitmentProof);

            return proof;

        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the idenity proof.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the identity proof");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in decoding the identity token used for proof.");
        }
    }
}
