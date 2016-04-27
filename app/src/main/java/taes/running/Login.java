package taes.running;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class Login extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

private static final String TAG = "SignInActivity";
private static final int RC_SIGN_IN = 9001;

private GoogleApiClient mGoogleApiClient;
private TextView mStatusTextView;
private ProgressDialog mProgressDialog;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
        } else {
             opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                 @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Log.d(TAG, "Got dsfsfsdfsdfsf sign-in");
                 }
                });
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sign_in_button:
        signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Got cached malamalamlamal   maaaaaal      -in");
    }



    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Usuario user = new Usuario();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            user.setEmail(result.getSignInAccount().getEmail());
          user.setNombre(result.getSignInAccount().getDisplayName().toString());
            user.setId(result.getSignInAccount().getIdToken());
            user.setFoto(result.getSignInAccount().getPhotoUrl().toString());
            Intent intent = new Intent(this, Principal.class);
            intent.putExtra("Usuario", user);
            startActivity(intent);
        }
    }

}