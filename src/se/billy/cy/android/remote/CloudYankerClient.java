package se.billy.cy.android.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CloudYankerClient {

	private static final String TAG = "CloudYankerClient";
	private final String url;
	private final String userName;
	private HttpClient httpClient;

	public CloudYankerClient(String url, String userName) {
		this.url = url;
		this.userName = userName;
		this.httpClient = new DefaultHttpClient();
	}

	public void postText(String data) throws IOException {
		try {
			HttpPost httpPost = new HttpPost(encodeUrl());

			httpPost.setEntity(new StringEntity(data, "UTF-8"));
			httpClient.execute(httpPost);
		
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new IOException(e);
		}
	}
	
	public String getText() throws IOException{
		HttpGet httpGet = new HttpGet(encodeUrl());
		HttpResponse httpResponse = httpClient.execute(httpGet);
				
		InputStream in = httpResponse.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    while ((line = reader.readLine()) != null)
	    {
	        sb.append(line + "\n");
	    }
	    
	    return sb.toString();
	}

	private String encodeUrl() throws UnsupportedEncodingException {
		return url + "/" + URLEncoder.encode(userName, "UTF-8");
	}

}
