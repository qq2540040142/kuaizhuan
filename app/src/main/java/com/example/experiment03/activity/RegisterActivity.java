package com.example.experiment03.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment03.R;
import com.example.experiment03.tools.tools;
import com.example.experiment03.bmob.User;
import com.google.android.material.button.MaterialButton;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity{

    private Context mContext;
    private long mLastClickTime = 0;
    private EditText re_identity,re_username,re_password,re_shetuan;
    public static final long TIME_INTERVAL = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //软键盘填充
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //注册按钮监听
        register();
        MaterialButton b = findViewById(R.id.btn_register1);
        getIconAndContentCenter(b);
    }


    /**
     * 注册按钮监听
     */
    public void register(){
        LinearLayout register = findViewById(R.id.btu_regiest);
        re_username = findViewById(R.id.re_username);
        re_password = findViewById(R.id.re_password);
        re_shetuan = findViewById(R.id.re_shetuan);
        re_identity = findViewById(R.id.text_identity);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查输入限制
                if(check_input() == true){
                final User user = new User();
                long nowTime = System.currentTimeMillis();
                user.setUsername(re_username.getText().toString().trim());
                user.setPassword(re_password.getText().toString().trim());
                user.setSuoshu(re_shetuan.getText().toString().trim());
                user.setIdentity(Integer.parseInt(re_identity.getText().toString().trim()));
                Integer power = 0;
                user.setPower(power);
                //这个判断是防止重复点击
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    mLastClickTime = nowTime;
                    Toast toast = Toast.makeText(mContext,"请等待,添加中",Toast.LENGTH_LONG);
                    tools.showMyToast(toast,1000);
                    //进行注册
                    user.signUp(new SaveListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if (e == null) {
                                            Toast toast = Toast.makeText(mContext,"添加成功",Toast.LENGTH_LONG);
                                            tools.showMyToast(toast,2000);
                                            Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                                            BmobUser.logOut();
                                            startActivity(intent2);
                                            finish();
                                        } else {
                                            Toast toast = Toast.makeText(mContext,"添加失败",Toast.LENGTH_LONG);
                                            tools.showMyToast(toast,500);
                                        }
                                    }
                                });
                }else{
                    Toast.makeText(mContext, "请勿重复点击", Toast.LENGTH_LONG).show();
                }
            }}
        });

    }

    /**
     * 检查输入限制
     */
    public boolean check_input(){
        String username,password,shetuan,moblienumber1,yanzhengma;
        re_username = findViewById(R.id.re_username);
        re_password = findViewById(R.id.re_password);
        re_shetuan = findViewById(R.id.re_shetuan);

        username = re_username.getText().toString();
        password = re_password.getText().toString();
        shetuan = re_shetuan.getText().toString();


        if(username.length() < 8 || username.isEmpty()){
            Toast toast = Toast.makeText(mContext,"请检查用户名",Toast.LENGTH_LONG);
            tools.showMyToast(toast,1000);
            return false;
        }else{
            if(password.length() < 7 || password.isEmpty()){
                Toast toast = Toast.makeText(mContext,"请检查密码,密码不能小于七位呦",Toast.LENGTH_LONG);
                tools.showMyToast(toast,1000);
                return false;
            }else{
                if(shetuan.isEmpty()){
                    Toast toast = Toast.makeText(mContext,"请检查填写的组名",Toast.LENGTH_LONG);
                    tools.showMyToast(toast,1000);
                    return false;
                }
            }
        }
        return true;
    }

    public void getIconAndContentCenter(final MaterialButton btn){
        btn.post(new Runnable() {
            @Override
            public void run() {
                int btnwidth = btn.getWidth();
                btn.measure(0, 0);
                int contentwidth = btn.getMeasuredWidth();
                int paddingleft = ( btnwidth - contentwidth ) / 2; //50 drawable width
                btn.setPadding(paddingleft, 0, 0, 0);
            }
        });
    }
}
