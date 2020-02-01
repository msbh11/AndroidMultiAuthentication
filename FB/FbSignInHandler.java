package org.MultiAuthentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FbSignInHandler {

    public CallbackManager callbackManager;
    private LoginButton fbloginButton;
    private FbSignInListener fbSignInListener;
    private String TAG = "FbSignInHandlerNoti";
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private Context context;

    public FbSignInHandler(Context context) {

        this.context = context;
        initFB();
    }

    private void initFB() {
        callbackManager = CallbackManager.Factory.create();

        fbloginButton = new LoginButton(context);
        fbloginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Login Success: " + loginResult.getAccessToken());
                graphApi(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                fbSignInListener.onFbLoginFailed("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.getMessage());
                fbSignInListener.onFbLoginFailed(error.getMessage());
            }
        });

    }

    public void clickOnLogin(FbSignInListener listener) {
        LoginManager.getInstance().logOut();
        this.fbSignInListener = listener;
        fbloginButton.performClick();
    }


    public void graphApi(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String email = object.getString("email");
                    String name = object.getString("name");

                    Log.d(TAG, "onCompleted: " + email);
                    Log.d(TAG, "onCompleted: " + name);

                    fbSignInListener.onFbLoginSucceed(email, name);

                } catch (JSONException e) {
                    e.printStackTrace();
                    fbSignInListener.onFbLoginFailed("Data Not found");
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }


}
