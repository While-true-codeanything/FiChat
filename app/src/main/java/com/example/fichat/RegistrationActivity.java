package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private RegistrationActivity ra;
    private EditText mail;
    private EditText pass;
    private EditText rpass;
    private EditText name;
    private FirebaseAuth mAuth;
    private ConstraintLayout lay;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        mail = findViewById(R.id.Registration_Email);
        pass = findViewById(R.id.Registration_Password);
        name = findViewById(R.id.Registration_Name);
        rpass = findViewById(R.id.Registration_RepPassword);
        mAuth = FirebaseAuth.getInstance();
        lay = findViewById(R.id.reglay);
        ra = this;
        Button bt = findViewById(R.id.su);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(rpass.getText().toString())) {
                    if (mail.getText().length() == 0 || pass.getText().length() == 0 || rpass.getText().length() == 0 || name.getText().length() == 0) {
                        showmessage("Fill in all fields");
                    } else {
                        if (isValidEmail(mail.getText().toString())) {
                            if (pass.getText().toString().length() >= 6) {

                                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(name.getText().toString())
                                                            .build();
                                                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(ra, "Successful", Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                                                        startActivity(new Intent(ra, MainActivity.class));
                                                                    } else {
                                                                        showmessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                                                    }
                                                                }
                                                            });

                                                } else {
                                                    showmessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                                }

                                            }
                                        });
                            } else {
                                showmessage("Password must not be less than 6 symbols");
                            }
                        } else {
                            showmessage("Enter the correct email address");
                        }
                    }
                } else {
                    showmessage("Passwords don't match! Check again");
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

    private void showmessage(String message) {
        snackbar = Snackbar
                .make(lay, message, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.show();
    }
}
