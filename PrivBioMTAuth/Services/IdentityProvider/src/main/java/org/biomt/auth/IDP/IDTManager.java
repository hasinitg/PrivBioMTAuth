package org.biomt.auth.IDP;

import lib.zkp4.identity.commit.IDTFactory;
import lib.zkp4.identity.commit.IDTRequest;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.IDPConfig;
import lib.zkp4.identity.util.ZKP4IDException;

/**
 * Created by hasini on 4/6/16.
 */
public class IDTManager {
    public IdentityToken createIDT(IDTRequest idtRequest, String userName) throws BioMtAuthException {
        try {
            //TODO: IDP manager should call the classifier and generate the BID.
            //TODO: IDTManager should distribute the different secrets derived from the same password to BID generator and IDTFactory.
            IdentityRetriever idRet = new IdentityRetriever();
            idRet.setBID(idtRequest.getAttributeValue());
            IDPConfigImpl config = new IDPConfigImpl();
            config.setIdentityRetriever(idRet);

            IDTFactory fac = new IDTFactory();
            return fac.createIdentityToken(config, idtRequest, userName);

        } catch (ZKP4IDException e) {
            e.printStackTrace();
            throw new BioMtAuthException("Error in creating the identity token.");
        }
    }
}
