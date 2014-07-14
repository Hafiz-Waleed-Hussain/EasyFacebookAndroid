package com.easyfacebook.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.easyfacebook.EasyFacebook;

public class FacebookPreferenceUtil {

	private static final String PREFERENCE_NAME = "facebook_preference";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String USER_INFO = "user_info";

	
	public static final void setAccesToken(String accessToken){
		SharedPreferences sharedPreferences = getSharedPreferences();
		sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).commit();
	}

	public static final String getAccessToken(){
		return getSharedPreferences().getString(ACCESS_TOKEN, "");
	}

	public static final void setUserInfo(String userInfo){
		getSharedPreferences().edit().putString(USER_INFO, userInfo).commit();
	}
	
	public static final JSONObject getUserInfo(){
		try {
			return new JSONObject(getSharedPreferences().getString(USER_INFO, ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static SharedPreferences getSharedPreferences(){
		SharedPreferences sharedPreferences = EasyFacebook.mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences;
	}

}
