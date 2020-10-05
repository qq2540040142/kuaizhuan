package com.example.experiment03.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.experiment03.R;
import com.example.experiment03.adapter.MyImageAdapter;
import com.example.experiment03.custom.viewpager.PhotoViewPager;
import com.example.experiment03.tools.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class PhotoViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = PhotoViewActivity.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;        //定义图片下标
    private MyImageAdapter adapter;     //声明适配器
    private TextView mTvSaveImage;
    private List<String> Urls;          //储存图片地址
    private String dir;                 //声明外部储存路径
    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager_photo);
        mTvSaveImage = (TextView) findViewById(R.id.text_saveImage);
        mTvSaveImage.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);

        Urls = new ArrayList<>();
        Urls.add(0,"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2196768770,1349882238&fm=26&gp=0.jpg");

        //new适配器
        adapter = new MyImageAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
            }
        });
        //设置外部储存
        dir =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/kuaizhuan/picture/";

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_saveImage:
                //判断要保存的图片是否存在
                if(imageExist(Urls.get(0).substring(Urls.get(0).length() - 10,Urls.get(0).length()))){
                    tools.miuiToast(this,"重复图片已保存至:"+dir);
                    break;
                }else{
                    // 启动线程
                    new MyThread().start();
                    break;
                }
        }
    }

    //保存网络图片涉及网络请求
    //需要子线程执行
    public class MyThread extends Thread {
        @Override
        public void run() {
            //保存图片至本地
            saveImage();
        }
    }

    //保存图片至本地
    public void saveImage(){
        //获取储存权限
        requestPermission();
        //获取内部存储状态
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Looper.prepare();
            tools.miuiToast(this,"存储状态异常");
            Looper.loop();
            return;
        }
        //创建文件夹
        try {
            File f=new File(dir);
            if(!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            Looper.prepare();
            tools.miuiToast(this,"创建文件夹失败"+e);
            Looper.loop();
        }
        //通过UUID生成字符串文件名
        String fileName = Urls.get(0).substring(Urls.get(0).length() - 10,Urls.get(0).length());
        try {
            //通过URL获取bitmap对象
            mBitmap = getBitmapByUrl(Urls.get(0));
            //设置保存路径
            File file = new File(dir + fileName);
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Looper.prepare();
            tools.miuiToast(this,"保存成功,路径为:"+ dir);
            Looper.loop();
        } catch (Exception e) {
            Looper.prepare();
            tools.miuiToast(this,"保存失败"+e);
            Looper.loop();
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", e.toString());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Looper.prepare();
            tools.miuiToast(getApplicationContext(),"复制成功");
            Looper.loop();
            e.printStackTrace();
        }
    }

    //通过URL获取bitmap对象
    public Bitmap getBitmapByUrl(final String url) {
        URL fileUrl = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            Log.d("shaoace", "1");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            is = null;
        }
        return bitmap;
    }

    //动态获取存储权限
    public void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    //判断当前要保存的图片是否存在
    public boolean imageExist(String file){
        File file1 = new File(dir + file);
        return file1.exists();
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
