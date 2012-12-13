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

	public Tnet(Context c){
		super(c);
		context = c;
	}
	
	@Override
	public void connect() {

	}

	@Override
	public void login() {
		
	}

	@Override
	public void logout() {

	}

	@Override
	public boolean isLogin() {

		return false;
	}

	@Override
	public boolean isConnect(WifiInfo current_wifi) {
		if (current_wifi.getSSID().contentEquals("Tnet")) {
			return true;
		}
		return false;
	}
}
