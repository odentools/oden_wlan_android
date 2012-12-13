package info.ohgita.oden_wlanloo;

import info.ohgita.oden_wlanloo.olan.Olan;
import info.ohgita.oden_wlanloo.olan.Tnet;

import java.util.HashMap;
import android.content.Context;

public class Olans {
	public HashMap<String, Olan> Objects;
	public Olans(Context context) {
		Objects = new HashMap<String, Olan>();
		Objects.put("Tnet", new Tnet(context));
	};
}
