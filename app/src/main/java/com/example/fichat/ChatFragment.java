package com.example.fichat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private Snackbar snackbar;
    private RecyclerView chatlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.userchat_page, container, false);
    }

    @Override
    public void onStart() {
        setHasOptionsMenu(true);
        super.onStart();
        chatlist = getActivity().findViewById(R.id.userchatlist);
        chatlist.setAdapter(new ChatsAdapter(new ArrayList<Chat>()));
        FirebaseDatabase.getInstance().getReference().child("PrivateChats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Chat> chatdata = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    /*Toast.makeText(getContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();*/
                    Chat cht = data.getValue(Chat.class);
                    chatdata.add(cht);
                }
                FirebaseUser curus = FirebaseAuth.getInstance().getCurrentUser();
                ArrayList<Chat> curchatdata = new ArrayList<>();
                for (Chat c : chatdata) {
                    if ((c.getUsers().get(0).getUserid().equals(curus.getUid())) || (c.getUsers().get(1).getUserid().equals(curus.getUid()))) {
                        curchatdata.add(c);
                    }
                }
                ((ChatsAdapter) chatlist.getAdapter()).Reset(curchatdata);
                chatlist.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                snackbar = Snackbar
                        .make((LinearLayout) getActivity().findViewById(R.id.kr), databaseError.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.findchat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Операции для выбранного пункта меню
        switch (item.getItemId())
        {
            case R.id.add:{
                MainActivity m= (MainActivity) getActivity();
                    m.loadFragment(new FindUserFragment());
                return true;
            }
        }
        return false;
    }

}
