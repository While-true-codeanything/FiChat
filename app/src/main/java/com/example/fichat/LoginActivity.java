package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private LoginActivity la;
    private EditText mail;
    private EditText pass;
    private TextView reg;
    private ConstraintLayout lay;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.LoginEmail);
        pass = findViewById(R.id.LoginPassword);
        lay = findViewById(R.id.loginlay);
        la = this;
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(la, RegistrationActivity.class));
                finish();
            }
        });
        Button sg = findViewById(R.id.Sign);
        sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mail.getText().length() == 0 || pass.getText().length() == 0) {
                    Toast.makeText(la, "Password or login must not be null", Toast.LENGTH_LONG).show();
                } else {
                    if (isValidEmail(mail.getText().toString())) {
                        String Login = mail.getText().toString();
                        String Password = pass.getText().toString();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithEmailAndPassword(Login, Password).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(la, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(la, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        /*new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Sorry")
                                .setMessage("Enter the correct email address ")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create()
                                .show();*/
                       /* Snackbar.make(
                                lay,
                                "Enter the correct email address",
                                Snackbar.LENGTH_LONG
                        ).show();*/
                        snackbar = Snackbar
                                .make(lay, "Enter the correct email address", Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.show();
                    }
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
