package com.example.fichat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserFragment extends Fragment {
    private ArrayList<User> Users;
    private Snackbar snackbar;
    private RecyclerView uslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finduser_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        uslist = getActivity().findViewById(R.id.userlist);
        uslist.setAdapter(new UsersAdapter(new ArrayList<User>(), ((MainActivity) getActivity())));
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> userdata = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User us = data.getValue(User.class);
                    userdata.add(us);
                }
                Users = userdata;
                ImageButton b = getActivity().findViewById(R.id.find);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView tv=getActivity().findViewById(R.id.findValue);
                        String query=tv.getText().toString();
                        ArrayList<User> list=new ArrayList<>();
                        if(isValidEmail(query)){
                            Toast.makeText(getContext(),"Valid",Toast.LENGTH_LONG).show();
                            for(User u:  Users){
                                if(u.getEmail().equals(query)){
                                    list.add(u);
                                }
                            }
                        }
                        else{
                            for(User u:  Users){
                                if(u.getName().equals(query)){
                                    list.add(u);
                                }
                            }
                        }
                        UsersAdapter a= (UsersAdapter) uslist.getAdapter();
                        a.Reset(list);
                        uslist.getAdapter().notifyDataSetChanged();
                    }
                });
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
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
