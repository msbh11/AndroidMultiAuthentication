package org.MultiAuthentication;

public interface GoogleSignInListener {
    void onSuccess(String email, String name);

    void onFailure(String error);
}
