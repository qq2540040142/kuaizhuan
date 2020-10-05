package com.example.experiment03.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.experiment03.bmob.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mainusername,mainidenty,mainsuoshu,maintime,mainnowtime,mainmoblienumber;

    public HomeViewModel() {
        mainusername = new MutableLiveData<>();
        mainidenty = new MutableLiveData<>();
        mainsuoshu = new MutableLiveData<>();
        maintime = new MutableLiveData<>();
        mainnowtime = new MutableLiveData<>();
        mainmoblienumber = new MutableLiveData<>();
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    final User User = BmobUser.getCurrentUser(User.class);
                    mainusername.setValue(user.getUsername());
                    mainsuoshu.setValue(User.getSuoshu());
                    //这个判断验证身份信息
                    if(User.getIdentity() == 1){
                        mainidenty.setValue("身份信息:    会员");
                    }else{
                        mainidenty.setValue("身份信息:   非会员");
                    }
                    maintime.setValue("加入时间:   "+user.getCreatedAt());
                    mainmoblienumber.setValue("已绑定的上级ID:   ");
                }
            }
        });
        //mainnowtime.setValue("查询时间:   "+getnowtime());
    }
    //获取当前查询时间
    public String getnowtime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public LiveData<String> getMainusername() {
        return mainusername;
    }

    public LiveData<String> getMainidenty(){
        return mainidenty;
    }
    public LiveData<String> getMainsuoshu(){
        return mainsuoshu;
    }
    public LiveData<String> getMaintime(){
        return maintime;
    }
    public LiveData<String> getMainnowtime(){
        return mainnowtime;
    }
    public LiveData<String> getMainmoblienumber(){
        return mainmoblienumber;
    }
}