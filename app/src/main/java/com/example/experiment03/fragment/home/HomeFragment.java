package com.example.experiment03.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.experiment03.activity.ChangepwActivity;
import com.example.experiment03.R;
import com.example.experiment03.tools.tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView mainusername,mainidenity,mainsuoshu,maintime,mainnowtime,mainmoblienumber;
    private Button btn_changeps;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mainusername = root.findViewById(R.id.mainusername);
        mainidenity = root.findViewById(R.id.mainidentity);
        mainsuoshu = root.findViewById(R.id.mainsuoshu);
        maintime = root.findViewById(R.id.maintime);
        mainnowtime = root.findViewById(R.id.mainnowtime);
        mainmoblienumber = root.findViewById(R.id.mainmoblienumber);
        btn_changeps = root.findViewById(R.id.btn_changeps);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        homeViewModel.getMainusername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                int poition = s.length()+8;
                String a = "用户名:    "+ s;
                mainusername.setText(tools.SetUnderlineDisplay(8,poition,a));
            }
        });
        homeViewModel.getMainidenty().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                mainidenity.setText(tools.SetUnderlineDisplay(8,11,s));
            }
        });
        homeViewModel.getMainsuoshu().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                int position = s.length() + 7;
                String a = "所属组:   "+s;
                mainsuoshu.setText(tools.SetUnderlineDisplay(7,position,a));
            }
        });
        homeViewModel.getMaintime().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                maintime.setText(tools.SetUnderlineDisplay(8,27,s));
            }
        });
        homeViewModel.getMainmoblienumber().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                mainmoblienumber.setText(tools.SetUnderlineDisplay(0,0,s));
            }
        });

        //对主界面右下角标志监听
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "功能待添加", Snackbar.LENGTH_LONG)
                        .setAction("知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view,"乖~~~~",Snackbar.LENGTH_LONG)
                                        .setAction("text",null).show();
                            }
                        }).show();
            }
        });

        //修改密码监听
        btn_changeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangepwActivity.class);
                startActivity(intent);
            }
        });
        /**
        homeViewModel.getMainnowtime().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                mainnowtime.setText(s);
            }
        });
         **/
        return root;
    }

    //监听右下角按钮
    protected void btn_listenright(){

    }
}
