package com.example.experiment03.custom.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyScrollView extends ScrollView {
    private static View view;


    public interface ScrollViewListener {
        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public static class NoScrollViewPager extends ViewPager {
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
            super.setCurrentItem(item, false);
        }

        // 设置当前显示的布局，并定义滑动方式
        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
            super.setCurrentItem(item, smoothScroll);
        }
        // 父容器拦截除了ACTION_DOWN的其他事件，不拦截ACTION_DOWN是为了让child可以接受ACTION_DOWN事件并处理。拦截其他事件是为了，child请求拦截的时候onInterceptTouchEvent返回TRUE实现真正拦截
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                return false;
            } else {
                return true;
            }
        }

    }
}
