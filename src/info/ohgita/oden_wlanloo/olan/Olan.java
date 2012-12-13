package info.ohgita.oden_wlanloo.olan;

/**
 * OECU-LAN環境定義ベースクラス
 * @class info.ohgita.oden_wlanloo.olan.Olan
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;

public abstract class Olan {
	public String NAME;
	
	protected Context context;
	
	public Olan (Context c) {
		context = c;
	}

	abstract public void connect();

	abstract public void login();

	abstract public void logout();

	abstract public boolean isLogin();

	abstract public boolean isConnect(WifiInfo currentWifiInfo);
	
	/**
	 * POST Request method (Asynchronous)
	 * @param url URL
	 * @param params POST Parameters
	 * @param callback Callback method (HTTPConnector_callback)
	 */
	public void post(String url, HashMap<String, String> params, HTTPConnector_callback callback){
		HTTPConnector task = new HTTPConnector(context, callback, -1);
		try{
			task.execute("post", url, params);
		}catch(Exception e){
			callback.callback(e.toString());
		}
	}
	
	/**
	 * POST Request method (synchronous)
	 * @param url URL
	 * @param params POST Parameters
	 * @return Response body
	 */
	public String post_sync(String url, HashMap<String, String> params) throws Exception{
		HTTPConnector task = new HTTPConnector(context, null, -1);
		return task.get(new URL(url));
	}
	
	/**
	 * GET Request method (Asynchronous)
	 * @param url URL
	 * @param callback Callback method (HTTPConnector_callback)
	 */
	public void get(String url, HTTPConnector_callback callback){
		HTTPConnector task = new HTTPConnector(context, callback, -1);
		try{
			task.execute("get", url);
		}catch(Exception e){
			callback.callback(e.toString());
		}
	}
	
	/**
	 * GET Request method (synchronous)
	 * @param URL
	 * @return Response body
	 */
	public String get_sync(String url) throws Exception{
		HTTPConnector task = new HTTPConnector(context, null, -1);
		return task.get(new URL(url));
	}
	
	
	/*------------------------------------------------------------------------------  */
	
	/**
	 * 
	 * HTTPS connection inner-class 
	 * @class info.ohgita.oden_wlanloo.olan.Olan.HTTPConnector
	 * @extends AsyncTask
	 * 
	 */
	protected class HTTPConnector extends AsyncTask<Object, Void, String> {
		private Context context;
		private boolean isDisable_VerfySSL_HostName;
		protected HTTPConnector_callback callback;
		protected int verfySSL_CertStore_ResourceId;
		
		/**
		 * 
		 * @param con Context
		 * @param calb Callback method (HTTPConnector_callback)
		 * @param cert_store_resid 
		 * @param is_disable_verfy_ssl_hostname
		 */
		public HTTPConnector(Context con, Olan.HTTPConnector_callback calb, int cert_store_resid, boolean is_disable_verfy_ssl_hostname){
			context = con;
			callback = calb;
			verfySSL_CertStore_ResourceId = cert_store_resid;
			isDisable_VerfySSL_HostName = is_disable_verfy_ssl_hostname;
		}
		
		public HTTPConnector(Context con, HTTPConnector_callback calb, int cert_store_resid){
			this(con, calb, cert_store_resid, false);
		}
		
		/* Background process */
		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(Object... params) {
			String req_type = (String) params[0];
			
			URL url;
			try {
				url = new URL((String) params[1]);
			} catch (MalformedURLException e1) {
				return e1.toString();
			}
			
			HashMap<String, String> body_params = null;
			if(req_type.contentEquals("post")){
				body_params = (HashMap<String, String>) params[2];
			}
			
			try {
				if(req_type.contentEquals("post")){
					return post(url, body_params);
				}else if(req_type.contentEquals("get")){
					return get(url);
				}
			} catch (Exception e) {
				return e.toString();
			}
			return null;
		}
		
		/* Callback caller */
		@Override  
	    protected void onPostExecute(String doInBackground_result) {
			callback.callback(doInBackground_result);
	    }
		
		/**
		 * HTTPS POST Request inner-method
		 * @param url_str
		 * @param body_params
		 * @return Response body
		 * @throws Exception
		 */
		public String post(URL url, HashMap<String, String> body_params) throws Exception {
			HttpURLConnection con = newHTTPSConObj(url);
			
			/* Make a POST request */
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			//con.setChunkedStreamingMode(0);
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
		
		/**
		 * HTTPS GET Request inner-method
		 * @param url_str
		 * @return Response body
		 * @throws Exception
		 */
		public String get(URL url) throws Exception {
			HttpURLConnection con = newHTTPSConObj(url);
			
			/* Make a GET request */
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			
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
		
		protected HttpsURLConnection newHTTPSConObj(URL url) throws Exception{
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			
			/* Setting for SSL */
			if(verfySSL_CertStore_ResourceId != -1){
				InputStream ks_is = this.context.getResources().openRawResource(verfySSL_CertStore_ResourceId);
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
			}
			
			if(isDisable_VerfySSL_HostName == true){
				con.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			} else {
				con.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			}
			
			return con; 
		}
		
		protected HttpURLConnection newHTTPConObj(URL url) throws Exception{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			return con; 
		}
	}
	
	/**
	 *  Interface for callback
	 */
	public interface HTTPConnector_callback {
		void callback(String resultText);
	}
}
