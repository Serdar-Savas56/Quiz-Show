package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_page extends AppCompatActivity {
    EditText mail;
    EditText password;
    Button SignIn;
    SignInButton SignInGoogle;
    TextView SignUp;
    TextView forgotPassword;
    ProgressBar progressBarSignİn;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        SignIn = findViewById(R.id.buttonLoginSignİn);
        SignInGoogle = findViewById(R.id.ButtonLoginGoogleSign);
        SignUp = findViewById(R.id.textViewLoginSignUp);
        forgotPassword = findViewById(R.id.textViewLoginForgetPassword);
        progressBarSignİn=findViewById(R.id.progressBarSignin);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail=mail.getText().toString();
                String userPassword=password.getText().toString();
                signInWithFirebase(userMail,userPassword);

            }
        });

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient= GoogleSignIn.getClient(this, gso);

        SignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultLauncher.launch(new Intent(googleSignInClient.getSignInIntent()));

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_page.this, sign_up_page.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_page.this,forgot_Password.class);
                startActivity(i);

            }
        });
    }
    public void signInWithFirebase(String userMail, String userPassword)
    {
        progressBarSignİn.setVisibility(View.INVISIBLE);
        SignIn.setClickable(false);
        auth.signInWithEmailAndPassword(userMail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(Login_page.this,MainActivity.class);
                            startActivity(i);
                            finish();
                            progressBarSignİn.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login_page.this,"Giriş İşlemi Başarılı"
                                             ,Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(Login_page.this,"Giriş İşlemi Başarısız"
                                    ,Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            Intent i = new Intent(Login_page.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);


                    assert account != null;
                    firebaseAuthWithGoogle(account.getIdToken());
                }
                catch (ApiException e)
                {
                    e.printStackTrace();

                }
            }

        }
    });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login_page.this,"Giriş işlemi Başarılı",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Login_page.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_page.this,"Giriş işlemi Başarısız",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }
    }

    private void  firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(Login_page.this,"Giriş işlemi Başarılı",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Login_page.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            Toast.makeText(Login_page.this,"Giriş işlemi Başarısız",Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseGoogleAccount(GoogleSignInAccount account)
    {
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful())
              {
                  FirebaseUser user=auth.getCurrentUser();
              }
              else
              {

              }
            }
        });
    }*/
}