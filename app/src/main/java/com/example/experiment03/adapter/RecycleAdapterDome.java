package com.example.experiment03.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experiment03.R;
import com.example.experiment03.activity.TaskDetailsActivity;
import com.example.experiment03.tools.PBombUtil;
import com.qintong.library.InsLoadingView;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

/*
① 创建一个继承RecyclerView.Adapter<VH>的Adapter类
② 创建一个继承RecyclerView.ViewHolder的静态内部类
③ 在Adapter中实现3个方法：
   onCreateViewHolder()
   onBindViewHolder()
   getItemCount()
*/
public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
    private static Context context;
    private List<String> task_title;//任务标题
    private List<BigDecimal> task_money;//任务金额
    private List<String> task_show1;//储存属性一
    private List<String> task_show2;//储存属性二
    private List<Integer> task_CompletedNumber;//储存完成数
    private List<Integer> task_RemainingNumber;//储存剩余数
    private List<Integer> task_Id;//储存任务ID
    private List<String> task_objectId;

    //构造方法，传入数据
    public RecycleAdapterDome(Context context,
                              List<String> task_title,
                              List<BigDecimal> task_money,
                              List<String> task_show1,
                              List<String> task_show2,
                              List<Integer> task_CompletedNumber,
                              List<Integer> task_RemainingNumber,
                              List<Integer> task_Id,
                              List<String> task_objectId) {
        this.context = context;
        this.task_title = task_title;
        this.task_money = task_money;
        this.task_show1 = task_show1;
        this.task_show2 = task_show2;
        this.task_CompletedNumber = task_CompletedNumber;
        this.task_RemainingNumber = task_RemainingNumber;
        this.task_Id = task_Id;
        this.task_objectId = task_objectId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        inflater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传参
                TaskDetailsActivity.setPosition((myViewHolder.getAdapterPosition()));
                Intent intent = new Intent();
                intent.putExtra("objectID",task_objectId.get(myViewHolder.getAdapterPosition()));
                intent.setClass(context,TaskDetailsActivity.class);
                context.startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.te_title.setText(task_title.get(position));
        holder.te_money.setText(String.format("+%s", task_money.get(position).toString()));
        holder.te_show1.setText(task_show1.get(position));
        holder.te_show2.setText(task_show2.get(position));
        holder.te_CompletedNumber.setText(MessageFormat.format("已完成:{0}", task_CompletedNumber.get(position).toString()));
        holder.te_RemainingNumber.setText(MessageFormat.format("剩余数:{0}", task_RemainingNumber.get(position).toString()));
        holder.te_Id.setText(String.format("任务编号:%04d", task_Id.get(position)));
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return task_title.size();
    }

    //设置position
    public void setPosition(int position){
    }
    //内部类，绑定控件
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView te_title,
                te_money,
                te_show1,
                te_show2,
                te_CompletedNumber,
                te_RemainingNumber,
                te_Id;
        InsLoadingView insLoadingView;
        MyViewHolder(View itemView) {
            super(itemView);
            te_title = itemView.findViewById(R.id.task_text_title);
            te_money = itemView.findViewById(R.id.task_text_money);
            te_show1 = itemView.findViewById(R.id.task_text_show1);
            te_show2 = itemView.findViewById(R.id.task_text_show2);
            te_CompletedNumber = itemView.findViewById(R.id.task_Completed_number);
            te_RemainingNumber = itemView.findViewById(R.id.task_Remaining_number);
            te_Id = itemView.findViewById(R.id.task_Id);
            insLoadingView = itemView.findViewById(R.id.loading_view);
            insLoadingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PBombUtil.starics4((Activity) context, 10.0f, 1000L, view, new CountDownTimer(1000,100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() { ;
                            if(insLoadingView.getStatus() == InsLoadingView.Status.LOADING){
                                insLoadingView.setStatus(InsLoadingView.Status.CLICKED);
                                return;
                            }
                            if(insLoadingView.getStatus() == InsLoadingView.Status.CLICKED){
                                insLoadingView.setStatus(InsLoadingView.Status.UNCLICKED);
                                return;
                            }
                            if (insLoadingView.getStatus() == InsLoadingView.Status.UNCLICKED){
                                insLoadingView.setStatus(InsLoadingView.Status.LOADING);
                            }
                        }
                    });
                }
            });
        }
    }
}