package com.example.applicationtest.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.applicationtest.R;
import com.google.android.material.card.MaterialCardView;

public class CFragment extends Fragment {

    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c, container, false);

        // 获取三个卡片视图
        MaterialCardView cardGame = view.findViewById(R.id.cardGame);
        MaterialCardView cardTest = view.findViewById(R.id.cardTest);
        MaterialCardView cardCompetition = view.findViewById(R.id.cardCompetition);

        // 设置点击事件
        /*cardGame.setOnClickListener(v -> navigateToGameSection());
        cardTest.setOnClickListener(v -> navigateToTestSection());
        cardCompetition.setOnClickListener(v -> navigateToCompetitionSection());
*/
        return view;
    }


}