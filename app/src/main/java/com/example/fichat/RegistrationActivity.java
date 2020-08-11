package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private RegistrationActivity ra;
    private EditText mail;
    private EditText pass;
    private EditText rpass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        mail = findViewById(R.id.Registration_Email);
        pass = findViewById(R.id.Registration_Password);
        rpass = findViewById(R.id.Registration_RepPassword);
        mAuth = FirebaseAuth.getInstance();
        ra = this;
        Button bt = findViewById(R.id.su);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(rpass.getText().toString())) {
                    if (mail.getText().length() == 0 || pass.getText().length() == 0 || rpass.getText().length() == 0) {
                        Toast.makeText(ra, "Fill in all fields", Toast.LENGTH_LONG).show();
                    } else {
                        if (isValidEmail(mail.getText().toString())) {
                            if (pass.getText().length() < 6) {
                                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    ;
                                                    Toast.makeText(ra, "Successful", Toast.LENGTH_LONG);
                                                    finish();
                                                    mAuth.getCurrentUser().sendEmailVerification();
                                                    startActivity(new Intent(ra, MainActivity.class));
                                                } else {
                                                    Toast.makeText(ra, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            } else {
                                Toast.makeText(ra, "Password must not be less than 6 symbols", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ra, "Enter the correct email address", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(ra, "Passwords don't match! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
