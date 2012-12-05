package info.ohgita.oden_wlanloo.olan;

import android.net.wifi.WifiInfo;

/**
 * @class Olan
 * OECU-LAN環境定義ベースクラス
 */

public abstract class Olan {
	static String NAME;

	abstract void connect();

	abstract void login();

	abstract void logout();

	abstract boolean isLogin();

	abstract boolean isConnect(WifiInfo currentWifiInfo);
}
