package com.easyfacebook.webrequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

public class GetWebRequest {

	public static interface Callback{
		void requestStart();
		void requestComplete(InputStream is);
		void requestFail(Exception e);
	};
	
	private Callback mCallback = null;
	private String mUrl = null;
	
	public GetWebRequest(Callback callback) {mCallback = callback;}
	
	public void executeRequest(String url){
		mUrl = url;
		new LocalAsyncTask().execute();
	}
	
	
	private class LocalAsyncTask extends AsyncTask<Void, Void, InputStream>{

		@Override
		protected void onPreExecute() {	
			super.onPreExecute();
			mCallback.requestStart();
		}

		@Override
		protected InputStream doInBackground(Void... params) {
			
			try {
				URL url = new URL(mUrl);
				URLConnection connection = url.openConnection();
				return connection.getInputStream();
						
			} catch (MalformedURLException e) {
				mCallback.requestFail(e);
			} catch (IOException e) {
				mCallback.requestFail(e);
			}
			
			
			return null;
		}


		@Override
		protected void onPostExecute(InputStream result) {
			super.onPostExecute(result);
			mCallback.requestComplete(result);
		}
		
		
	}
}
