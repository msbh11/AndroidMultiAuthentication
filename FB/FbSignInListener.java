package org.MultiAuthentication;

public interface FbSignInListener {

    void onFbLoginSucceed(String email, String name);

    void onFbLoginFailed(String error);

}
