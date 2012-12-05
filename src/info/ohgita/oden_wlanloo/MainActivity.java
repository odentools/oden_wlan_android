package info.ohgita.oden_wlanloo;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends SherlockActivity {
	private static final int MENU_ID_ADDPROFILE = 100;
	private static final int MENU_ID_PREF = 110;
	private static final int MENU_ID_APPINFO = 120;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_main);
		TextView footer = (TextView)findViewById(R.id.main_textView_footer);
		footer.setText("aaa");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);

		/* Add-profile button */
		menu.add(Menu.NONE, MENU_ID_ADDPROFILE, Menu.NONE,
				R.string.general_menu_addprofile)
				.setIcon(R.drawable.ic_content_new)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		/* SubMenu button */
		SubMenu sub_menu = menu.addSubMenu(R.string.general_menu_overflow);
		sub_menu.getItem().setIcon(R.drawable.ic_action_overflow);
		sub_menu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		/* SubMenu > Preference button */
		sub_menu.add(Menu.NONE, MENU_ID_PREF, Menu.NONE,
				R.string.general_menu_pref);

		/* SubMenu > About button */
		sub_menu.add(Menu.NONE, MENU_ID_APPINFO, Menu.NONE,
				R.string.general_menu_about);

		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		case MENU_ID_ADDPROFILE:
			ret = false;
			Intent i_addprofile = new Intent(this, Activity_profileEditor.class);
			i_addprofile.putExtra("MODE", "ADD");
			startActivity(i_addprofile);
			break;
		case MENU_ID_PREF:
			ret = false;
			startActivity(new Intent(this, Activity_preference.class));
			break;
		case MENU_ID_APPINFO:
			ret = false;
			startActivity(new Intent(this, Activity_appInfo.class));
			break;
		}
		return ret;
	}
}
