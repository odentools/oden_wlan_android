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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import com.actionbarsherlock.R;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;

/**
 * @class Olan
 * OECU-LAN環境定義ベースクラス
 */

public abstract class Olan {
	static String NAME;
	
	protected Context context;
	
	public Olan (Context c) {
		context = c;
	}

	abstract public void connect();

	abstract public void login();

	abstract public void logout();

	abstract public boolean isLogin();

	abstract public boolean isConnect(WifiInfo currentWifiInfo);
	
	public String post(String url, HashMap<String, String> params, HTTPConnector_callback callback){
		HTTPConnector task = new HTTPConnector(context, callback);  
		task.execute(url, params);
		return "";
	}
	
	/**
	 * HTTPConnector class
	 */
	protected class HTTPConnector extends AsyncTask<Object, Void, String> {
		private Context context;
		protected HTTPConnector_callback callback;
		
		public HTTPConnector(Context con, HTTPConnector_callback calb){
			context = con;
			callback = calb;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(Object... params) {
			String url = (String) params[0];
			HashMap<String, String> body_params = (HashMap<String, String>) params[1];
			
			try {
				return post(url, body_params);
			} catch (Exception e) {
				return null;
			}
		}
		
		public String post(String url, HashMap<String, String> body_params) throws Exception {
			URLConnection connection = new URL(url).openConnection();
			HttpsURLConnection con = (HttpsURLConnection) connection;
			
			/* Setting for SSL */
			
			InputStream ks_is = this.context.getResources().openRawResource(R.raw.tnet_alcatel_webview_bin3);
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
			Iterator<String> itr = body_params.keySet().iterator();
			while(itr.hasNext()){
				String key = itr.next();
				String value = body_params.get(key);
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
		
		@Override  
	    protected void onPostExecute(String doInBackground_result) {
			callback.callback(doInBackground_result);
	    }
	}
	
	public interface HTTPConnector_callback {
		void callback(String resultText);
	}
}
