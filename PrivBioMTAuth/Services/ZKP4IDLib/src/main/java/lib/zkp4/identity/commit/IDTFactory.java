package lib.zkp4.identity.commit;

import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.IDPConfig;
import lib.zkp4.identity.util.Utilz;
import lib.zkp4.identity.util.ZKP4IDException;
import org.crypto.lib.CryptoLibConstants;
import org.crypto.lib.PKC.RSA.SignerVerifier;
import org.crypto.lib.commitments.pedersen.PedersenCommitment;
import org.crypto.lib.commitments.pedersen.PedersenCommitmentFactory;
import org.crypto.lib.commitments.pedersen.PedersenPublicParams;
import org.crypto.lib.exceptions.CryptoAlgorithmException;
import org.crypto.lib.util.CryptoUtil;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by hasini on 4/6/16.
 */
/* Identity Token Factory which creates an Identity Token with the identity commitment.
*  TODO: This should not be coupled to pedersen commitment. */
public class IDTFactory {
    //create idt
    public IdentityToken createIdentityToken(IDPConfig config, IDTRequest request, String userName)
            throws ZKP4IDException {
        try {
            //initialize pedersen commitment factory
            PedersenCommitmentFactory pedersenCommitmentFactory = new PedersenCommitmentFactory();
            PedersenPublicParams pedersenPublicParams;
            if (config == null || config.getPublicParams() == null) {
                pedersenPublicParams = pedersenCommitmentFactory.initialize();
            } else {
                pedersenPublicParams = config.getPublicParams();
            }

            pedersenCommitmentFactory.initialize(pedersenPublicParams);
            /*******TODO: get the secret related to ID creation as an input from the caller(IDTManager)*************/
            //TODO: decrypt the secret and obtain the big integer for the moment, assume that sending over SSL is fine.
            String encryptedSecret = request.getEncryptedSecret();
            //todo: decrypt the secret
            BigInteger secretBIG = new BigInteger(encryptedSecret);

            String attributeName = request.getAttributeName();
            /*TODO: if the attribute value is not in the request,
            get the user's attribute value from the AttributeFinder given the user name and the attribute name*/

            //String attributeValue = request.getAttributeValue();
            //if (attributeValue == null) {

            //Retrieve the attribute value through attribute call back handler.
            String attributeValue = config.getAttributeValue(userName, attributeName);
            //}
            BigInteger emailBIG = CryptoUtil.getCommittableThruHash(attributeValue, CryptoLibConstants.SECRET_BIT_LENGTH);

            PedersenCommitment commitment = pedersenCommitmentFactory.createCommitment(emailBIG, secretBIG);
            BigInteger commitmentBIG = commitment.getCommitment();

            IdentityToken IDT = new IdentityToken();

            IDT.setFromIdentity(request.getFromField());
            IDT.setToIdentity(request.getToField());
            IDT.setAttributeName(request.getAttributeName());
            IDT.setIdentityCommitment(commitmentBIG);

            //create current timestamp and expiration timestamp
            Date now = new Date();
            Timestamp currentTimestamp = new Timestamp(now.getTime());
            IDT.setCreationTimestamp(Utilz.reformatTimestamp(currentTimestamp));

            IDT.setPedersenParams(pedersenPublicParams);

            //todo: uncomment following after signing is implemented.
            //String concatenatedInfo = getConcatenatedInfoFromIDT(IDT, config.getConcatenationSign());

            //SignerVerifier signer = new SignerVerifier();
            //String signature = signer.signMessageAsString(concatenatedInfo, config.getRSAPrivateKey());
            //IDT.setSignature(signature);
            //IDT.setPublicCertAlias(config.getCertificateAlias());
            return IDT;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the identity commitment.");
        } catch (CryptoAlgorithmException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in creating the Pedersen commitment.");
        /*} catch (SignatureException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in signing the identity token.");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in signing the identity token.");*)*)*/
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ZKP4IDException("Error in parsing the timestamp.");
        }

    }

    public String getConcatenatedInfoFromIDT(IdentityToken IDT, String concatenation) {
        String concatSign;
        if(concatenation == null){
            concatSign = Constants.CONCATENATION;
        } else {
            concatSign = concatenation;
        }
        //concatenated string of information to be signed:
        //TODO: go through the additional params list and concatenate them too if present.
        StringBuilder concatenatedInfo = new StringBuilder();

        concatenatedInfo.append(IDT.getFromIdentity());
        concatenatedInfo.append(concatSign);

        if (IDT.getToIdentity() != null) {
            concatenatedInfo.append(IDT.getToIdentity());
            concatenatedInfo.append(concatSign);
        }

        concatenatedInfo.append(IDT.getAttributeName());
        concatenatedInfo.append(concatSign);

        concatenatedInfo.append(IDT.getIdentityCommitment().toString());
        concatenatedInfo.append(concatSign);

        concatenatedInfo.append(IDT.getCreationTimestamp().toString());
        concatenatedInfo.append(concatSign);

        PedersenPublicParams pedersenParams = IDT.getPedersenParams();
        concatenatedInfo.append(pedersenParams.getG().toString());
        concatenatedInfo.append(Constants.CONCATENATION);
        concatenatedInfo.append(pedersenParams.getP().toString());
        concatenatedInfo.append(Constants.CONCATENATION);
        concatenatedInfo.append(pedersenParams.getQ().toString());
        concatenatedInfo.append(Constants.CONCATENATION);
        concatenatedInfo.append(pedersenParams.getH().toString());
        concatenatedInfo.append(Constants.CONCATENATION);

        return concatenatedInfo.toString();
    }

}