package com.example.experiment03.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.experiment03.bmob.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class EdituserActivity extends AppCompatActivity {
    private static String objectid;
    private TextView text_name,text_id,text_moblienumber,text_identuty,text_suoshu,text_jointime,text_power;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituser);

        text_name = findViewById(R.id.text_editshowname);
        text_id = findViewById(R.id.text_editshowid);
        text_moblienumber = findViewById(R.id.text_editshowmoblienumber);
        text_identuty = findViewById(R.id.text_editshowidentity);
        text_suoshu = findViewById(R.id.text_editshowsuoshu);
        text_jointime = findViewById(R.id.text_editshowjointime);
        text_power = findViewById(R.id.text_editshowpower);

        //设置textview显示
        //储存获取到的id长度
        final int idlength;
        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append("当前用户ID: "+objectid);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", objectid);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                tools.miuiToast(getApplicationContext(),"复制成功");
            }
        };
        idlength = objectid.length() + 8;
        style.setSpan(clickableSpan, 8, idlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FFF59D"));
        style.setSpan(foregroundColorSpan, 8, idlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置textview可点击
        text_id.setMovementMethod(LinkMovementMethod.getInstance());
        text_id.setText(style);
        setAllText();
    }

    //查询并设置文字
    protected void setAllText(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(objectid, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    //设置用户名
                    text_name.setText(user.getUsername()+" 的个人详情页");
                    //设置手机号
                    text_moblienumber.setText("已绑定的上级ID: 无");
                    //设置身份信息
                    text_identuty.setText("当前身份代号: "+ user.getIdentity());
                    //设置所属组
                    text_suoshu.setText("所属: "+ user.getSuoshu());
                    //设置加入时间
                    text_jointime.setText("加入时间: "+ user.getCreatedAt());
                    //设置权限信息
                    text_power.setText("当前权限代号: " + user.getPower());
                }else{
                    //设置用户名
                    text_name.setText("NULL");
                    //设置手机号
                    text_moblienumber.setText("NULL");
                    //设置身份信息
                    text_identuty.setText("NULL");
                    //设置所属组
                    text_suoshu.setText("NULL");
                    //设置加入时间
                    text_jointime.setText("NULL");
                    //设置权限信息
                    text_power.setText("NULL");
                }
            }
        });
    }

    public static void setObjectid(String transmit){
        objectid = transmit;
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
