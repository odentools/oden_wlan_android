package info.ohgita.oden_wlanloo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import info.ohgita.oden_wlanloo.olan.Olan;
import info.ohgita.oden_wlanloo.olan.Tnet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class Activity_profileEditor extends SherlockActivity {
	private static final int MENU_ID_SAVEPROFILE = 100;
	
	private int editDBItemId = -1;
	private Olans olans;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_profile_editor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		olans = new Olans(getApplicationContext());

		Spinner ap = (Spinner) findViewById(R.id.spinner_profileeditor_ap);
		ap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,View view, int position,long id) {
				String selectedName = parent.getSelectedItem().toString();
				Iterator<Entry<String, Olan>> ite = olans.Objects.entrySet().iterator();
				while(ite.hasNext()){
					Olan vlan = (ite.next().getValue());
					if(vlan.returnName().contentEquals(selectedName)){
						EditText et_id = (EditText) findViewById(R.id.edittext_profileeditor_loginid);
						EditText et_pw = (EditText) findViewById(R.id.edittext_profileeditor_loginpw);
						et_id.setHint(vlan.returnLoginIdHint());
						et_pw.setHint(vlan.returnLoginPwHint());
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}

		});
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ap.setAdapter(adapter);
		
		Iterator<Entry<String, Olan>> ite = olans.Objects.entrySet().iterator();
		while(ite.hasNext()){
			Olan vlan = (ite.next().getValue());
			adapter.add(vlan.returnName());
		}
		
		Intent intent = getIntent();
		if (intent.getSerializableExtra("editProfileId") != null) {
			// Profile edit mode
			this.setTitle(R.string.activity_profileeditor_name_edit);
		} else {
			// Profile add mode
			this.setTitle(R.string.activity_profileeditor_name_add);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);

		/* Save button */
		menu.add(Menu.NONE, MENU_ID_SAVEPROFILE, Menu.NONE, R.string.profileeditor_menu_ok)
		.setIcon(R.drawable.ic_action_send)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case MENU_ID_SAVEPROFILE:
				saveProfile();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Save a profile
	 */
	protected boolean saveProfile() {
		boolean isError = false;
		Spinner spinner_ap = (Spinner) findViewById(R.id.spinner_profileeditor_ap);
		EditText edittext_loginid = (EditText) findViewById(R.id.edittext_profileeditor_loginid);
		EditText edittext_loginpw = (EditText) findViewById(R.id.edittext_profileeditor_loginpw);
		
		if(spinner_ap.getSelectedItem() == null){
			//(spinner_ap).setError(getResources().getString(R.string.profileeditor_spinner_ap_error));
			isError = true;
		}
		
		if(edittext_loginid.getText() == null || edittext_loginid.getText().length() <= 0){
			edittext_loginid.setError(getResources().getString(R.string.profileeditor_edittext_loginid_error));
			isError = true;
		}
		if(edittext_loginpw.getText() == null || edittext_loginpw.getText().length() <= 0){
			edittext_loginpw.setError(getResources().getString(R.string.profileeditor_edittext_loginpw_error));
			isError = true;
		}

		if(isError){
			return false;
		}
		
		String ap = spinner_ap.getSelectedItem().toString();
		String loginid = edittext_loginid.getText().toString();
		String loginpw = edittext_loginpw.getText().toString();
		
		Profiles profs = new Profiles(getApplicationContext());
		if(editDBItemId == -1){
			// New item
			ProfileItem prof = new ProfileItem(getApplicationContext(),-1);
			prof.setClassName(ap);
			prof.setLoginId(loginid);
			prof.setLoginPw(loginpw);
			prof.save();
		}

		setResult(RESULT_OK);
		finish();
		return true;
	}
}
