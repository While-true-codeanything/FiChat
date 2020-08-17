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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private FirebaseAuth mAuth;
    private EditText edt;
    private TextView tv;
    private boolean EditingEmail;
    private Snackbar snackbar;
    private ImageView imageView;
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
        imageView = getActivity().findViewById(R.id.avatar);
        edt = getActivity().findViewById(R.id.Name);
        edt.setText(mAuth.getCurrentUser().getDisplayName());
        tv = getActivity().findViewById(R.id.Ng);
        storage = FirebaseStorage.getInstance().getReference();
        storage.child(mAuth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (imageView.getDrawable() == null) {
                    Glide
                            .with(getContext())
                            .load(uri)
                            .into(imageView);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.getDrawable() != null) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, AVATAR_REQUEST);
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edt2 = getActivity().findViewById(R.id.Name);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edt2.getText().toString())
                        .build();
                mAuth.getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String key = "";
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                User us = data.getValue(User.class);
                                                if (us.getUserid().equals(mAuth.getCurrentUser().getUid()))
                                                    key = data.getKey();
                                            }
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("name").setValue(edt2.getText().toString());
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
                    if (isValidEmail(edt.getText().toString())) {
                        mAuth.getCurrentUser().updateEmail(edt.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = "";
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    User us = data.getValue(User.class);
                                    if (us.getUserid().equals(mAuth.getCurrentUser().getUid()))
                                        key = data.getKey();
                                }
                                FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("email").setValue(edt.getText().toString());
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
                        tv.setText(Html.fromHtml("<u>Reset Email</u>"));
                        edt.setEnabled(false);
                        EditingEmail = false;
                    } else {
                        snackbar = Snackbar
                                .make(getActivity().findViewById(R.id.prf), "Enter correct email!", Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.show();
                    }
                } else {
                    edt = getActivity().findViewById(R.id.Email);
                    edt.setEnabled(true);
                    tv.setText(Html.fromHtml("<u>Save</u>"));
                    EditingEmail = true;
                }
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
                    Uri selectedImage = imageReturnedIntent.getData();
                    Glide
                            .with(this)
                            .load(selectedImage)
                            .into(imageView);
                }
                storage.child(mAuth.getCurrentUser().getUid()).putFile(imageReturnedIntent.getData()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        snackbar = Snackbar
                                .make(getActivity().findViewById(R.id.prf), exception.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Avatar changed!", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    private boolean isValidEmail(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
