package com.easyfacebook.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.easyfacebook.custom.views.EasyWebView;
import com.easysocial.R;

public class EasyOauthActivity extends Activity{

	private EasyWebView mWebView = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easy_oauth);
		Intent intent = getIntent();
		if( intent == null){
			Toast.makeText(this, "oauth url is null", Toast.LENGTH_LONG).show();
			return;
		}
		
		mWebView = (EasyWebView) findViewById(R.id.EasyOauth_web_view);
		mWebView.setCallbackUrl(intent.getStringExtra("callbackurl"));
		mWebView.setAccessTokenUrl(intent.getStringExtra("access_token_url"));
		mWebView.loadUrl(intent.getStringExtra("url"));
	}
	
}
