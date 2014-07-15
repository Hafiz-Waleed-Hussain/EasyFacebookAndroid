<b>EasyFacebookAndroid</b><br/>

In this Beta version i add these features.<br/>
. Facebook Auth<br/>
. Facebook User Info<br/>
. Facebook Post a message<br/>
. Facebook Get picture<br/>

First add this lib into Android project.<br/>
Add internet permission into manifest file.<br/>
Add below activity into your project manifest file.<br/>
        
        <activity
            android:name="com.easyfacebook.activities.EasyOauthActivity">
        </activity>

Now getinstance of EasyFacebook class.<br/>
	private EasyFacebook mEasyFacebook = null;<br/>
	private CredentialModel mFacebookCredentials = null;<br/><br/>

  // In below code give  redirect url = 	https://www.facebook.com/connect/login_success.html;<br/>
  // In permission you can give null or String permissions[] = {"email"};<br/>
	mFacebookCredentials = new CredentialModel(appid, appSecretId,redirectUrl,permissions);<br/>
	mEasyFacebook = EasyFacebook.getInstance(this,facebookCredentialModel);<br/>

  Now integration is complete.<br/>
  Now time for magic.<br/>
  1. Auth or SignIn<br/>
     <b>mEasyFacebook.authorize(this) // Here  'this' is an Activity reference <br/></b>
  2. Get UserInfo<br/>
     <b>mEasyFacebook.getUser() // At this time you get JSON object. I will change into UserModel in future release<br/></b>
  3. Post a message<br/>
     <b>mEasyFacebook.postMessage("Test", null);</b><br/> // In future release i will give overload methods for post
  4. Get Image<br/>
     <b>		mEasyFacebook.getUserImage(true, EasyFacebook.TypeSmall, 500, 500, new callback() {<br/>
			@Override public void start() {}<br/>
			@Override public void fail(Object data) {}<br/>
			@Override public void complete(String imageUrl) {}<br/>
			@Override public void complete(final Bitmap image) {}<br/>
		});</b><br/>

