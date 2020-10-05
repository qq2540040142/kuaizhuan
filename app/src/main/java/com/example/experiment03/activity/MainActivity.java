package com.example.experiment03.activity;

import android.animation.FloatEvaluator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.experiment03.R;
import com.example.experiment03.custom.slidingmenu.SlidingMenu;
import com.example.experiment03.fragment.home.HomeFragment;
import com.example.experiment03.fragment.main.MainFragment;
import com.example.experiment03.fragment.power.PowerFragment;
import com.example.experiment03.fragment.settings.SettingsFragment;
import com.example.experiment03.fragment.slideshow.SlideshowFragment;
import com.example.experiment03.tools.tools;
import com.example.experiment03.bmob.User;
import com.google.android.material.navigation.NavigationView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private View navframent;
    private boolean mIsExit;//监听返回键
    private SlidingMenu vSlidingMenu;
    private View vContentBg;//黑色遮罩
    private Fragment currentFragment = new Fragment(); //声明全局Fragemnt
    private MainFragment mainfragment;
    private HomeFragment homefragment;
    private SlideshowFragment slideshowfragment;
    private PowerFragment powerfragment;
    private SettingsFragment settingsfragment;

    private FloatEvaluator mAlphaEvaluator;//透明度估值器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_main);

        findview();
        setSidebar();//获取用户信息并展示
        setupSlidingMenu();//设置黑色遮罩
        settop();//根据状态栏高度设置当前布局高度
    }

    //fragment的动态加载
    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
        .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            //加载传入的fragment
            transaction.add(R.id.nav_host_fragment, targetFragment, targetFragment.getClass().getName());
        } else {
            //hide当前fragment
            //show传入的fragment
                transaction
                    .hide(currentFragment)
                    .show(targetFragment);
            }
        //赋值
            currentFragment = targetFragment;
        return transaction;
    }

    //实例化控件
    public void findview(){
        vContentBg = findViewById(R.id.content_bg);
        vSlidingMenu = findViewById(R.id.sliding_menu);
        navframent = findViewById(R.id.nav_host_fragment);
        mainfragment = new MainFragment();
        homefragment = new HomeFragment();
        slideshowfragment = new SlideshowFragment();
        powerfragment =new PowerFragment();
        settingsfragment =new SettingsFragment();

    }
    //侧滑设置
    private void setupSlidingMenu() {
        //创建估值器
        mAlphaEvaluator = new FloatEvaluator();
        //设置侧滑菜单的状态切换监听
        vSlidingMenu.setOnMenuStateChangeListener(new SlidingMenu.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen() {
                //让黑色遮罩，禁用触摸
                vContentBg.setClickable(true);
            }

            @Override
            public void onSliding(float fraction) {
                //拽托菜单开启黑色遮罩
                vContentBg.setVisibility(View.VISIBLE);
                //Log.d(TAG, "菜单拽托中，百分比：" + fraction);
                //设定最小、最大透明度值
                float startValue = 0;
                float endValue = 0.55f;
                //估值当前的透明度值，并设置
                Float value = mAlphaEvaluator.evaluate(fraction, startValue, endValue);
                vContentBg.setAlpha(value);
            }

            @Override
            public void onMenuClose() {
                //Log.d(TAG, "菜单关闭");
                //让黑色遮罩，恢复触摸
                vContentBg.setClickable(false);


            }
        });
    }

     //通过状态栏资源id来获取状态栏高度
    private static int getStatusBarByResId(Context context) {
        int height = 0;
        //获取状态栏资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            try {
                height = context.getResources().getDimensionPixelSize(resourceId);
            } catch (Exception e) {
            }
        }
        return height;
    }
    //根据状态栏高度设置当前布局高度
    private void settop(){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) navframent.getLayoutParams();
        lp.topMargin = getStatusBarByResId(getApplicationContext());
        navframent.setLayoutParams(lp);
    }

    //设置侧边栏
    public void setSidebar(){
        NavigationView mainlayout;
        LayoutInflater layout=this.getLayoutInflater();
        //加载布局文件
        View textEntryView = layout.inflate(R.layout.sidebar_above,null);
        mainlayout = findViewById(R.id.nav_view);

        //添加菜单
        mainlayout.inflateMenu(R.menu.activity_test_drawer);
        //监听menu
        mainlayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_main:
                        //切换界面关闭黑色遮罩
                        vContentBg.setVisibility(View.INVISIBLE);
                        //切换fragment
                        switchFragment(mainfragment).commit();
                        break;
                    case R.id.nav_home:
                        //切换界面关闭黑色遮罩
                        //切换fragment
                        vContentBg.setVisibility(View.INVISIBLE);
                        switchFragment(homefragment).commit();
                        break;
                    case R.id.nav_slideshow:
                        //切换界面关闭黑色遮罩
                        //切换fragment
                        vContentBg.setVisibility(View.INVISIBLE);
                        switchFragment(slideshowfragment).commit();
                        break;
                    case R.id.nav_power:
                        //切换界面关闭黑色遮罩
                        //切换fragment
                        vContentBg.setVisibility(View.INVISIBLE);
                        switchFragment(powerfragment).commit();
                        break;
                    case R.id.nav_setting:
                        //切换界面关闭黑色遮罩
                        //切换fragment
                        vContentBg.setVisibility(View.INVISIBLE);;
                        switchFragment(settingsfragment).commit();
                    default:
                        break;
                }
                return false;
            }
        });
        //增加侧边栏头部布局
        mainlayout.addHeaderView(textEntryView);
        TextView username9 = textEntryView.findViewById(R.id.username);
        TextView shetuan9 =  textEntryView.findViewById(R.id.shetuan);

        //获取信息并展示
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    final User User = BmobUser.getCurrentUser(User.class);
                    username9.setText(String.format("%s   |   %s", User.getUsername(), User.getSuoshu()));

                    //储存获取到的id长度
                    final int[] idlength = new int[1];
                    final SpannableStringBuilder style = new SpannableStringBuilder();
                    style.append("用户ID:   ").append(User.getObjectId());
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            //获取剪贴板管理器：
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", user.getObjectId());
                            // 将ClipData内容放到系统剪贴板里。
                            assert cm != null;
                            cm.setPrimaryClip(mClipData);
                            tools.miuiToast(getApplicationContext(),"复制成功");
                        }
                    };
                    idlength[0] = User.getObjectId().length() + 8;
                    style.setSpan(clickableSpan, 8, idlength[0], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //设置部分文字颜色
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#fafafa"));
                    style.setSpan(foregroundColorSpan, 8, idlength[0], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //设置textview可点击
                    shetuan9.setMovementMethod(LinkMovementMethod.getInstance());
                    shetuan9.setText(style);

                } else {
                    //Toast toast1 = Toast.makeText(mContext,"失败",Toast.LENGTH_LONG);
                    //tools.showMyToast(toast1,5000);
                }
            }
        });
    }

    //监听返回键
    //实现两次退出功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mIsExit) {
                    this.finish();
                } else {
                    tools.miuiToast(getApplicationContext(),"再按一次退出");
                    mIsExit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsExit = false;
                        }
                    }, 2000);
                return true;}
        }
        return super.onKeyDown(keyCode, event);
    }
}
