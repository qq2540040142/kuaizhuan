package com.example.experiment03.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.experiment03.R;
import com.example.experiment03.bmob.Task;
import com.example.experiment03.bmob.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.experiment03.tools.tools.miuiToast;

public class TaskDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler1;  //获取异步线程数据
    private SwipeRefreshLayout mSwipeRefreshLayout;//刷新控件
    public static int position;        //储存任务下标
    public static String objectId;     //储存查询ID
    private TextView
            title,              //任务标题
            show1,              //属性1
            show2,              //属性2
            money,              //金额
            step1,              //步骤1
            step2,              //步骤2
            taskId,             //任务ID
            taskTime,           //剩余时间
            notice,             //公告注意事项
            CompletedNumber,    //完成任务数
            RemainingNumber,    //剩余任务数
            administrator,      //联系管理员
            PublisherId,        //发布者ID
            PublisherNickname,  //发布者昵称
            TaskState;          //任务状态
    private ImageButton
            iStep1,             //步骤一图片
            iStep2,             //步骤二图片
            iHeadPortrait;      //发布者头像
    private Button taskButton;  //领取任务按钮
    /*函数执行标识*/
    private int frequency = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        initView();
        initData();
        /*加载中...功能实现*/
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("加载中...");
        LoadingDailog dialog = loadBuilder.create();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.hide();
            }
        }, 1500);
        /*第一次进入执行一遍进行判断任务状态*/
        getArSize();//获取MineTask数组
        setHandler();//设置Handler获取数组

    }

    //接受参数并查询后台
    public void initData() {
        Intent intent = getIntent();
        //设置查询ID
        objectId = intent.getStringExtra("objectID");
        //异步查询
        BmobQuery<Task> bmobQuery = new BmobQuery<Task>();
        bmobQuery.getObject(objectId, new QueryListener<Task>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void done(Task object, BmobException e) {
                if (e == null) {
                    //获取成功设置数据
                    title.setText(object.getTitle());
                    show1.setText(object.getShow1());
                    show2.setText(object.getShow2());
                    money.setText(String.format("+%s", object.getMoney().toString()));
                    step1.setText(object.getStep1());
                    step2.setText(object.getStep2());
                    taskId.setText(String.format("任务编号:%04d",object.getId()));
                    //taskTime.setText();
                    notice.setText(object.getNotice());
                    CompletedNumber.setText(String.format("已完成数量%d", object.getCompletedNumber()));
                    RemainingNumber.setText(String.format("剩余数量%d", object.getRemainingNumber()));
                    PublisherNickname.setText(String.format("发布者昵称:%s", object.getPublisherNickname()));
                    PublisherId.setText(String.format("ID:%d", object.getPublisherId()));
                } else {
                    Log.i("bmob:", "查询失败: "+e);
                    //miuiToast(getApplicationContext(), "查询失败：" + e.getMessage());
                }
            }
        });
    }
    //初始化控件
    public void initView(){
        //刷新控件
        mSwipeRefreshLayout = findViewById(R.id.re_freshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        //初始化按钮
        iStep1 = findViewById(R.id.taskDetails_step1Image);
        iStep1.setOnClickListener(this);
        iStep2 = findViewById(R.id.taskDetails_step2Image);
        taskButton = findViewById(R.id.taskDetails_CollectTask);
        taskButton.setOnClickListener(this);


        //初始化TextView
        title = findViewById(R.id.taskDetails_title);
        show1 = findViewById(R.id.taskDetails_show1);
        show2 = findViewById(R.id.taskDetails_show2);
        money = findViewById(R.id.taskDetails_money);
        step1 = findViewById(R.id.taskDetails_step1);
        step2 = findViewById(R.id.taskDetails_step2);
        taskId = findViewById(R.id.taskDetails_taskId);
        taskTime = findViewById(R.id.taskDetails_taskTime);
        notice = findViewById(R.id.taskDetails_Notice);
        CompletedNumber = findViewById(R.id.taskDetails_CompletedNumber);
        RemainingNumber = findViewById(R.id.taskDetails_RemainingNumber);
        administrator = findViewById(R.id.taskDetails_administrator);
        administrator.setOnClickListener(this);
        PublisherNickname = findViewById(R.id.taskDetails_PublisherNickname);
        PublisherId = findViewById(R.id.taskDetails_PublisherId);
        TaskState = findViewById(R.id.taskDetails_taskState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.taskDetails_step1Image:
                Intent intent = new Intent(this, PhotoViewActivity.class);
                startActivity(intent);
                break;
            case R.id.taskDetails_step2Image:

                break;
            case R.id.taskDetails_CollectTask:
                getArSize();//获取MineTask数组
                setHandler();//设置Handler获取数组
                break;
            case R.id.taskDetails_administrator:
                miuiToast(getApplicationContext(),"联系管理员");
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    /**
     * 新增一个对象
    **/
    private void AddBack(List<Integer> MineTask,String objectId) {

        //获取当前任务ID
        BmobQuery<Task> bmobQuery = new BmobQuery<Task>();
            bmobQuery.getObject(objectId, new QueryListener<Task>() {
                @Override
                public void done(Task object, BmobException e) {
                    if (e == null) {
                        /*声明任务ID*/
                        Integer taskId = object.getId();
                        /*声明当前任务领取标识*/
                        Integer CurrentTask = getUserCurrentTask();
                        /*判断当前任务领取标识*/
                        /*为0时*/
                        if(CurrentTask==0) {
                            /*判断当前任务是否已添加至集合中*/
                            if (!isIdExist(MineTask, taskId)) {
                                /*
                                * 未添加操作
                                */
                                if (frequency == 1) {
                                    /*点击按钮时操作*/
                                    User user = new User();
                                    user.setObjectId(getUserObjectId());
                                    /*将当前任务ID添加至MineTask*/
                                    user.setValue("MineTask." + MineTask.size(), taskId);
                                    /*设置CurrentTask为当前任务ID*/
                                    user.setCurrentTask(taskId);
                                    /*设置TaskState为待提交*/
                                    user.setTaskState(1);
                                    user.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                miuiToast(getApplicationContext(), "领取任务成功");
                                                Log.i("bmob", "成功");
                                                TaskState.setText(R.string.TaskState1);
                                                taskButton.setText(R.string.ButtonState1);
                                            } else {
                                                miuiToast(getApplicationContext(), "领取任务失败");
                                                Log.i("bmob", "失败：" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                                if (frequency == 0) {
                                    /*且第一次进入
                                     * 则显示状态为未完成
                                     * */
                                    TaskState.setText(R.string.TaskState0);
                                    frequency = 1;
                                }
                            } else {
                                /*已添加操作*/
                                miuiToast(getApplicationContext(), "当前任务已完成,无法再次领取");
                                taskButton.setBackgroundColor(Color.parseColor("#676767"));
                                taskButton.setEnabled(false);
                                taskButton.setText(R.string.ButtonState4);
                                TaskState.setText(R.string.TaskState4);
                            }
                        }
                        /*与当前任务ID相等时*/
                        if(CurrentTask.equals(taskId)){
                            /*继续判断task_state*/
                            switch (getUserTaskState()){
                                    /*提交操作*/
                                case 1:
                                    if(frequency == 1){
                                        miuiToast(getApplicationContext(),"提交任务操作");
                                    }
                                    if(frequency == 0){
                                        TaskState.setText(R.string.TaskState1);
                                        taskButton.setText(R.string.ButtonState1);
                                        frequency = 1;
                                    }

                                    break;
                                    /*已提交待审核操作*/
                                case 2:
                                    if(frequency == 1){
                                        miuiToast(getApplicationContext(),"已提交待审核操作");
                                    }
                                    if(frequency == 0){
                                        TaskState.setText(R.string.TaskState2);
                                        taskButton.setText(R.string.ButtonState2);
                                        frequency = 1;
                                    }

                                    break;
                                    /*审核未通过重新提交操作*/
                                case 3:
                                    if(frequency == 1){
                                        miuiToast(getApplicationContext(),"审核未通过重新提交操作");
                                    }
                                    if(frequency == 0){
                                        TaskState.setText(R.string.TaskState3);
                                        taskButton.setText(R.string.ButtonState2);
                                        frequency = 1;
                                    }

                                    break;
                                case 4:
                                    /*当前任务已完成操作*/
                                    if(frequency == 1){
                                        miuiToast(getApplicationContext(),"当前任务已完成操作");
                                    }
                                    if(frequency == 0){
                                        TaskState.setText(R.string.TaskState4);
                                        taskButton.setText(R.string.ButtonState4);
                                        frequency = 1;
                                    }

                                    break;
                            }
                        }
                        /*不相等且不等于0,已领取其他任务*/
                        if(!CurrentTask.equals(taskId)& CurrentTask!=0 ){
                            /*判断当前任务ID是否已添加进集合*/
                            if (isIdExist(MineTask, taskId)) {
                                /*已存在,则显示当前任务状态为已完成,无法领取当前任务*/
                                TaskState.setText(R.string.TaskState4);
                                //设置button背景黑色
                                taskButton.setBackgroundColor(Color.parseColor("#676767"));
                                //设置button无法点击
                                taskButton.setEnabled(false);
                                //设置任务状态
                                taskButton.setText(R.string.ButtonState4);
                                miuiToast(getApplicationContext(),"当前任务已完成,无法再次领取");
                            }else{
                                /*不存在,显示当前任务状态为未领取,无法领取当前任务*/
                                TaskState.setText(R.string.TaskState0);
                                //设置button背景黑色
                                taskButton.setBackgroundColor(Color.parseColor("#676767"));
                                //设置button无法点击
                                taskButton.setEnabled(false);
                                miuiToast(getApplicationContext(),"已领取其他任务了哦,无法领取当前任务");
                            }
                        }
                    } else {
                        miuiToast(getApplicationContext(), "获取当前任务ID失败");
                    }
                }
            });
    }
    /**
     * 获取数组
     */
    private void getArSize(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(getUserObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
                    //Log.i("数组长度:", String.valueOf(object.getMineTask().size()));
                    //获取到的数据通过handler传递出去
                            Message msg = new Message();
                            msg.obj = object.getMineTask();
                            handler1.sendMessage(msg);
                }else{
                    Log.i("获取数组失败", "done: "+e);
                }
            }
        });
    }

    /**
     * 获取异步线程数据
     * 处理handle传过来的数据
     */
    @SuppressLint("HandlerLeak")
    private void setHandler(){
        handler1 = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //添加当前任务
                AddBack((List<Integer>) msg.obj,objectId);
            }
        };
    }

    /**
     * 获取当前用户的objectId
     */
    private String getUserObjectId(){
        User user = BmobUser.getCurrentUser(User.class);
        return user.getObjectId();
    }

    /**
     * 获取当前任务领取标识
     */
    private Integer getUserCurrentTask(){
        User user = BmobUser.getCurrentUser(User.class);
        return user.getCurrentTask();
    }
    /**
     * 获取当前任务状态标识
     */
    private Integer getUserTaskState(){
        User user = BmobUser.getCurrentUser(User.class);
        return user.getTaskState();
    }

    /**
     * 判断当前任务ID是否已添加
     */
    private boolean isIdExist(List<Integer> arr, Integer targetValue) {
        for(Integer s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }


    /*接收任务下标*/
    public static void setPosition(int position){
        TaskDetailsActivity.position = position;
    }

    /*加载中功能实现*/
    private void loadDialog(){
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(getApplicationContext())
                .setMessage("加载中...");
        LoadingDailog dialog = loadBuilder.create();
        dialog.show();
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.hide();
            }
        }, 2000);*/
    }
    
    /*监听返回键*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
