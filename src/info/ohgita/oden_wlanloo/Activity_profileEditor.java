package info.ohgita.oden_wlanloo;

import android.os.Bundle;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class Activity_profileEditor extends SherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
		switch(item.getItemId()) {  
			case android.R.id.home:  
				finish();  
				return true;  
		}
		return super.onOptionsItemSelected(item);  
	}  
}
