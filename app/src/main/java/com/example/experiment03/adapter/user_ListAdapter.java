package com.example.experiment03.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.experiment03.R;
import com.example.experiment03.bmob.User;

import java.util.List;

public class user_ListAdapter extends BaseAdapter {
    private Context context;
    private static List<User> list;
    private static String username,objectId;
    private String showidentity;
    //构造函数
    public user_ListAdapter(Context context, List<User> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public static String getUsername(int position){
        return list.get(position).getUsername();
    }
    public static String getObjectId(int position){
        return list.get(position).getObjectId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            Integer identity;
            //判断用户身份
            identity=list.get(position).getIdentity();
            if(identity == 0){
                showidentity = "非会员";
            }else{
                if(identity == 1){
                    showidentity = "会员";
                }else{
                    showidentity = "null";
                }
            }
            //获取用户名
            username=list.get(position).getUsername();
            //加载list_team布局
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_team, null);//实例化一个布局文件
            TextView textView=convertView.findViewById(R.id.list_text);
            textView.setText(username+"   ||   "+showidentity);

        return convertView;
    }

}
