package com.easyfacebook.models;


public class CredentialModel {

	private String mAppID;
	private String mAppSecretID;
	private String mCallbackURL;
	private String[] mPermissions = null;

	public CredentialModel(String appID, String appSecretID,
			String callbackURL, String[] permissions) {
		this.mAppID = appID;
		this.mAppSecretID = appSecretID;
		this.mCallbackURL = callbackURL;
		mPermissions = permissions;
	}

	
	public String getmAppID() {return mAppID;}
	public String getmAppSecretID() {return mAppSecretID;}
	public String getmCallbackURL() {return mCallbackURL;}
	public String[] getPermissions(){return mPermissions;}
	public String getPermissionAsString(){
		
		if( mPermissions == null) return "";
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mPermissions.length; i++) {
			stringBuilder.append(mPermissions[i]+",");
		}
		try{
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}catch(StringIndexOutOfBoundsException e){
			return "";
		}
		return stringBuilder.toString();
	}

}
