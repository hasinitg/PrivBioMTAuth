package lib.zkp4.identity.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import lib.zkp4.identity.commit.IDTRequest;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.JSONIdentityTokenDecoder;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by hasini on 4/9/16.
 */
public class TestClient {
    public static String idpReqURL = "http://localhost:8080/idp/enroll";
    private static ClientConfig clientConfig = new DefaultClientConfig();

    public static void main(String[] args) throws Exception {
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        //create the IDT request
        IDTRequest idtRequest = new IDTRequest();
        idtRequest.setAttributeName("email");
        idtRequest.setFromField("hasini");

        Client client = Client.create(clientConfig);

        WebResource webResource = client.resource(idpReqURL);

        ClientResponse response = webResource./*type(MediaType.APPLICATION_JSON).*/
                accept(MediaType.APPLICATION_JSON).
                /*header("userName", "hasini").*/
                get(ClientResponse.class);

        System.out.println(response);
        InputStream is = response.getEntityInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String strResp = in.readLine();
        //Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        //String strResp = s.hasNext() ? s.next() : "";
        System.out.println(strResp);
        //JSONObject jj = new JSONObject(strResp);
        JSONIdentityTokenDecoder encdec = new JSONIdentityTokenDecoder();
        IdentityToken idt = encdec.decodeIdentityToken(strResp);
        System.out.println(idt.getIdentityCommitment().toString());

    }

}
