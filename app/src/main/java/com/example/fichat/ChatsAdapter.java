package com.example.fichat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private ArrayList<Chat> Chats;

    public ChatsAdapter(ArrayList<Chat> chats) {
        Chats = chats;
    }

    public void Reset(ArrayList<Chat> chats) {
        Chats = chats;
    }

    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ChatsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ViewHolder holder, final int position) {
        holder.lastmes.setText(Chats.get(position).getChatlist().get(Chats.get(position).getChatlist().size() - 1).getMessageText());
        if ((Chats.get(position).getUsers().get(0).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
            holder.name.setText(Chats.get(position).getUsers().get(1).getName());
        } else {
            holder.name.setText(Chats.get(position).getUsers().get(0).getName());
        }
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView lastmes;
        private CardView user;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            lastmes = itemView.findViewById(R.id.useremail);
            user = itemView.findViewById(R.id.user);
        }
    }
}