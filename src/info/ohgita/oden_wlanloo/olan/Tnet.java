package info.ohgita.oden_wlanloo.olan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import com.actionbarsherlock.R;

import android.content.Context;
import android.net.wifi.WifiInfo;

public class Tnet extends Olan {
	Context context;
	String NAME = "Tnet";

	@Override
	void connect() {

	}

	@Override
	void login() {
		
	}

	@Override
	void logout() {

	}

	@Override
	boolean isLogin() {

		return false;
	}

	@Override
	boolean isConnect(WifiInfo current_wifi) {
		if (current_wifi.getSSID().contentEquals("Tnet")) {
			return true;
		}
		return false;
	}

	String post(String url, HashMap<String, String> body_param) throws MalformedURLException, IOException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			KeyManagementException {
		
		URLConnection connection = new URL(url).openConnection();
		HttpsURLConnection con = (HttpsURLConnection) connection;
		
		/* Setting for SSL */
		
		InputStream ks_is = this.context.getResources().openRawResource(R.raw.tnet_alcatel_webview_bin);
		KeyStore ks = KeyStore.getInstance("BKS");
		ks.load(ks_is, "ODENWLANLOO_CERT_STOREPASS".toCharArray());
		ks_is.close();

		SSLContext sslcon = SSLContext.getInstance("TLS");

		TrustManager x509 = new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
		};
		sslcon.init(null, new TrustManager[] { x509 }, null);

		con.setSSLSocketFactory(sslcon.getSocketFactory());
		con.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		/* Make a POST request */
		
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setChunkedStreamingMode(0);
		OutputStreamWriter osw = new OutputStreamWriter( con.getOutputStream() );
		Iterator<String> itr = body_param.keySet().iterator();
		while(itr.hasNext()){
			String key = itr.next();
			String value = body_param.get(key);
			osw.write(key+"="+value + "\n");
		}
		osw.flush();
		osw.close();
		
		/* Connect */
		
		con.connect();
		
		/* Read response */
		StringBuffer buffer = new StringBuffer();
		InputStreamReader is = new InputStreamReader(con.getInputStream());
		BufferedReader reader = new BufferedReader(is);
		String str;
		while ((str = reader.readLine()) != null) {
			buffer.append(str).append("\n");
		}
		is.close();
		
		return buffer.toString();
	}
}
