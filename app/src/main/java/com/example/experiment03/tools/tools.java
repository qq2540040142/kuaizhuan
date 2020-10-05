package com.example.experiment03.tools;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {

    //判断Email合法性
    public static boolean isEmail(String email) {
        if (email == null)
            return false;
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(rule);
        matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else
            return false;
    }
    //自定义toast显示时间
    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
    //修复小米手机toast带应用名称问题
    public static void miuiToast(Context context,String text){
        Toast toast = Toast.makeText(context,null,Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }
    //设置textview下划线显示
    public static SpannableStringBuilder SetUnderlineDisplay(int startlength,int endlength, String text){

        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(text);
        //设置下划线
        style.setSpan(new UnderlineSpan(),startlength,endlength,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#fafafa"));
        style.setSpan(foregroundColorSpan, startlength, endlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
}
