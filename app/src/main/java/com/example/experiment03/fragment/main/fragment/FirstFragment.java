package com.example.experiment03.fragment.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.experiment03.R;
import com.example.experiment03.adapter.RecycleAdapterDome;
import com.example.experiment03.bmob.Task;
import com.example.experiment03.custom.scrollview.MyScrollView;
import com.example.experiment03.tools.tools;
import com.google.android.material.tabs.TabLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FirstFragment extends Fragment {
    private MyScrollView mScrollView;
    private View adview,mStickView;
    //广告viewpager
    private ViewPager view_pager;
    private LinearLayout ll_dotGroup;
    //存储轮播广告的图片
    private int[] imgResIds = new int[]{R.drawable.a, R.drawable.b,R.drawable.c};
    //存储目录
    private int curIndex = 0;
    //用来记录当前滚动的位置
    private PicsAdapter picsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabLayout tabLayout;//TabLayout
    private RecyclerView recyclerView;//声明RecyclerView
    private RecycleAdapterDome adapterDome;//声明适配器
    /**
     * 后端建表储存
     * 每一行一个任务
     * 下方为列数
     * **/
    private List<String> task_title;//储存任务标题
    private List<String> task_objectId;//储存任务查询ID
    private List<BigDecimal> task_money;//储存金钱数额
    private List<String> task_show1;//储存属性一
    private List<String> task_show2;//储存属性二
    private List<Integer> task_CompletedNumber;//储存完成数
    private List<Integer> task_RemainingNumber;//储存剩余数
    private List<Integer> task_Id;//储存任务ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_first,container,false);
        initview(root);//初始化控件
        //initRecycleView(root);//初始化RecycleView
        initTaskData(root);
        initTabLayout();//初始化TabLayout
        setAdapter();//设置广告viewpager适配器
        initPoints(imgResIds.length); // 初始化图片小圆点
        /**
        startAutoScroll(); // 开启自动播放
         **/
        hideScrollView();

        return root;
    }
    //设置viewpage适配器
    private void setAdapter(){
        picsAdapter = new PicsAdapter(); // 创建适配器
        picsAdapter.setData(imgResIds);
        view_pager.setAdapter(picsAdapter); // 设置适配器

        view_pager.setOnPageChangeListener(new MyPageChangeListener()); //设置页面切换监听器
    }
    //设定吸顶的显示与隐藏
    private void hideScrollView(){
        //设定吸顶的显示与隐藏
        mScrollView.setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > adview.getHeight())
                    mStickView.setVisibility(View.VISIBLE);
                else
                    mStickView.setVisibility(View.GONE);
            }
        });
    }

    //初始化控件
    private void initview(View root){
        mStickView = root.findViewById(R.id.tv_stick_view);
        mScrollView = root.findViewById(R.id.scrollView);
        adview = root.findViewById(R.id.AdView);
        view_pager = root.findViewById(R.id.view_pager);
        ll_dotGroup =root.findViewById(R.id.dotgroup);
        tabLayout = root.findViewById(R.id.tab_show);
        mSwipeRefreshLayout = root.findViewById(R.id.main_freshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        tools.miuiToast(getContext(),"刷新");
                    }
                }, 3000);
            }
        });
    }
    //初始化RecycleView
    private void initRecycleView(View root){
        recyclerView = root.findViewById(R.id.Task_recyclerView);
        //设置RecycleView适配器
        //并传入任务数组数据
        adapterDome = new RecycleAdapterDome(getContext(),
                task_title,
                task_money,
                task_show1,
                task_show2,
                task_CompletedNumber,
                task_RemainingNumber,
                task_Id,
                task_objectId);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDome);
    }
    //从后端获取任务数据
    private void initTaskData(View root){

        //从后端获取数据
        BmobQuery<Task> bmobQuery = new BmobQuery<Task>();
        bmobQuery.addQueryKeys("objectId,title,money,show1,show2,CompletedNumber,RemainingNumber,Id");
        bmobQuery.findObjects(new FindListener<Task>() {
            @Override
            public void done(List<Task> object, BmobException e) {
                //查询成功
                if(e==null){
                    //标题
                    task_title = new ArrayList<String>();
                    //金额
                    task_money = new ArrayList<BigDecimal>();
                    //属性一
                    task_show1 = new ArrayList<String>();
                    //属性二
                    task_show2 = new ArrayList<String>();
                    //完成数
                    task_CompletedNumber = new ArrayList<Integer>();
                    //剩余数
                    task_RemainingNumber = new ArrayList<Integer>();
                    //任务ID
                    task_Id = new ArrayList<Integer>();
                    //任务查询ID
                    task_objectId = new ArrayList<String>();
                    for (int i=0;i< object.size();i++){
                        //设置标题
                        task_title.add(object.get(i).getTitle());
                        //设置金额
                        task_money.add(object.get(i).getMoney());
                        //设置属性一
                        task_show1.add(object.get(i).getShow1());
                        //设置属性二
                        task_show2.add(object.get(i).getShow2());
                        //设置完成数
                        task_CompletedNumber.add(object.get(i).getCompletedNumber());
                        //设置剩余数
                        task_RemainingNumber.add(object.get(i).getRemainingNumber());
                        //设置任务ID
                        task_Id.add(object.get(i).getId());
                        //设置任务查询ID
                        task_objectId.add(object.get(i).getObjectId());
                    }
                    //加载RecycleView
                    initRecycleView(root);

                    //tools.miuiToast(getContext(),"查询成功：共" + object.size() + "个任务。");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }
    //初始化TabLayout
    private void initTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText("默认排序"),true);
        tabLayout.addTab(tabLayout.newTab().setText("佣金最高"));
        tabLayout.addTab(tabLayout.newTab().setText("最新发布"));
        tabLayout.setScrollPosition(0,0,false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //第一次点击的操作
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        tools.miuiToast(getContext(),"当前选中为默认排序");
                        break;
                    case 1:
                        tools.miuiToast(getContext(),"当前选中为佣金最高");
                        break;
                    case 2:
                        tools.miuiToast(getContext(),"当前选中为最新发布");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //重复点击后的操作
            //可用于刷新
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        tools.miuiToast(getContext(),"当前选中为默认排序");
                        break;
                    case 1:
                        tools.miuiToast(getContext(),"当前选中为佣金最高");
                        break;
                    case 2:
                        tools.miuiToast(getContext(),"当前选中为最新发布");
                        break;
                }
            }
        });
    }
    // 初始化图片轮播的小圆点和目录
    private void initPoints(int count) {
        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            params.setMargins(0, 0, 20, 0);
            iv.setLayoutParams(params);
            iv.setImageResource(R.drawable.dot1);
            ll_dotGroup.addView(iv);
        }
        ((ImageView) ll_dotGroup.getChildAt(curIndex))
                .setImageResource(R.drawable.dot2);
    }
    /**
    // 自动播放
    private void startAutoScroll() {
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        // 每隔4秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 5,
                4, TimeUnit.SECONDS);
    }

    // 切换图片任务
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int count = picsAdapter.getCount();
                    view_pager.setCurrentItem((curIndex + 1) % count);
                }
            });
        }

    }
**/
    // 定义ViewPager控件页面切换监听器
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) ll_dotGroup.getChildAt(position);
            ImageView imageView2 = (ImageView) ll_dotGroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.drawable.dot2);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.drawable.dot1);
            }
            curIndex = position;
        }
        boolean b = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //实现切换到末尾后返回到第一张
            switch (state) {
                case 1:// 手势滑动
                    b = false;
                    break;
                case 2:// 界面切换中
                    b = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (view_pager.getCurrentItem() == view_pager.getAdapter()
                            .getCount() - 1 && !b) {
                        view_pager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (view_pager.getCurrentItem() == 0 && !b) {
                        view_pager.setCurrentItem(view_pager.getAdapter()
                                .getCount() - 1);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    // 定义ViewPager控件适配器
    class PicsAdapter extends PagerAdapter {

        private List<ImageView> views = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        public void setData(int[] imgResIds) {
            for (int i = 0; i < imgResIds.length; i++) {
                ImageView iv = new ImageView(getContext());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置ImageView的属性
                iv.setImageResource(imgResIds[i]);
                views.add(iv);
            }
        }

        public Object getItem(int position) {
            if (position < getCount())
                return views.get(position);
            return null;
        }

        //判断手势滑动冲突

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {

            if (position < views.size())
                ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(View container, int position) {
            if (position < views.size()) {
                final ImageView imageView = views.get(position);
                ((ViewPager) container).addView(imageView);
                return views.get(position);
            }
            return null;
        }
    }

}
