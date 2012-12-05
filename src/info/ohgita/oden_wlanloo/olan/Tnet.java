package info.ohgita.oden_wlanloo.olan;

import android.net.wifi.WifiInfo;

public class Tnet extends Olan {

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
		if(current_wifi.getSSID().contentEquals("Tnet")){
			return true;
		}
		return false;
	}

}
