package com.example.experiment03.fragment.main.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment03.R;
import com.example.experiment03.tools.PBombUtil;
import com.example.experiment03.tools.tools;
import com.qintong.library.InsLoadingView;

public class ThirdFragment extends Fragment {
    private InsLoadingView insLoadingView;
    private Button btn_1,btn_2,btn_3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_third,container,false);
        insLoadingView = root.findViewById(R.id.loading_view);
        btn_1 = root.findViewById(R.id.btn_1);
        btn_2 = root.findViewById(R.id.btn_2);
        btn_3 = root.findViewById(R.id.btn_3);
        btn_1.setOnClickListener(this::onClick);
        btn_2.setOnClickListener(this::onClick);
        btn_3.setOnClickListener(this::onClick);
        insLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PBombUtil.starics4(getActivity(), 10.0f, 1000L, view, new CountDownTimer(1000,100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        tools.miuiToast(getContext(),"点击");
                    }
                });
            }
        });
        return root;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                insLoadingView.setStatus(InsLoadingView.Status.LOADING);
                break;
            case R.id.btn_2:
                insLoadingView.setStatus(InsLoadingView.Status.UNCLICKED);
                break;
            case R.id.btn_3:
                insLoadingView.setStatus(InsLoadingView.Status.CLICKED);
                break;
            default:
                break;
        }
    }
}
