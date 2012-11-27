package info.ohgita.oden_wlanloo;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends SherlockActivity {
	private static final int MENU_ID_ADDPROFILE = 100;
	private static final int MENU_ID_PREF = 110;
	private static final int MENU_ID_ABOUT = 120;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        
        /* SubMenu button */
		SubMenu sub_menu = menu.addSubMenu("Overflow Item");
		sub_menu.getItem().setIcon(R.drawable.ic_action_overflow);
		sub_menu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		/* SubMenu > Add-profile button */
		sub_menu.add(Menu.NONE, MENU_ID_ADDPROFILE, Menu.NONE, R.string.general_menu_addprofile);
		//.setIcon(R.drawable.ic_content_new);

		/* SubMenu > Preference button */
		sub_menu.add(Menu.NONE, MENU_ID_PREF, Menu.NONE, R.string.general_menu_pref);
		
		/* SubMenu > About button */
		sub_menu.add(Menu.NONE, MENU_ID_ABOUT, Menu.NONE, R.string.general_menu_about);
		
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
		    //startActivity(new Intent(this, .class));
		    break;
		case MENU_ID_PREF:
			
		}
		return ret;
    }
}
