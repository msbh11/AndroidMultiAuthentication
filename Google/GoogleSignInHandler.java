package org.MultiAuthentication;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleSignInHandler {
    int RC_SIGN_IN = 12345;
    GoogleSignInListener listener;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;
    private FirebaseAuth mAuth;


    public GoogleSignInHandler(Activity activity) {
        this.activity = activity;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signIn(GoogleSignInListener listener) {
        this.listener = listener;
        mAuth.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void handleSignInResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                listener.onSuccess(account.getEmail(), account.getDisplayName());

            } catch (ApiException e) {
                listener.onFailure(e.getMessage());
            }
        }
    }

}
