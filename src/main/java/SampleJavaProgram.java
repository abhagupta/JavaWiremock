/**
 * Created by agupt13 on 6/14/16.
 */

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class SampleJavaProgram {
   // protected static WireMockServer wireMockServer;

    protected static WireMockServer wireMockServer  = new WireMockServer(wireMockConfig().port(9988).httpsPort(9943).keystorePath("src/main/resources/trust.jks").keystorePassword("password"));


   // public WireMockRule wireMockRule = new WireMockRule(8089);
//   static {
//       HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("127.0.0.1"));
//   }
   static {
       HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
       {
           public boolean verify(String hostname, SSLSession session)
           {
               // ip address of the service URL(like.23.28.244.244)
               if (hostname.equals("127.0.0.1"))
                   return true;
               return false;
           }
       });
   }

    public static void main(String[] args){
        Logger logger = LoggerFactory.getLogger(SampleJavaProgram.class);
        logger.info("Hello World");

        wireMockServer.start();
        String hostName = "127.0.0.1";
        int portNumber = Integer.parseInt("9988");


        wireMockServer.stubFor(get(urlEqualTo("/my/resource"))
                .withHeader("Accept", equalTo("text/xml"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/xml")
                                .withBody("<response>Some content</response>")));

                try {
                   // URL myURL = new URL("http://127.0.0.1:8080/_admin");
                    URL myURL = new URL("https://127.0.0.1:9943/__admin");
                    HttpsURLConnection myURLConnection = ( HttpsURLConnection) myURL.openConnection();

                    System.out.println("reached here" );
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            myURLConnection.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null)
                        System.out.println(inputLine);
                    in.close();
                }
                catch (MalformedURLException e) {
                    // new URL() failed
                    // ...
                }
                catch (IOException e) {
                    // openConnection() failed
                    // ...
                    e.printStackTrace();
                }
        wireMockServer.stop();

//        try (
//                Socket echoSocket = new Socket(hostName, portNumber);
//                PrintWriter out =
//                        new PrintWriter(echoSocket.getOutputStream(), true);
//                BufferedReader in =
//                        new BufferedReader(
//                                new InputStreamReader(echoSocket.getInputStream()));
//                BufferedReader stdIn =
//                        new BufferedReader(
//                                new InputStreamReader(System.in))
//        ) {
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
