package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private LoginActivity la;
    private TextView mail;
    private TextView pass;
    private TextView reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        mail=findViewById(R.id.LoginEmail);
        pass=findViewById(R.id.LoginPassword);
        la=this;
        reg=findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(la, MainActivity.class));
                finish();
            }
        });
        Button sg=findViewById(R.id.Sign);
        sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Login=mail.getText().toString();
                String Password=pass.getText().toString();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(Login, Password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    startActivity(new Intent(la, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(la,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });
    }
}
