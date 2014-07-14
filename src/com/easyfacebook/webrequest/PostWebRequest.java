package com.easyfacebook.webrequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class PostWebRequest {

	public static interface Callback{
		void requestStart();
		void requestComplete(BufferedReader data);
		void requestFail(Exception e);
	};
	
	private Callback mCallback = null;
	private String mUrl = null;
	private String mUrlParams;
	
	public PostWebRequest(Callback callback) {mCallback = callback;}
	
	public void executeRequest(String url, String urlParams){
		mUrl = url;
		mUrlParams = urlParams;
		new LocalAsyncTask().execute();
	}
	
	
	private class LocalAsyncTask extends AsyncTask<Void, Void, BufferedReader>{

		@Override
		protected void onPreExecute() {	
			super.onPreExecute();
			mCallback.requestStart();
		}

		@Override
		protected BufferedReader doInBackground(Void... params) {
			
			try {
				URL url = new URL(mUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
				dataOutputStream.writeBytes(mUrlParams);
				dataOutputStream.flush();
				dataOutputStream.close();

				
				
				return new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
			} catch (MalformedURLException e) {
				mCallback.requestFail(e);
			} catch (IOException e) {
				mCallback.requestFail(e);
			}
			
			
			return null;
		}


		@Override
		protected void onPostExecute(BufferedReader result) {
			super.onPostExecute(result);
			mCallback.requestComplete(result);
		}
		
		
	}
}
