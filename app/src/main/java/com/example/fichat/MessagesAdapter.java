package com.example.fichat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private final String user;
    private ArrayList<Message> mMessageList;

    public MessagesAdapter(String username, ArrayList<Message> mMessageList) {
        this.mMessageList = mMessageList;
        this.user = username;
    }

    public void datachange(ArrayList<Message> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message current = mMessageList.get(position);
        if (current.getMessageUser().equals(user)) {
            holder.user.setText("You");
        } else {
            holder.user.setText(current.getMessageUser());
        }
        holder.text.setText(current.getMessageText());
        holder.time.setText(current.getMessageTime());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView text;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.message_user);
            text = (TextView) itemView.findViewById(R.id.message_text);
            time = (TextView) itemView.findViewById(R.id.message_time);
        }
    }
}