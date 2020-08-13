package com.example.fichat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private MainActivity ma;
    private TextView tv;
    private FloatingActionButton send;
    private Snackbar snackbar;
    private RecyclerView allmessagelist;

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
            setContentView(R.layout.main_content);
            allmessagelist = findViewById(R.id.list);
            final LinearLayout lay = findViewById(R.id.chatlay);
            myRef = FirebaseDatabase.getInstance().getReference();
            allmessagelist.setAdapter(new MessagesAdapter(currentUser.getDisplayName(), new ArrayList<Message>()));
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
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Message> messagedata = new ArrayList<Message>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        //Getting User object from dataSnapshot
                        Message mes = data.getValue(Message.class);
                        messagedata.add(mes);
                    }
                    /*Toast.makeText(ma,messagedata.get(messagedata.size()).getMessageText(),Toast.LENGTH_LONG).show();*/
                    /*allmessagelist.setAdapter(new MessagesAdapter(currentUser.getDisplayName(),messagedata));*/
                    MessagesAdapter m = (MessagesAdapter) allmessagelist.getAdapter();
                    m.datachange(messagedata);

                    allmessagelist.getAdapter().notifyDataSetChanged();
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
            });
            send = findViewById(R.id.SendButton);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = (EditText) findViewById(R.id.message);
                    myRef.push()
                            .setValue(new Message(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );
                    input.setText("");
                }
            });
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