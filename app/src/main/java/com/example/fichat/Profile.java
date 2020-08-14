package com.example.fichat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private FirebaseAuth mAuth;
    private EditText edt;
    private TextView tv;
    private boolean EditingEmail;
    private Snackbar snackbar;
    static final int AVATAR_REQUEST = 66;
    StorageReference storage;

    public Profile(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.getCurrentUser().reload();
        edt = getActivity().findViewById(R.id.Name);
        edt.setText(mAuth.getCurrentUser().getDisplayName());
        tv = getActivity().findViewById(R.id.Ng);
        FirebaseStorage storagef = FirebaseStorage.getInstance();
        storage = storagef.getReference();
        ImageView imageView = getActivity().findViewById(R.id.avatar);
        Glide.with(getContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(imageView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edt2 = getActivity().findViewById(R.id.Name);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edt2.getText().toString())
                        .build();
                mAuth.getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Name Changed", Toast.LENGTH_LONG).show();
                                } else {
                                    snackbar = Snackbar
                                            .make(getActivity().findViewById(R.id.prf), "Error! Please try again late!", Snackbar.LENGTH_LONG)
                                            .setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                    snackbar.show();
                                }
                            }
                        });
            }
        });
        edt = getActivity().findViewById(R.id.Email);
        edt.setText(mAuth.getCurrentUser().getEmail());
        edt.setEnabled(false);
        tv = getActivity().findViewById(R.id.Qt);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        tv = getActivity().findViewById(R.id.Rt);
        EditingEmail = false;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EditingEmail) {
                    mAuth.getCurrentUser().verifyBeforeUpdateEmail(edt.getText().toString());
                    tv.setText(Html.fromHtml("<u>Reset Email</u>"));
                    edt.setEnabled(false);
                    EditingEmail = false;
                } else {
                    edt = getActivity().findViewById(R.id.Email);
                    edt.setEnabled(true);
                    tv.setText(Html.fromHtml("<u>Save</u>"));
                    EditingEmail = true;
                }
            }
        });
        ImageView avatar = getActivity().findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, AVATAR_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        ImageView imageView = getActivity().findViewById(R.id.avatar);

        switch (requestCode) {
            case AVATAR_REQUEST:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = imageReturnedIntent.getData();
                    Glide.with(this).load(selectedImage).into(imageView);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(selectedImage)
                            .build();
                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Avatar Changed", Toast.LENGTH_LONG).show();
                                    } else {
                                        snackbar = Snackbar
                                                .make(getActivity().findViewById(R.id.prf), "Error! Please try again late!", Snackbar.LENGTH_LONG)
                                                .setAction("Ok", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        snackbar.dismiss();
                                                    }
                                                });
                                        snackbar.show();
                                    }
                                }
                            });
                }
        }

    }
}
