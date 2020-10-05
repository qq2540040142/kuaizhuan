package com.example.experiment03.fragment.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment03.R;
import com.example.experiment03.custom.textview.RunnTextView;

public class SecondFragment extends Fragment {
    private RunnTextView textView;
    private EditText editText;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_second,container,false);
        textView = root.findViewById(R.id.text_money);
        editText = root.findViewById(R.id.edit_money);
        button = root.findViewById(R.id.btn_setmoney);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setMoney(Float.parseFloat(editText.getText().toString()));
            }
        });
        return root;
    }
}
