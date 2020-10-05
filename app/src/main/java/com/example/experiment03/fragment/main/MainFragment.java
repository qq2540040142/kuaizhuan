package com.example.experiment03.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment03.R;
import com.example.experiment03.adapter.b_tablayout_adapter;
import com.example.experiment03.custom.bottomtablayout.CustomBotTabItem;
import com.example.experiment03.custom.viewpager.NoScrollViewPager;
import com.example.experiment03.fragment.main.fragment.FirstFragment;
import com.example.experiment03.fragment.main.fragment.MineFragment;
import com.example.experiment03.fragment.main.fragment.SecondFragment;
import com.example.experiment03.fragment.main.fragment.ThirdFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

;

public class MainFragment extends Fragment{
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private MineFragment mineFragment;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;
    private List<Fragment> mFragmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //初始化布局
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        findView(root);
        initFragment();
        setVpAdapter();

        CustomBotTabItem item = CustomBotTabItem.create();
        item.setContext(getContext())
                .setViewPager(mViewPager)
                .setTabLayout(mTabLayout)
                .build();
        return root;
    }

    //初始化控件
    public void findView(View root){
        mTabLayout = root.findViewById(R.id.id_tab_layout);
        mViewPager = root.findViewById(R.id.id_vp);
        //实例化fragment
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        mineFragment =new MineFragment();
    }

    //初始化fragment
    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(firstFragment);
        mFragmentList.add(secondFragment);
        mFragmentList.add(thirdFragment);
        mFragmentList.add(mineFragment);
    }

    //设置ViewPager的Adapter
    private void setVpAdapter() {
        mViewPager.setAdapter(new b_tablayout_adapter(getChildFragmentManager(), mFragmentList));
    }
}

