package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MainActivity ma;

    private BottomNavigationView navigation;

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.place, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        ma = this;
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                       /* tv = findViewById(R.id.YrVer);
                        tv.setText(String.format("Verified: %s", mAuth.getCurrentUser().isEmailVerified()));*/
                    } else {
                        Toast.makeText(ma, "Some problems with updating your account. Try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
            setContentView(R.layout.activity_main);
            navigation = findViewById(R.id.navigation);
            loadFragment(new AllUsersChat(currentUser));
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_chats:
                            loadFragment(new Fragment());
                            setTitle("FiChat");
                            return true;
                        case R.id.navigation_group:
                            loadFragment(new AllUsersChat(currentUser));
                            setTitle("FiChat");
                            return true;
                        case R.id.navigation_profile:
                            loadFragment(new Profile(mAuth));
                            setTitle("Profile");
                            return true;
                    }
                    return false;
                }
            });
          /*  myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Message> messagedata=new ArrayList<Message>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Message mes = data.getValue(Message.class);
                        messagedata.add(mes);
                    }
                    allmessagelist.setAdapter(new MessagesAdapter(currentUser.getDisplayName(),messagedata));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    snackbar = Snackbar
                            .make(lay, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                    snackbar.show();
                }
            });*/
           /* tv = findViewById(R.id.YrEmail);
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

            tv.setText(String.format("Verified: %s", currentUser.isEmailVerified()));*/
        }
    }
}