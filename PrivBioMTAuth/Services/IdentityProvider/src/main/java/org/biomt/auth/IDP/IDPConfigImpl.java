package org.biomt.auth.IDP;

import org.crypto.lib.commitments.pedersen.PedersenPublicParams;

import java.security.PrivateKey;

/**
 * Created by hasini on 4/6/16.
 */
public class IDPConfigImpl implements lib.zkp4.identity.util.IDPConfig {
    //todo: this class needs to be static and this variable is non-static
    private IdentityRetriever identityRetriever = null;

    public void setIdentityRetriever(IdentityRetriever identityRetriever) {
        this.identityRetriever = identityRetriever;
    }

    @Override
    public PedersenPublicParams getPublicParams() {
        return null;
    }

    @Override
    public String getAttributeValue(String userName, String attributeName) {
        return identityRetriever.getBID();
    }

    @Override
    public String getConcatenationSign() {
        return null;
    }

    @Override
    public PrivateKey getRSAPrivateKey() {
        return null;
    }

    @Override
    public String getCertificateAlias() {
        return null;
    }
}
