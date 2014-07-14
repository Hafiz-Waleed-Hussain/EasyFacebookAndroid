package com.easyfacebook.algos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.easyfacebook.EasyFacebook;
import com.easyfacebook.interfaces.FacebookAlgos;
import com.easyfacebook.util.Logger;
import com.easyfacebook.webrequest.GetWebRequest;
import com.easyfacebook.webrequest.GetWebRequest.Callback;

public class GetUserImageAlgo implements FacebookAlgos{

	private callback getUserInfoCallback = null;


	private boolean mAsBitmap = false;
	private String mType = EasyFacebook.TypeNormal;
	private int mWidth = 200;
	private int mHeight = 200;
	
	
	public static interface callback{
		void start();
		void fail(Object data);
		void complete(String imageUrl);
		void complete(Bitmap image);
	}

	public GetUserImageAlgo(boolean asBitmap, String type, int width, int height) {
		mAsBitmap = asBitmap;
		mType = type;
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void execute(Object callback) {

		getUserInfoCallback = (com.easyfacebook.algos.GetUserImageAlgo.callback) callback;

		String asImage = mAsBitmap ? "1": "0";
				
		String url = EasyFacebook.FACEBOOK_REQUEST_URL+"me/picture?redirect="+asImage+"&type="+mType+"&width="+mWidth+"&height="+mHeight+"&";
		url = EasyFacebook.appendAccessToken(url);		
		Logger.showLog(url);

		GetWebRequest getWebRequest = new GetWebRequest(getWebRequestCallback);
		getWebRequest.executeRequest(url);

	}

	private GetWebRequest.Callback getWebRequestCallback = new Callback() {

		@Override public void requestStart() {getUserInfoCallback.start();}
		@Override public void requestFail(Exception e) {getUserInfoCallback.fail(e);}
		@Override
		public void requestComplete(final InputStream is) {
			if(mAsBitmap){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						getUserInfoCallback.complete(bitmap);
						
					}
				}).start();
			}else{
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				try {
					JSONObject jsonObject = new JSONObject(br.readLine());
					String imageUrl = jsonObject.getJSONObject("data").getString("url");
					getUserInfoCallback.complete(imageUrl);
					
				} catch (IOException e) {
					e.printStackTrace();
					getUserInfoCallback.fail(e);
				} catch (JSONException e) {
					getUserInfoCallback.fail(e);
					e.printStackTrace();
				}
				
			}
			
		}
	};

}
