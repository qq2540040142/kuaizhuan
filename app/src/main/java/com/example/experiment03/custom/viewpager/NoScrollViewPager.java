package com.example.experiment03.custom.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    // 定义一个是否可以滑动的boolean 值
    private boolean isCanScroll = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    // 滑动到指定位置
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    // 触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    // 设置当前显示的布局
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }

    // 设置当前显示的布局，并定义滑动方式
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }


    // 拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
