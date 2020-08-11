package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private RegistrationActivity ra;
    private EditText mail;
    private EditText pass;
    private EditText rpass;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        mail = findViewById(R.id.Registration_Email);
        pass = findViewById(R.id.Registration_Password);
        rpass = findViewById(R.id.Registration_RepPassword);
        mAuth = FirebaseAuth.getInstance();
        ra = this;
        mProgressBar = findViewById(R.id.progressBar);
        Button bt = findViewById(R.id.su);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(rpass.getText().toString())) {
                    /* mProgressBar.setVisibility(View.VISIBLE);*/
                    mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(ra, MainActivity.class));
                                        Toast.makeText(ra, "Successful", Toast.LENGTH_LONG);
                                        finish();
                                    } else {
                                        Toast.makeText(ra, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG);
                                    }

                                }
                            });
                } else {
                    Toast.makeText(ra, "Passwords don't match! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
