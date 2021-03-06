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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrivateChatFragment extends Fragment {
    private Snackbar snackbar;
    private RecyclerView chat;
    private DatabaseReference node;
    private String ChatKey;
    private FloatingActionButton send;
    private Chat chatdata;

    public PrivateChatFragment(String chatKey, Chat cahtdata) {
        ChatKey = chatKey;
        this.chatdata = cahtdata;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        chat = getActivity().findViewById(R.id.list);
        chat.setAdapter(new MessagesAdapter(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), chatdata.getChatlist(), getActivity()));
        node = FirebaseDatabase.getInstance().getReference().child("PrivateChats").child(ChatKey).child("chatlist");
        send = getActivity().findViewById(R.id.SendButton);
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messagedata = new ArrayList<Message>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message mes = data.getValue(Message.class);
                    messagedata.add(mes);
                }
                chatdata.setChatlist(messagedata);
                MessagesAdapter m = (MessagesAdapter) chat.getAdapter();
                m.datachange(messagedata);
                chat.getAdapter().notifyDataSetChanged();
                chat.scrollToPosition(chat.getAdapter().getItemCount() - 1);
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = getActivity().findViewById(R.id.message);
                if (!input.getText().toString().isEmpty()) {
                    chatdata.getChatlist().add(new Message(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    node.setValue(chatdata.getChatlist());
                    input.setText("");
                }
            }
        });
    }
}