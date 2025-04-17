package com.example.applicationtest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.applicationtest.MainActivity;
import com.example.applicationtest.R;
import com.example.applicationtest.activity.PersonManageActivity;
import com.example.applicationtest.activity.WordManageActivity;

public class EFragment extends Fragment {

    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root == null){
            root = inflater.inflate(R.layout.fragment_e, container, false);
        }

        View personmanager = root.findViewById(R.id.personmanager);
        personmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonManageActivity.class);
                startActivity(intent);
            }
        });

        View wordmanager = root.findViewById(R.id.wordmanager);
        wordmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WordManageActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}