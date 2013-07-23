package com.lulu;


import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class WeiboPlugin extends CordovaPlugin {
	Weibo weibo;
	private String appKey;
    private String redirectUrl;
    private String scope = "email,direct_messages_read,direct_messages_write," +
			"friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
			"follow_app_official_microblog";
    private SsoHandler mSsoHandler;
    
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		cordova.setActivityResultCallback(this);
		appKey = args.getString(0);
		redirectUrl = args.getString(1);
		weibo = Weibo.getInstance(appKey, redirectUrl, scope);
		if("login".equals(action)){
			doLogin(callbackContext);
			return true;
		}else{
			return false;
		}
	}
	
	private boolean doLogin(CallbackContext callbackContext){
		mSsoHandler = new SsoHandler(cordova.getActivity(), weibo);
        mSsoHandler.authorize(new AuthDialogListener(callbackContext), null);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, intent);
        }
	}
	

}

class AuthDialogListener implements WeiboAuthListener {
    private CallbackContext callbackContext;
    
	public AuthDialogListener(CallbackContext callbackContext){
		this.callbackContext = callbackContext;
	}
    @Override
    public void onComplete(Bundle values) {
        String code = values.getString("code");
        JSONObject json = new JSONObject();
    	try {
			json.put("code", code);
			callbackContext.success(json);        
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(getErrorObject(e.getMessage()));
		}
    }

    @Override
    public void onError(WeiboDialogError e) {
    	callbackContext.error(getErrorObject(e.getMessage()));
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onWeiboException(WeiboException e) {
    	callbackContext.error(getErrorObject(e.getMessage()));
    }
    
    private JSONObject getErrorObject(String message){
    	JSONObject json = new JSONObject();
    	try {
			json.put("error", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return json;
    }

}


