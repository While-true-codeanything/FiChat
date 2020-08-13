package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MainActivity ma;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        ma=this;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }else {
            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        tv = findViewById(R.id.YrVer);
                        tv.setText(String.format("Verified: %s", mAuth.getCurrentUser().isEmailVerified()));
                    } else {
                        Toast.makeText(ma, "Some problems with updating your account. Try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
            setContentView(R.layout.activity_main);
            tv = findViewById(R.id.YrEmail);
            tv.setText(String.format("%sYour name: %s", String.format("Your Emal: %s", currentUser.getEmail()), currentUser.getDisplayName()));
            tv = findViewById(R.id.Qt);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    startActivity(new Intent(ma, LoginActivity.class));
                    ma.finish();
                }
            });
            tv = findViewById(R.id.YrVer);

            tv.setText(String.format("Verified: %s", currentUser.isEmailVerified()));
        }
    }
}