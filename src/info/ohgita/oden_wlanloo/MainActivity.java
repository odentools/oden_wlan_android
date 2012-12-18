package info.ohgita.oden_wlanloo;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {
	private static final int MENU_ID_ADDPROFILE = 100;
	private static final int MENU_ID_PREF = 110;
	private static final int MENU_ID_APPINFO = 120;
	
	private static final int REQUEST_CODE_ADDPROFILE = 1000;

	ProfileListAdapter adp;
	List<ProfileItem> dataList;
	ListView profileListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_main);
		TextView footer = (TextView) findViewById(R.id.main_textView_footer);
		footer.setText(R.string.main_footer_text_wifidisable);

		/* Load profiles */
		dataList = new ArrayList<ProfileItem>();

		adp = new ProfileListAdapter(this, R.layout.list_profilelist, dataList);

		final ListView lv = (ListView) this.findViewById(R.id.main_listView_profiles);
		lv.setScrollingCacheEnabled(false);
		lv.setAdapter(adp);
		profileListView = lv;
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	final ProfileItem prof = (ProfileItem) profileListView.getItemAtPosition(position);
            	prof.
            	profileListLoad();
            }
        });
       profileListLoad();
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
			this.startActivityForResult(i_addprofile, REQUEST_CODE_ADDPROFILE);
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
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == REQUEST_CODE_ADDPROFILE){
    		/* Return from Add profile */
    		if(resultCode == RESULT_OK){
    			profileListLoad();
    		}
    	}
    }

	public void profileListLoad() {
		Profiles profs = new Profiles(getApplicationContext());

		ArrayList<ProfileItem> items = profs.returnDBItems();

		dataList.clear();

		for (int i = 0; i < items.size(); i++) {
			dataList.add((ProfileItem) items.get(i));
		}

		adp.notifyDataSetChanged();
	}

	public class ProfileListAdapter extends ArrayAdapter<ProfileItem> {
		private List<ProfileItem> items;
		private LayoutInflater inflater;

		public ProfileListAdapter(Context context, int resourceId,
				List<ProfileItem> items) {
			super(context, resourceId, items);
			this.items = items;
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = inflater.inflate(R.layout.list_profilelist, null);
			}
			TextView ProfileClassName = (TextView) v.findViewById(R.id.textView_ProfileClassName);
			ProfileClassName.setText(items.get(position).profileClassName);
			TextView ProfileLoginId = (TextView) v	.findViewById(R.id.textView_ProfileLoginId);
			ProfileLoginId.setText(items.get(position).loginId);
			TextView ProfileStatus = (TextView) v.findViewById(R.id.textView_ProfileStatus);
			ProfileStatus.setText("オフライン");
			return v;
		}
	}
}
