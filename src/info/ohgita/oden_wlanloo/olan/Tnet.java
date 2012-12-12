package info.ohgita.oden_wlanloo.olan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
		URLConnection connection;
		StringBuffer buffer = new StringBuffer();
		try {
			connection = new URL("https://10.0.0.253/").openConnection();
			HttpsURLConnection con = (HttpsURLConnection) connection;
			
			InputStreamReader is = new InputStreamReader(con.getInputStream());
			BufferedReader reader = new BufferedReader(is);
			String str;
			while ((str = reader.readLine()) != null){
				buffer.append(str).append("\n");
			}
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	void post(String url) throws MalformedURLException, IOException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			KeyManagementException {
		URLConnection connection = new URL(url).openConnection();
		HttpsURLConnection con = (HttpsURLConnection) connection;

		InputStream is = this.context.getResources().openRawResource(
				R.raw.tnet_alcatel_webview_bin);
		KeyStore ks = KeyStore.getInstance("BKS");
		ks.load(is, "ODENWLANLOO_CERT_STOREPASS".toCharArray());
		is.close();

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

		StringBuffer buffer = new StringBuffer();
		InputStreamReader is_ = new InputStreamReader(con.getInputStream());
		BufferedReader reader = new BufferedReader(is_);
		String str;
		while ((str = reader.readLine()) != null) {
			buffer.append(str).append("\n");
		}
		is.close();

	}
}
