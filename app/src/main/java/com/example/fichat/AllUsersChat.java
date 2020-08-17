package com.example.fichat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

public class AllUsersChat extends Fragment {
    private DatabaseReference myRef;
    private RecyclerView allmessagelist;
    private FloatingActionButton send;
    private Snackbar snackbar;
    private FirebaseUser currentUser;

    public AllUsersChat(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        allmessagelist = getActivity().findViewById(R.id.list);
        final LinearLayout lay = getActivity().findViewById(R.id.chatlay);
        myRef = FirebaseDatabase.getInstance().getReference();
        allmessagelist.setAdapter(new MessagesAdapter(currentUser.getDisplayName(), new ArrayList<Message>(), getActivity()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messagedata = new ArrayList<Message>();
                for (DataSnapshot data : dataSnapshot.child("AllUsersChat").getChildren()) {
                    //Getting User object from dataSnapshot
                    Message mes = data.getValue(Message.class);
                    messagedata.add(mes);
                }
                /*Toast.makeText(ma,messagedata.get(messagedata.size()).getMessageText(),Toast.LENGTH_LONG).show();*/
                /*allmessagelist.setAdapter(new MessagesAdapter(currentUser.getDisplayName(),messagedata));*/
                MessagesAdapter m = (MessagesAdapter) allmessagelist.getAdapter();
                m.datachange(messagedata);

                allmessagelist.getAdapter().notifyDataSetChanged();
                allmessagelist.scrollToPosition(allmessagelist.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                snackbar = Snackbar
                        .make((LinearLayout) getActivity().findViewById(R.id.prf), databaseError.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                snackbar.show();
            }
        });
        send = getActivity().findViewById(R.id.SendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) getActivity().findViewById(R.id.message);
                myRef.child("AllUsersChat").push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), currentUser.getUid())
                        );
                input.setText("");
            }
        });
    }
}
