package com.example.fichat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private ArrayList<User> Users;

    public UsersAdapter(ArrayList<User> users) {
        Users = users;
    }
    public void Reset(ArrayList<User> l){
        Users=l;
    }
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        holder.email.setText(Users.get(position).getEmail());
        holder.name.setText(Users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.username);
            email = (TextView) itemView.findViewById(R.id.useremail);
        }
    }
}
