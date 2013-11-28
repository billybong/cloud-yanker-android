package se.billy.cy.android;

import java.io.IOException;

import se.billy.cy.android.remote.CloudYankerClient;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PastedTextFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v =  inflater.inflate(R.layout.pasted_text_fragment, container, true);
		
		Button b = (Button) v.findViewById(R.id.pasteButton);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pasteText(view);
			}
		});
        
        return v;
	}
	
	public void pasteText(View view){
		new PasteFromCloudTask().execute();		
	}
	
	//Callback when async task is done
	private void updatePastedText(String newText){
		TextView pastedTextView = (TextView) this.getView().findViewById(R.id.pastedText);
		pastedTextView.setText(newText);
	}
	
	private class PasteFromCloudTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String url = preferences.getString(SettingsActivity.SERVER_URL, getString(R.string.pref_default_server_url));
			String userName = preferences.getString(SettingsActivity.USER_NAME, getString(R.string.pref_default_name));
			
			try {
				return new CloudYankerClient(url, userName).getText();
			} catch (IOException e) {
				Toast.makeText(getActivity(), "Could not fetch data due to: " + e.getMessage(), Toast.LENGTH_LONG).show();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			updatePastedText(result);
		}
		
		
	}
}
