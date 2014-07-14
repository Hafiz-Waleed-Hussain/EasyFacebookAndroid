package com.easyfacebook.algos;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.easyfacebook.EasyFacebook;
import com.easyfacebook.interfaces.FacebookAlgos;
import com.easyfacebook.util.FacebookPreferenceUtil;
import com.easyfacebook.util.Logger;
import com.easyfacebook.webrequest.PostWebRequest;

public class PostAlgo implements FacebookAlgos{

	private String message = null;
	public void setMessage(String message) {
		this.message = message;
	}
	
	public static interface callback{
		void start();
		void fail(Object data);
		void complete(String id);
	}

	private callback nullHandler = new callback() {		
		@Override public void start() {}
		@Override public void fail(Object data) {}	
		@Override public void complete(String id) {}
	};

	@Override
	public void execute(Object callback) {
		
		final PostAlgo.callback localCallback = (PostAlgo.callback) (callback == null ? nullHandler : callback);
		JSONObject useJsonObject = FacebookPreferenceUtil.getUserInfo();
		String userId = useJsonObject.optString("id");
		String url = EasyFacebook.FACEBOOK_REQUEST_URL+userId+"/feed?";
		url = EasyFacebook.appendAccessToken(url);
		
		String urlParams = "message="+message;
		Logger.showLog(url);
		
		PostWebRequest getWebRequest = new PostWebRequest(new PostWebRequest.Callback() {
			
			@Override
			public void requestStart() {
				localCallback.start();
				
			}
			
			@Override
			public void requestFail(Exception e) {
				localCallback.fail(e.getMessage());
				
			}
			
			@Override
			public void requestComplete(BufferedReader data) {
				try {
					localCallback.complete(data.readLine());
				} catch (IOException e) {
					localCallback.fail(e.getMessage());
					e.printStackTrace();
				}				
			}
		});				
		getWebRequest.executeRequest(url,urlParams);
	}

}
