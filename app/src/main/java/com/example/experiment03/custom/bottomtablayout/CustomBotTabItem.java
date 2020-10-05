package com.example.experiment03.custom.bottomtablayout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.experiment03.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class CustomBotTabItem {

        private TabLayout mTabLayout;
        private ViewPager mViewPager;
        private Context mContext;
        private int mSwitchId;//储存切换图片的id
        //底部Tab标题
        private final String[] mTitles = {"主页", "推荐", "推广","我的"};
        private final int[] imageid = {
                R.drawable.bottom_tablayout_main,R.drawable.bottom_tablayout_main1,
                R.drawable.bottom_tablayout_recommend,R.drawable.bottom_tablayout_recommend1,
                R.drawable.bottom_tablayout_extension,R.drawable.bottom_tablayout_extension1,
                R.drawable.botton_tablayout_mine,R.drawable.bottom_tablayout_mine1};
        //返回CustomBotTabItem实例
        public static CustomBotTabItem create() {
            return TabItemHolder.sCustomTabItem;
        }
        //创建CustomBotTabItem实例
        private static class TabItemHolder {
            private static CustomBotTabItem sCustomTabItem = new CustomBotTabItem();
        }
        //引入布局需要的Context
        public CustomBotTabItem setContext(Context context) {
            mContext = context;
            return this;
        }
        //需要自定义的TabLayout
        public CustomBotTabItem setTabLayout(TabLayout tabLayout) {
            mTabLayout = tabLayout;
            return this;
        }
        //设置与TabLayout关联的ViewPager
        public CustomBotTabItem setViewPager(ViewPager viewPager) {
            mViewPager = viewPager;
            return this;
        }
        //创建Tab
        public CustomBotTabItem build() {
            initTabLayout();
            return this;
        }
        //初始化Tab
        private void initTabLayout() {
            mTabLayout.setupWithViewPager(mViewPager);
            //第二个参数为selector，下同
            Objects.requireNonNull(mTabLayout.getTabAt(0))
                    .setCustomView(getTabView(0, imageid[0]));
            Objects.requireNonNull(mTabLayout.getTabAt(1))
                    .setCustomView(getTabView(1, imageid[2]));
            Objects.requireNonNull(mTabLayout.getTabAt(2))
                    .setCustomView(getTabView(2, imageid[4]));
            Objects.requireNonNull(mTabLayout.getTabAt(3))
                    .setCustomView(getTabView(3, imageid[6]));
            tabSelectListener();
        }
        //自定义Tab样式
        private View getTabView(final int position, int resId) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_tab_item, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.id_tv_title);
            final ImageView ivTitle = (ImageView) view.findViewById(R.id.id_iv_title);
            ivTitle.setImageResource(resId);
            tvTitle.setText(mTitles[position]);
            //默认第一个tab选中，设置字体为选中色
            if (position == 0) {
                tvTitle.setTextColor(Color.parseColor("#f94c51"));
                ivTitle.setImageResource(R.drawable.bottom_tablayout_main1);
            } else {
                tvTitle.setTextColor(Color.parseColor("#262a3b"));
            }
            //点击Tab切换
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(position);
                }
            });
            return view;
        }

        //Tab监听
        private void tabSelectListener() {
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    changeTabStatus(tab, true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    changeTabStatus(tab, false);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
        //切换Tab变换文字颜色
        //切换Tab变换图片
        private void changeTabStatus(TabLayout.Tab tab, boolean selected) {
            View view = tab.getCustomView();
            if (view == null) {
                return;
            }
            TextView tvTitle = view.findViewById(R.id.id_tv_title);
            ImageView tvImageView = view.findViewById(R.id.id_iv_title);
            if (selected) {
                //计算要切换的图片id
                mSwitchId = tab.getPosition()*2 + 1;
                tvTitle.setTextColor(Color.parseColor("#f94c51"));
                tvImageView.setImageResource(imageid[mSwitchId]);
            } else {
                //计算要切换的图片id
                mSwitchId = tab.getPosition()*2;
                tvTitle.setTextColor(Color.parseColor("#262a3b"));
                tvImageView.setImageResource(imageid[mSwitchId]);
            }

        }
}
