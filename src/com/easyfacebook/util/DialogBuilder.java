package com.easyfacebook.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import com.easysocial.R;

public class DialogBuilder {

	public static Dialog createDialog(Context context){
		return createDialog(context,context.getString(R.string.please_wait_));
	}

	public static Dialog createDialog(Context context, String message){
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);	
		return progressDialog;
	}
}
