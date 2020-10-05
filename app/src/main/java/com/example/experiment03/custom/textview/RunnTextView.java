package com.example.experiment03.custom.textview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class RunnTextView extends androidx.appcompat.widget.AppCompatTextView {
    private ValueAnimator mValueAnimator;
    private DecimalFormat mDf;

    public RunnTextView(Context context) {
        this(context,null);
    }
    public RunnTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public RunnTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        //格式化小数（保留小数点后两位）
        mDf = new DecimalFormat("0.00");
        initAnim();
    }
    /**
     * 初始化动画
     */
    private void initAnim() {
        mValueAnimator = ValueAnimator.ofFloat(0,0);//由于金钱是小数所以这里使用ofFloat方法
        mValueAnimator.setDuration(1000);//动画时间为1秒
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if(value>0){//当数值大于0的时候才赋值
                    setText(mDf.format(value));
                }
            }
        });
    }
    /**
     * 设置要显示的金钱
     * @param money
     */
    public void setMoney(float money){
        mValueAnimator.setFloatValues(0,money);//重新设置数值的变化区间
        mValueAnimator.start();//开启动画
    }
    /**
     * 取消动画和动画监听（优化内存）
     */
    public void cancle(){
        mValueAnimator.removeAllUpdateListeners();//清除监听事件
        mValueAnimator.cancel();//取消动画
    }
}
