package com.example.fichat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private ArrayList<User> Users;
    private MainActivity act;

    public UsersAdapter(ArrayList<User> users, MainActivity act) {
        Users = users;
        this.act = act;
    }

    public void Reset(ArrayList<User> l) {
        Users = l;
    }
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapter.ViewHolder holder, final int position) {
        holder.email.setText(Users.get(position).getEmail());
        holder.name.setText(Users.get(position).getName());
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = act.getSupportFragmentManager();
                ConfirmDialog myDialogFragment = new ConfirmDialog(Users.get(position));
                myDialogFragment.show(manager, "Tag");
            }
        });
        FirebaseStorage.getInstance().getReference().child(Users.get(position).getUserid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                        .with(act)
                        .load(uri)
                        .into(holder.img);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;
        private CardView user;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.useremail);
            user = itemView.findViewById(R.id.user);
            img = (ImageView) itemView.findViewById(R.id.ava);
        }
    }
}
