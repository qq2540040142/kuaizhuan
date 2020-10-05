package com.example.experiment03.fragment.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.experiment03.R;
import com.example.experiment03.tools.tools;
import com.example.experiment03.bmob.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private LinearLayout serchbtu;
    private EditText serach_ID;
    private TextView serach_showID,serach_username,serach_identity,serach_suoshu,serach_jointime,serach_moblienumber;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        serach_ID = root.findViewById(R.id.serch_inputID);
        serchbtu = root.findViewById(R.id.btu_serch);
        serach_showID = root.findViewById(R.id.search_showID);
        serach_username = root.findViewById(R.id.serach_username);
        serach_identity = root.findViewById(R.id.serach_identity);
        serach_suoshu = root.findViewById(R.id.serach_suoshu);
        serach_jointime = root.findViewById(R.id.serach_jointime);
        serach_moblienumber = root.findViewById(R.id.serach_moblienumber);
        /**
         监听查询按钮
        查询单条数据
         **/
        serchbtu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (checknull() == true){
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.getObject(serach_ID.getText().toString().trim(), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        //这个判断验证是否有管理员权限
                        final User user1 = BmobUser.getCurrentUser(User.class);
                        if (user1.getPower() == 1 || user1.getPower() == 3) {
                            if (e == null) {
                                serach_showID.setText("查询的ID:   " + serach_ID.getText().toString().trim());
                                serach_username.setText("用户名:   " + user.getUsername());
                                //这个判断验证身份信息
                                if (user.getIdentity() == 1) {
                                    serach_identity.setText("身份信息:   会员");
                                } else {
                                    serach_identity.setText("身份信息:   非会员");
                                }
                                serach_suoshu.setText("所属组:   " + user.getSuoshu());
                                serach_jointime.setText("加入时间   " + user.getCreatedAt());
                                serach_moblienumber.setText("已绑定上级ID: ");
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_LONG);
                                tools.showMyToast(toast, 1000);
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "没有管理员权限", Toast.LENGTH_LONG);
                            tools.showMyToast(toast, 1000);
                        }
                    }
                    });
                }
            }
        });
        return root;
    }
    //检查id是否为空
    protected boolean checknull(){
        if(serach_ID.getText().toString().length() == 0 || serach_ID.getText().toString().isEmpty() == true){
            Toast toast = Toast.makeText(getApplicationContext(), "请检查id", Toast.LENGTH_LONG);
            tools.showMyToast(toast, 1000);
            return false;
        }else {
            return true;
        }
    }
}
