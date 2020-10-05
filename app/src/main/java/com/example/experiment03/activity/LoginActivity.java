package com.example.experiment03.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment03.R;
import com.example.experiment03.bmob.User;
import com.example.experiment03.tools.tools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 2000L;
    private EditText username,password;
    private CheckBox saved;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        saved = findViewById(R.id.check_save);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        //免登录
        //Nologin();
        //登录按钮实现
        loginbtn();
        //密码替换为*号显示
        Listenpassword();
        SharedPreferences sp2=getSharedPreferences("Logindb",MODE_PRIVATE);
        if(sp2.getBoolean("save", false)){    //判断是否写入了数值save==true
            getDB();
            saved.setChecked(true);
        }
    }

    //监听登录按钮
    public void loginbtn(){
        LinearLayout register = findViewById(R.id.btu_login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btu_login:
                        if(saved.isChecked()){                    //当多选按钮按下时执行报损数据
                            saveDB();
                        }
                        else {
                            clearDB();
                        }
                        login();
                        break;
                }
            }
        });
    }

    //登录
    private void login(){

        //检查是否有空
        if(check_input()){
            User user = new User();
            //此处替换为你的用户名
            user.setUsername(username.getText().toString().trim());
            //此处替换为你的密码
            user.setPassword(password.getText().toString().trim());
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL){
                mLastClickTime = nowTime;
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User bmobUser, BmobException e) {
                        if (e == null) {
                            tools.miuiToast(getApplicationContext(),"登录成功");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG,"done:",e);
                            tools.miuiToast(getApplicationContext(),"登录失败" + e);
                        }
                    }
                });}else{
                tools.miuiToast(getApplicationContext(),"请等待两秒再点击");
            }
        }
    }

    //监听登录密码框
    public void Listenpassword(){
        EditText password = findViewById(R.id.password1);
        //设置密码转换
        password.setTransformationMethod(new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new PasswordCharSequence(source);
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }

        });
    }

    //检查输入
    public boolean check_input(){
        String getusername,getpassword;
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);

        getusername = username.getText().toString();
        getpassword = password.getText().toString();

        if(getusername.length() < 8 || getusername.isEmpty()){
            username.setError("请检查用户名");
            return false;
        }else{
            if(getpassword.length() < 7 || getpassword.isEmpty()){
                password.setError("请检查密码");
                return false;
            }
        }
        return true;
    }

    //将密码转换成*显示
    private static class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            //这里返回的char，就是密码的样式，注意，是char类型的
            return '*';
        }

        public int length() {
            return mSource.length();
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }

    //判断Android ID直接免登录
    private void Nologin(){
        String id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String Presupposition1 = "fba672707d901f04";
        String username1 = "2540040142";
        String password1 = "1234567";
        if(Presupposition1.equals(id)){
          nologin(username1,password1);
        }else{
            Toast toast = Toast.makeText(mContext,"免登录失败,获取的id:" + id + "作比较的id: "+ Presupposition1,Toast.LENGTH_LONG);
            tools.showMyToast(toast,5000);
        }
    }
    public void nologin(String setusername,String setpassword){
        User user = new User();
        //此处替换为你的用户名
        user.setUsername(setusername);
        //此处替换为你的密码
        user.setPassword(setpassword);
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > TIME_INTERVAL){
            mLastClickTime = nowTime;
            user.login(new SaveListener<User>() {
                @Override
                public void done(User bmobUser, BmobException e) {
                    if (e == null) {
                        Toast toast = Toast.makeText(mContext,"免登录成功",Toast.LENGTH_LONG);
                        tools.showMyToast(toast,500);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(mContext,"免登录失败",Toast.LENGTH_LONG);
                        tools.showMyToast(toast,5000);
                    }
                }
            });
        }
    }
    
    //清除
    private void clearDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }
    //保存数据
    private void saveDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("loginEdt",username.getText().toString());
        editor.putString("passwordEdt",password.getText().toString());
        editor.putBoolean("save",true);
        editor.commit();            //写入数据
        //Toast.makeText(LoginActivity.this,"sd",Toast.LENGTH_LONG).show();
    }
    //读取数据
    private void getDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        String name= sp.getString("loginEdt","");
        String password1=sp.getString("passwordEdt","");
        username.setText(name);
        password.setText(password1);
    }

}
