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
    private ArrayList<String> Keys;
    private MainActivity ma;

    public ChatsAdapter(ArrayList<Chat> chats, ArrayList<String> keys, MainActivity ma) {
        Chats = chats;
        Keys = keys;
        this.ma = ma;
    }

    public void Reset(ArrayList<Chat> chats, ArrayList<String> keys) {
        Chats = chats;
        Keys = keys;
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
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.loadFragment(new PrivateChatFragment(Keys.get(position), Chats.get(position)));
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
        private CardView chat;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            lastmes = itemView.findViewById(R.id.useremail);
            chat = itemView.findViewById(R.id.user);
        }
    }
}