package com.example.experiment03.fragment.power;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment03.adapter.user_ListAdapter;
import com.example.experiment03.activity.EdituserActivity;
import com.example.experiment03.R;
import com.example.experiment03.activity.RegisterActivity;
import com.example.experiment03.tools.tools;
import com.example.experiment03.bmob.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class PowerFragment extends Fragment {
    private Button btn_setpower,btn_nopower,btn_serachAll,btn_adduser;
    private EditText power_inputID;
    private ListView listView;
    private TextView showusername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final User User = BmobUser.getCurrentUser(User.class);
        //判断是否有顶级管理员权限
        if(User.getPower() == 3) {

            View root = inflater.inflate(R.layout.power,container,false);
            btn_setpower = root.findViewById(R.id.btn_setpower);
            btn_nopower = root.findViewById(R.id.btn_nopower);
            power_inputID=root.findViewById(R.id.power_inputID);
            btn_serachAll=root.findViewById(R.id.btn_serachAll);
            btn_adduser = root.findViewById(R.id.btn_adduser);
            listView = root.findViewById(R.id.AllListView);
            showusername = root.findViewById(R.id.text_showusername);

            btn_setpower.setOnClickListener(new ButtonListener());
            btn_nopower.setOnClickListener(new ButtonListener());
            btn_serachAll.setOnClickListener(new ButtonListener());
            btn_adduser.setOnClickListener(new ButtonListener());
            return root;
        }else {
            Toast toast = Toast.makeText(getApplicationContext(), "没有管理员权限", Toast.LENGTH_LONG);
            tools.showMyToast(toast, 1000);
            return null;
        }

    }

    //按钮的switch监听方法
   private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_setpower:
                    if(TextUtils.isEmpty(power_inputID.getText()) == true){
                        Toast toast = Toast.makeText(getApplicationContext(), "请输入id", Toast.LENGTH_LONG);
                        tools.showMyToast(toast, 1000);
                    }else{
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.getObject(power_inputID.getText().toString().trim(), new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            //判断输入的id是否有误
                            if(e==null){
                                //判断当前管理员权限的值
                                //只能为1或者为0
                                if(user.getPower() == 1){
                                    btn_setpower.setEnabled(false);
                                    Toast toast = Toast.makeText(getApplicationContext(), "当前id已是管理员", Toast.LENGTH_LONG);
                                    tools.showMyToast(toast, 1000);

                                }else{
                                    //设定传参
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("userId",power_inputID.getText().toString().trim());
                                        jsonObject.put("changePower","+1");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                                    //第一个参数是云函数的方法名称
                                    //第二个参数是上传到云函数的参数列表（JSONObject cloudCodeParams)
                                    //第三个参数是回调类
                                    ace.callEndpoint("set_power", jsonObject, new CloudCodeListener() {
                                        @Override
                                        public void done(Object object, BmobException e) {
                                            if (e == null) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "设置管理员成功", Toast.LENGTH_LONG);
                                                tools.showMyToast(toast, 1000);
                                                btn_nopower.setEnabled(true);
                                                btn_setpower.setEnabled(false);
                                            } else {
                                                Toast toast = Toast.makeText(getApplicationContext(), "设置管理员失败"+e, Toast.LENGTH_LONG);
                                                tools.showMyToast(toast, 1000);
                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "id有误", Toast.LENGTH_LONG);
                                tools.showMyToast(toast, 1000);
                            }
                        }
                    });}
                    break;
                case R.id.btn_nopower:
                    if(TextUtils.isEmpty(power_inputID.getText()) == true){
                        Toast toast = Toast.makeText(getApplicationContext(), "请输入id", Toast.LENGTH_LONG);
                        tools.showMyToast(toast, 1000);
                    }else{
                    BmobQuery<User> bmobQuery1 = new BmobQuery<User>();
                    bmobQuery1.getObject(power_inputID.getText().toString().trim(), new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            //判断输入的id是否有误
                            if (e == null){
                                //判断当前管理员权限的值
                                //只能为1或者为0
                                if (user.getPower() == 0) {
                                    btn_nopower.setEnabled(false);
                                    Toast toast = Toast.makeText(getApplicationContext(), "当前id不是管理员,不用取消哟", Toast.LENGTH_LONG);
                                    tools.showMyToast(toast, 1000);
                                } else {
                                    //设定传参
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("userId", power_inputID.getText().toString().trim());
                                        jsonObject.put("changePower", "-1");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                                    //第一个参数是云函数的方法名称
                                    //第二个参数是上传到云函数的参数列表（JSONObject cloudCodeParams)
                                    //第三个参数是回调类
                                    ace.callEndpoint("set_power", jsonObject, new CloudCodeListener() {
                                        @Override
                                        public void done(Object object, BmobException e) {
                                            if (e == null) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "取消管理员成功", Toast.LENGTH_LONG);
                                                tools.showMyToast(toast, 1000);
                                                btn_nopower.setEnabled(false);
                                                btn_setpower.setEnabled(true);
                                            } else {
                                                Toast toast = Toast.makeText(getApplicationContext(), "取消管理员失败" + e, Toast.LENGTH_LONG);
                                                tools.showMyToast(toast, 1000);
                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "id有误", Toast.LENGTH_LONG);
                                tools.showMyToast(toast, 1000);
                            }
                        }
                    });}
                    break;
                case R.id.btn_serachAll:
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.addQueryKeys("username,Identity");
                    bmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if(e==null){
                                showusername.setText("用户名");
                                //设置listview
                                listView.setAdapter(new user_ListAdapter(getApplicationContext(),object));
                                listView.setOnItemClickListener(new ListViewListener());
                                tools.miuiToast(getApplicationContext(),"查询成功：共" + object.size() + "个用户。");
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                    break;
                case R.id.btn_adduser:
                    //跳转界面
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
       }
   }
   //listview监听方法
    private class ListViewListener implements AdapterView.OnItemClickListener{

       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //从AllAdapter获取数据
           String objectId = user_ListAdapter.getObjectId(position);
           //传递给Edituser
           EdituserActivity.setObjectid(objectId);
           //跳转界面
           Intent intent = new Intent(getApplicationContext(), EdituserActivity.class);
           startActivity(intent);
       }
   }
}

