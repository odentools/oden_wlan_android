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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_profile_editor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Spinner ap = (Spinner) findViewById(R.id.spinner_profileeditor_ap);
		ap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,View view, int position,long id) {
				Spinner ap = (Spinner) parent; 
				Toast.makeText(view.getContext(), ap.getSelectedItem().toString(), Toast.LENGTH_SHORT);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}

		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Olans olans = new Olans(getApplicationContext());
		
		Iterator<Entry<String, Olan>> ite = olans.Objects.entrySet().iterator();
		while(ite.hasNext()){
			Olan vlan = ite.next().getValue();
			//adapter.add(vlan.NAME.toString());
		}
		ap.setAdapter(adapter);
		ap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                Toast.makeText(Activity_profileEditor.this, item, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
		
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
		Tnet tnet = new Tnet(this.getApplicationContext());//DEBUG
		HashMap<String,String> param = new HashMap<String, String>();
		param.put("aaa", "123");
		param.put("bbb", "456");
		tnet.isLogin();
		String text = new String();
		try {
			tnet.post("https://s1.mpnets.net", param, new Tnet.HTTPConnector_callback(){
				@Override
				public void callback(String resultText) {
					Toast.makeText(getApplicationContext(), resultText, Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
			Log.e("owlan", e.toString(), e);
		}
		Log.i("owlan", text);
		
		Spinner spinner_ap = (Spinner) findViewById(R.id.spinner_profileeditor_ap);
		EditText edittext_loginid = (EditText) findViewById(R.id.edittext_profileeditor_loginid);
		EditText edittext_loginpw = (EditText) findViewById(R.id.edittext_profileeditor_loginpw);
		
		if(spinner_ap.getSelectedItem() == null){
			//(spinner_ap).setError(getResources().getString(R.string.profileeditor_spinner_ap_error));
			return false;
		}
		
		if(edittext_loginid.getText() == null){
			edittext_loginid.setError(getResources().getString(R.string.profileeditor_edittext_loginid_error));
			return false;
		} else if(edittext_loginpw.getText() == null){
			edittext_loginpw.setError(getResources().getString(R.string.profileeditor_edittext_loginpw_error));
			return false;
		}

		String ap = spinner_ap.getSelectedItem().toString();
		String loginid = edittext_loginid.getText().toString();
		String loginpw = edittext_loginpw.getText().toString();
		return true;
	}
}
