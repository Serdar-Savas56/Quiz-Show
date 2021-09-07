package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_up_page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button SignUp;
    ProgressBar progressBar;

    FirebaseAuth auth =FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mail = findViewById(R.id.editTextSignUpEmail);
        password = findViewById(R.id.editTextSignUpPassword);
        SignUp = findViewById(R.id.buttonSignUpSign);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp.setClickable(false);
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signUpFirebase(userEmail, userPassword);

            }
        });
    }

    public void signUpFirebase(String userEmail, String userPassword)
    {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(sign_up_page.this, "İşlem Başarılı",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(sign_up_page.this, "Bir Problem Oluştu! Lütfen Tekrar Deneyiniz",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}