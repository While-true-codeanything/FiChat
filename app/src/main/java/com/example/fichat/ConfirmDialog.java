package com.example.fichat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ConfirmDialog extends DialogFragment {
    private String ousi;

    public ConfirmDialog(String ousi) {
        this.ousi = ousi;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Warning";
        String message = "Do you really want to create chat with this user?";
        String button1String = "Yes";
        String button2String = "Cancel";
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseDatabase.getInstance().getReference().child("PrivateChats").push()
                        .setValue(new Chat(new ArrayList<Message>(), FirebaseAuth.getInstance().getUid() + "&" + ousi)
                        );
                ((MainActivity) Objects.requireNonNull(getActivity())).loadFragment(new ChatFragment());
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
