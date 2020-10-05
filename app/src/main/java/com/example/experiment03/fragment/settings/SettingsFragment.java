package com.example.experiment03.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment03.activity.AboutActivity;
import com.example.experiment03.activity.GetidActivity;
import com.example.experiment03.activity.LoginActivity;
import com.example.experiment03.R;

import cn.bmob.v3.BmobUser;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private TextView get_ID,about,exit;
    private View root,view1,view2,view3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings,container,false);
        initialization();
        return root;
    }
    private void initialization(){
        view1 = root.findViewById(R.id.settings_1);
        view1.setOnClickListener(this);
        get_ID = view1.findViewById(R.id.text_settings);
        get_ID.setText("获取ID");
        view2 = root.findViewById(R.id.settings_2);
        view2.setOnClickListener(this);
        about = view2.findViewById(R.id.text_settings);
        about.setText("关于快赚");
        view3 = root.findViewById(R.id.settings_3);
        view3.setOnClickListener(this);
        exit = view3.findViewById(R.id.text_settings);
        exit.setText("退出登录");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_1:
                //获取ID
                Intent intent3 = new Intent(getActivity(), GetidActivity.class);
                startActivity(intent3);
                break;
            case R.id.settings_2:
                //关于界面
                Intent intent1 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.settings_3:
                //退出登录
                BmobUser.logOut();
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                getActivity().finish();
                break;
            default:
                break;
        }

    }
}
