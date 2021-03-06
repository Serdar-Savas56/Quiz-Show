package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_Password extends AppCompatActivity {
    EditText mail;
    Button button;
    ProgressBar progressBar;

    FirebaseAuth auth =FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        mail=findViewById(R.id.editTextPasswordEmail);
        button=findViewById(R.id.buttonPasswordDevam);
        progressBar=findViewById(R.id.progressBarPasswordForget);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail=mail.getText().toString();
                resetPassword(userMail);

            }
        });
    }

    public void resetPassword(String userMail)
    {
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(userMail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(forgot_Password.this,"Mail adresinize şifre sıfırlama maili gönderilmiştir",Toast.LENGTH_SHORT).show();
                           button.setClickable(false);
                           progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(forgot_Password.this,"Bir problem oluştu,Lütfen tekrar deneyiniz...",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}