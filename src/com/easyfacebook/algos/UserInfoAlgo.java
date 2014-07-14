package com.easyfacebook.algos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.easyfacebook.EasyFacebook;
import com.easyfacebook.interfaces.FacebookAlgos;
import com.easyfacebook.webrequest.GetWebRequest;

public class UserInfoAlgo implements FacebookAlgos{

	public static interface callback{
		void start();
		void fail(Object data);
		void complete(JSONObject userData);
	}

	@Override
	public void execute(Object callback) {

		final UserInfoAlgo.callback callbacks = (com.easyfacebook.algos.UserInfoAlgo.callback) callback;

		String url = EasyFacebook.FACEBOOK_REQUEST_URL+"me?";
		url = EasyFacebook.appendAccessToken(url);
		GetWebRequest userInfoGetWebRequest = new GetWebRequest(new GetWebRequest.Callback() {

			@Override public void requestStart() {callbacks.start();}
			@Override public void requestFail(Exception e) {callbacks.fail(e.getMessage());}
			@Override
			public void requestComplete(InputStream in) {
				
				BufferedReader data = new BufferedReader(new InputStreamReader(in));
				String line;
				try {
					line = data.readLine();
					JSONObject jsonObject = new JSONObject(line);
					callbacks.complete(jsonObject);
				} catch (IOException e) {
					callbacks.fail(e.getMessage());
					e.printStackTrace();
				} catch (JSONException e) {
					callbacks.fail(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		userInfoGetWebRequest.executeRequest(url);


	}


}
