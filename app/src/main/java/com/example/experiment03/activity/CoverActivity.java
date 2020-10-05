package com.example.experiment03.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment03.R;
import com.example.experiment03.tools.tools;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

public class CoverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置全屏和状态栏透明
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // 设置状态栏为灰色
            getWindow().setStatusBarColor(Color.parseColor("#FAFAFA"));
            // getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_cover);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //初始化Bmob后端
        Bmob.initialize(this, "fcdf7b8fe09e3e8e6ae4a9e8512f0712");
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        DetectUpdates();//检测更新结果
        listenbutton();//更新按钮监听
        BmobUpdateAgent.update(this);

    }
    /**
     判断用户是否登录状态
     判断APP是否联网
     **/
    protected void iflogin(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            Toast toast = Toast.makeText(getApplicationContext(),"无法联网,请联网后重试",Toast.LENGTH_LONG);
            tools.showMyToast(toast,2000);
            finish();
        } else {
            //判断是否登录,已登录就直接跳转至主界面
            //未登录跳转至登录界面
            if (BmobUser.isLogin()) {
                new Handler(new Handler.Callback() {
                    //处理接收到的消息的方法
                    @Override
                    public boolean handleMessage(Message arg0) {
                        //实现页面跳转
                        Intent intent = new Intent(CoverActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500); //表示延时2秒进行任务的执行
            }else {
                new Handler(new Handler.Callback() {
                    //处理接收到的消息的方法
                    @Override
                    public boolean handleMessage(Message arg0) {
                        //实现页面跳转
                        Intent intent = new Intent(CoverActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500); //表示延时2秒进行任务的执行
                Toast toast = Toast.makeText(getApplicationContext(),"用户未登录",Toast.LENGTH_LONG);
                tools.showMyToast(toast,1000);
            }
        }
    }

    //检测更新结果
    protected void DetectUpdates(){
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (updateStatus == UpdateStatus.Yes) {//版本有更新
                    Toast.makeText(getApplicationContext(), "此版本已停用,请立即更新", Toast.LENGTH_LONG).show();
                }else if(updateStatus == UpdateStatus.No){
                    //Toast.makeText(getApplicationContext(), "已经是最新版本了", Toast.LENGTH_SHORT).show();
                    //判断用户是否已经登录过
                    iflogin();
                }else if(updateStatus==UpdateStatus.EmptyField){
                    Toast.makeText(getApplicationContext(), "信息缺失", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //更新按钮监听
    protected void listenbutton(){
        //设置对对话框按钮的点击事件的监听
        BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.Update:
                        openBrowser("https://wws.lanzous.com/queary");
                        break;
                    case UpdateStatus.NotNow:

                        break;
                    case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
                        finish();
                        break;
                }
            }
        });
    }

    //立即更新跳转至浏览器
    public void openBrowser(String url) {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        //指定跳转链接
        intent.setData(content_url);
        startActivity(intent);
    }




}
