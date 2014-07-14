package com.easyfacebook;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.easyfacebook.activities.EasyOauthActivity;
import com.easyfacebook.algos.GetUserImageAlgo;
import com.easyfacebook.algos.PostAlgo;
import com.easyfacebook.models.CredentialModel;
import com.easyfacebook.util.FacebookPreferenceUtil;

public class EasyFacebook{

	//	private String redirectUrl = "https://www.facebook.com/connect/login_success.html";

	private static EasyFacebook mEasySocialFacebook = null;

	public static Context mContext = null;

	public static EasyFacebook getInstance(Context context, CredentialModel credentialModel){

		if( mEasySocialFacebook == null)
			mEasySocialFacebook = new EasyFacebook(context, credentialModel);
		return mEasySocialFacebook;
	}


	private CredentialModel mCredentialModel = null;
	private String mLoginUrl;
	private String mAccessTokenUrl;


	public static final String FACEBOO_API_VERSION_NUMBER = "v2.0";
	public static final String FACEBOOK_GRAPH_SERVER = "https://graph.facebook.com/";
	public static final String FACEBOOK_REQUEST_URL =  FACEBOOK_GRAPH_SERVER+FACEBOO_API_VERSION_NUMBER+"/";
	public static String TypeSquare = "square";
	public static String TypeSmall = "small";
	public static String TypeNormal = "normal";
	public static String TypeLarge = "large";



	/**
	 * This method is used to Authorize user from Facebook after successfull Authorization this method automatically 
	 * call and save the user's info.
	 * @param activityReference
	 * This method require the activity reference
	 */
	public void authorize(Activity activityReference){
		Intent intent = new Intent(activityReference, EasyOauthActivity.class);
		intent.putExtra("url", mLoginUrl);
		intent.putExtra("callbackurl", mCredentialModel.getmCallbackURL());		
		intent.putExtra("access_token_url", mAccessTokenUrl);
		activityReference.startActivity(intent);
	}

	/**
	 * This method return you the JSONObject of user.
	 * @return
	 */
	public JSONObject getUser() {
		return FacebookPreferenceUtil.getUserInfo();
	}

	/**
	 * Send a post on a wall use this method
	 * @param message Message which you want to send on facebook wall.
	 * @param callback this is optional you can send null or if you want to get response than send {@link PostAlgo.callback} 
	 * interface object
	 */
	public void postMessage(String message, PostAlgo.callback callback){
		PostAlgo postAlgo = new PostAlgo();
		postAlgo.setMessage(message);
		postAlgo.execute(callback);
	}


	/**
	 * Get facebook user image
	 * @param asBitmap if you want get bitmap send true and if you want get a image url than send false
	 * @param type You can use four types TypeSquare,TypeSmall,TypeNormal,TypeLarge
	 * @param width 
	 * @param height
	 * @param callback to get the result and all callback events send a GetUserImageAlgo.callback interface object as param
	 */
	public void getUserImage(boolean asBitmap,String type, int width, int height, GetUserImageAlgo.callback callback){
		GetUserImageAlgo getUserImageAlgo = new GetUserImageAlgo(asBitmap,type,width,height);
		getUserImageAlgo.execute(callback);
	}

	private EasyFacebook(Context context, CredentialModel credentialModel){ 
		mContext = context;
		mCredentialModel = credentialModel;
		setLoginUrl();
		setAccessTokenUrl();
	}

	private void setLoginUrl() {
		String loginUrl = "https://www.facebook.com/dialog/oauth?";
		mLoginUrl = String.format(loginUrl+"client_id=%s&redirect_uri=%s&scope",mCredentialModel.getmAppID(),mCredentialModel.getmCallbackURL(),mCredentialModel.getPermissionAsString());
	}

	private void setAccessTokenUrl() {
		String accessTokenUrl = FACEBOOK_GRAPH_SERVER+"oauth/access_token?";
		mAccessTokenUrl = String.format(accessTokenUrl+"client_id=%s&redirect_uri=%s&client_secret=%s&code=",mCredentialModel.getmAppID(),mCredentialModel.getmCallbackURL(),mCredentialModel.getmAppSecretID());	
	}

	public static final String appendAccessToken(String url){
		return url+"access_token="+FacebookPreferenceUtil.getAccessToken();
	}


}









