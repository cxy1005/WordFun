package com.example.applicationtest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.applicationtest.adapter.FragmentAdapter;
import com.example.applicationtest.fragment.AFragment;
import com.example.applicationtest.fragment.BFragment;
import com.example.applicationtest.fragment.CFragment;
import com.example.applicationtest.fragment.DFragment;
import com.example.applicationtest.fragment.EFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigation;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化MMKV
        String rootDir = MMKV.initialize(this);
        Log.e(TAG, "MMKV: "+rootDir);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

        setupViewPager();
        setupBottomNavigation();
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());
        fragments.add(new DFragment());
        fragments.add(new EFragment());

        FragmentAdapter adapter = new FragmentAdapter(this, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true); // 启用滑动

        // 监听ViewPager2页面变化，同步BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    private void setupBottomNavigation() {
        // 监听BottomNavigationView点击事件，切换ViewPager2页面
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_a) {
                viewPager.setCurrentItem(0);
            } else if (itemId == R.id.nav_b) {
                viewPager.setCurrentItem(1);
            } else if (itemId == R.id.nav_c) {
                viewPager.setCurrentItem(2);
            } else if (itemId == R.id.nav_d) {
                viewPager.setCurrentItem(3);
            } else if (itemId == R.id.nav_e) {
                viewPager.setCurrentItem(4);
            }
            return true;
        });
    }
}