package com.example.experiment03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment03.R;
import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangepwActivity extends AppCompatActivity {
    private EditText oldpassword,newpassword;
    private Button btn_change;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        //修改密码
        updatePassword();
    }
    /**
     * 提供旧密码修改密码
     */
    private void updatePassword(){
        oldpassword = findViewById(R.id.edit_oldpaasword);
        newpassword = findViewById(R.id.edit_newpassword);
        btn_change = findViewById(R.id.btn_change);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newpassword.getText().toString().isEmpty() == true
                        || oldpassword.getText().toString().isEmpty() == true
                        || newpassword.getText().toString().length() < 7){
                    Snackbar.make(view, "请检查所填内容", Snackbar.LENGTH_LONG).show();
                }else{
                //此处替换为你的旧密码和新密码
                BmobUser.updateCurrentUserPassword(oldpassword.getText().toString(), newpassword.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "修改成功", Snackbar.LENGTH_LONG).show();
                            //修改成功重新登录
                            BmobUser.logOut();
                            Intent intent = new Intent(ChangepwActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(view, "修改失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
            }
        });
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
