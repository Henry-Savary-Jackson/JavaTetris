/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetris.utils.WebUtils;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author hsavaryjackson
 */
public class WebConfig {

    public static final String trustStorePassword = "adulthood25";

    public static WebClient getWebClient() {
	return WebClient.builder().clientConnector(getClientHttpConnector()).build();
    }

    public static SslContext getSSLContext() throws SSLException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
	
	InputStream truststoreInputStream = new FileInputStream("/Users/hsavaryjackson/NetBeansProjects/JavaTetris/keystore.p12");

	KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
	truststore.load(truststoreInputStream, trustStorePassword.toCharArray());

	TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	trustManagerFactory.init(truststore);

	var sslContext = SslContextBuilder.forClient().trustManager(trustManagerFactory)
		.build();

	return sslContext;
    }

    public static ClientHttpConnector getClientHttpConnector() {

	HttpClient httpClient = HttpClient.create()
		.secure(sslContextSpec -> {
		    try {
			sslContextSpec.sslContext(getSSLContext());
		    } catch (SSLException ex) {
			Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (KeyStoreException ex) {
			Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (IOException ex) {
			Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (CertificateException ex) {
			Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
		    }
		});

	return new ReactorClientHttpConnector(httpClient);
    }

}
