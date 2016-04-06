package lib.zkp4.identity.util;

import org.crypto.lib.commitments.pedersen.PedersenPublicParams;

import java.security.PrivateKey;

/**
 * Created by hasini on 4/6/16.
 */
public interface IDPConfig {
    /*TODO: Ideally this should not be bound to any specific commitment.*/
    public PedersenPublicParams getPublicParams();

    public String getAttributeValue (String userName, String attributeName);

    public String getConcatenationSign();

    public PrivateKey getRSAPrivateKey() ;

    public String getCertificateAlias() ;

}
