package se.billy.cy.android;

import java.io.IOException;

import se.billy.cy.android.remote.CloudYankerClient;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
    	switch(item.getItemId()){
          case R.id.action_settings:
              openSettings();
              return true;
          default:
              return super.onOptionsItemSelected(item);
      }
	}

	private void openSettings() {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}
	
	public void copyText(View view){
		EditText copiedTextView = (EditText) findViewById(R.id.copyText);
		String copiedText = copiedTextView.getText().toString();
		
		new CopyTextToCloudTask().execute(copiedText);
		
		//Update the fragment with the newest copied text
		TextView pastedTextView = (TextView) findViewById(R.id.pastedText);
		pastedTextView.setText(copiedText);
	}
	
	private class CopyTextToCloudTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			
			String url = preferences.getString(SettingsActivity.SERVER_URL, getString(R.string.pref_default_server_url));
			String userName = preferences.getString(SettingsActivity.USER_NAME, getString(R.string.pref_default_name));
			
			try {
				new CloudYankerClient(url, userName).postText(arg0[0]);
			} catch (IOException e) {
				Toast.makeText(getParent(), "Unable to call backend due to: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			return null;
		}
		
	}
}
