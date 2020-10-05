package com.example.experiment03.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment03.R;
import com.example.experiment03.tools.tools;

public class GetidActivity extends AppCompatActivity {
    private TextView showid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);
        showid = findViewById(R.id.text_get_id);
        String id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //储存获取到的id长度
        final int[] idlength = new int[1];
        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(id);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label",id);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                tools.miuiToast(getApplicationContext(),"复制成功");
            }
        };
        idlength[0] = id.length();
        style.setSpan(clickableSpan, 0, idlength[0], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#fafafa"));
        style.setSpan(foregroundColorSpan, idlength[0], idlength[0], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置textview可点击
        showid.setMovementMethod(LinkMovementMethod.getInstance());
        showid.setText(style);
    }
    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
