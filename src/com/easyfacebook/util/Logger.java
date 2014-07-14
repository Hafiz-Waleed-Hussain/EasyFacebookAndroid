package com.easyfacebook.util;

import android.util.Log;

public class Logger {

	public static boolean DEBUG = true;

	public static final void showLog(String message){
		if( DEBUG ){
			Log.d("EasySocial", message == null ? "Null":message);
		}
	}

}
