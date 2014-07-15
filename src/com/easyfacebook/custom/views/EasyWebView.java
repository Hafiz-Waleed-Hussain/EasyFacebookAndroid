package com.easyfacebook.custom.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.easyfacebook.EasyFacebook;
import com.easyfacebook.algos.UserInfoAlgo;
import com.easyfacebook.algos.UserInfoAlgo.callback;
import com.easyfacebook.util.DialogBuilder;
import com.easyfacebook.util.FacebookPreferenceUtil;
import com.easyfacebook.util.Logger;

public class EasyWebView extends WebView{

	private Dialog mDialog = null;
	private Activity mActivityReference = null;
	private String mCallbackUrl = null;
	private String mAccessToken = null;

	public EasyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public EasyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public EasyWebView(Context context) {
		super(context);
		init(context);
	}

	public void setCallbackUrl(String callbackUrl) { mCallbackUrl = callbackUrl;}
	public void setAccessTokenUrl(String accessTokenUrl) { mAccessToken = accessTokenUrl;}


	private void init(Context context) {
		mActivityReference = (Activity) context;
		mDialog = DialogBuilder.createDialog(context);
		setWebViewClient(new EasyWebViewClient());
	}

	private void showDialog(){mDialog.show();}
	private void hideDialog(){mDialog.dismiss();}
	private void showWebView(){setVisibility(WebView.VISIBLE);}
	private void hideWebView(){setVisibility(WebView.GONE);}


	class EasyWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Uri uri = Uri.parse(url);
			Uri callBackUri = Uri.parse(mCallbackUrl);
			if( uri.getHost().equals(callBackUri.getHost())){
				Logger.showLog(url);
				String code = uri.getQueryParameter("code");
				GetAccessTokenRequest accessTokenRequest = new GetAccessTokenRequest();
				accessTokenRequest.execute(mAccessToken+code);
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			showDialog();
			hideWebView();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			showWebView();
			hideDialog();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Intent errorData = new Intent();
			errorData.putExtra(EasyFacebook.ERROR_CODE, errorCode);
			mActivityReference.setResult(Activity.RESULT_CANCELED,errorData);
			mActivityReference.finish();
		}


	}


	private class GetAccessTokenRequest extends AsyncTask<String, Void, BufferedReader>{
		@Override protected void onPreExecute() {super.onPreExecute();showDialog();}
		@Override protected BufferedReader doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				URLConnection connection = url.openConnection();
				return new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (MalformedURLException e) {hideDialog();
			} catch (IOException e) {hideDialog();}
			return null;
		}

		@Override protected void onPostExecute(BufferedReader result) {
			super.onPostExecute(result);
			String line;
			try {
				line = result.readLine();
				Logger.showLog(line);
				String url = "http://www.uwanttolearn.com?"+line;
				Uri u = Uri.parse(url);
				FacebookPreferenceUtil.setAccesToken(u.getQueryParameter("access_token"));
				Logger.showLog(FacebookPreferenceUtil.getAccessToken());
				UserInfoAlgo userInfoAlgo = new UserInfoAlgo();
				userInfoAlgo.execute(userInfoCallback);
			} catch (IOException e) {
				e.printStackTrace();
				hideDialog();
				mActivityReference.setResult(Activity.RESULT_CANCELED);
				mActivityReference.finish();
			}
		}
		
		UserInfoAlgo.callback userInfoCallback = new callback() {
			
			@Override public void start() {}
			@Override public void fail(Object data) {hideDialog();mActivityReference.finish();}			
			@Override public void complete(JSONObject userData) {
				FacebookPreferenceUtil.setUserInfo(userData.toString());
				hideDialog();
				mActivityReference.setResult(Activity.RESULT_OK);
				mActivityReference.finish();						
				
			}
		};
	}
}
