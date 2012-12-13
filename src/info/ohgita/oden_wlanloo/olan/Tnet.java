package info.ohgita.oden_wlanloo.olan;

/**
 * OECU-LAN環境定義クラス	-	Tnet
 * @class info.ohgita.oden_wlanloo.olan.Tnet
 */

import java.net.URL;
import java.util.HashMap;

import com.actionbarsherlock.R;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.util.Log;

public class Tnet extends Olan {
	static protected String NAME = "Tnet";
	static protected String LOGIN_ID_HINT = "(例) ht12a000";
	static protected String LOGIN_PW_HINT = "Tnetのパスワード";

	public Tnet(Context c){
		super(c);
		super.NAME = NAME;
		super.context = c;
		super.LOGIN_ID_HINT = LOGIN_ID_HINT;
		super.LOGIN_PW_HINT = LOGIN_PW_HINT;
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
		try {
			get_sync("https://mpnet.sakura.ne.jp/services/test/");
		} catch (Exception e) {
			Log.e("owlan", e.toString(), e);
		}
		return false;
	}

	@Override
	public boolean isConnect(WifiInfo current_wifi) {
		if (current_wifi.getSSID().contentEquals("Tnet")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void post(String url, HashMap<String, String> params,  HTTPConnector_callback callback){
		HTTPConnector task = new HTTPConnector(context, callback, R.raw.tnet_alcatel_webview_bin3);  
		task.execute("post", url, params);
	}
	
	@Override
	public String post_sync(String url, HashMap<String, String> params) throws Exception{
		HTTPConnector task = new HTTPConnector(context, null, R.raw.tnet_alcatel_webview_bin3);  
		return task.post(new URL(url), params);
	}
	
	@Override
	public void get(String url, HTTPConnector_callback callback){
		HTTPConnector task = new HTTPConnector(context, callback, R.raw.tnet_alcatel_webview_bin3);  
		task.execute("get", url);
	}
	
	@Override
	public String get_sync(String url) throws Exception{
		HTTPConnector task = new HTTPConnector(context, null, R.raw.tnet_alcatel_webview_bin3);  
		return task.get(new URL(url));
	}
}
