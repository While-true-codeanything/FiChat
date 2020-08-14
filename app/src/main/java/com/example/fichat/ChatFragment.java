package com.example.fichat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        /*toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        new SetData(false).execute();
        BottomNavigationView nav = getActivity().findViewById(R.id.tmenu);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Text_data:
                        new SetData(false).execute();
                        return true;
                    case R.id.Files_data:
                        new SetData(true).execute();
                        return true;
                }
                return false;
            }
        });*/
    }

}
