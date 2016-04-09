package lib.zkp4.identity.util;

import lib.zkp4.identity.commit.IdentityToken;
import org.crypto.lib.commitments.pedersen.PedersenPublicParams;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasini on 4/9/16.
 */
public class JSONIdentityTokenDecoder implements IdentityTokenDecoder {
    @Override
    public JSONObject encodeIdentityToken(IdentityToken identityToken) throws Exception {
        JSONObject IDTContent = new JSONObject();
        IDTContent.put(Constants.FROM_IDENTITY, identityToken.getFromIdentity());
        IDTContent.put(Constants.FROM_IDENTITY, identityToken.getFromIdentity());
        IDTContent.put(Constants.ATTRIBUTE_NAME, identityToken.getAttributeName());
        IDTContent.put(Constants.IDENTITY_COMMITMENT, identityToken.getIdentityCommitment().toString());
        IDTContent.put(Constants.CREATION_TIMESTAMP, identityToken.getCreationTimestamp().toString());
        //todo: encode biometric identity and single pseudonym certification if they are available in the IDT.
        //todo: attach the signature and the public cert
        //include the pedersen params
        JSONObject pedersenParams = new JSONObject();
        pedersenParams.put(Constants.P_PARAM, identityToken.getPedersenParams().getP().toString());
        pedersenParams.put(Constants.Q_PARAM, identityToken.getPedersenParams().getQ().toString());
        pedersenParams.put(Constants.G_PARAM, identityToken.getPedersenParams().getG().toString());
        pedersenParams.put(Constants.H_PARAM, identityToken.getPedersenParams().getH().toString());
        IDTContent.put(Constants.PEDERSEN_PARAMS, pedersenParams);
        //todo: include the signature
        IDTContent.put(Constants.SIGNATURE, identityToken.getSignature());
        IDTContent.put(Constants.PUBLIC_CERT_ALIAS, identityToken.getPublicCertAlias());
        return IDTContent;
    }

    @Override
    public IdentityToken decodeIdentityToken(String identityTokenStrRepresentation) throws Exception {
        JSONObject jsonIDT = new JSONObject(new JSONTokener(identityTokenStrRepresentation));

        IdentityToken identityToken = new IdentityToken();

        identityToken.setFromIdentity(jsonIDT.optString(Constants.FROM_IDENTITY));
        identityToken.setFromIdentity(jsonIDT.optString(Constants.FROM_IDENTITY));
        identityToken.setAttributeName(jsonIDT.optString(Constants.ATTRIBUTE_NAME));
        identityToken.setIdentityCommitment(new BigInteger(jsonIDT.optString(Constants.IDENTITY_COMMITMENT)));
        //read and set timestamp
        String currentTimestamp = jsonIDT.optString(Constants.CREATION_TIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat(Utilz.TIME_STAMP_FORMAT);
        Date creationTime = df.parse(currentTimestamp);
        identityToken.setCreationTimestamp(new Timestamp(creationTime.getTime()));

        String signature = jsonIDT.optString(Constants.SIGNATURE);
        identityToken.setSignature(signature);

        String certAlias = jsonIDT.optString(Constants.PUBLIC_CERT_ALIAS);
        identityToken.setPublicCertAlias(certAlias);

        JSONObject paramsObj = jsonIDT.optJSONObject(Constants.PEDERSEN_PARAMS);
        PedersenPublicParams params = new PedersenPublicParams();
        params.setP(new BigInteger(paramsObj.optString(Constants.P_PARAM)));
        params.setQ(new BigInteger(paramsObj.optString(Constants.Q_PARAM)));
        params.setG(new BigInteger(paramsObj.optString(Constants.G_PARAM)));
        params.setH(new BigInteger(paramsObj.optString(Constants.H_PARAM)));

        identityToken.setPedersenParams(params);

        return identityToken;
    }
}
