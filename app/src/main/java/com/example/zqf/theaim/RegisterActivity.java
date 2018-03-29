package com.example.zqf.theaim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button regButton;
    private BmobFile bmobFile = new BmobFile("head.jpg","","http://bmob-cdn-17620.b0.upaiyun.com/2018/03/25/4cbb16e5404ef8e880e376e5702c4945.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        name =(EditText) findViewById(R.id.editText_register_name);             //初始化
        password=(EditText)findViewById(R.id.editText_register_password);
        regButton=(Button)findViewById(R.id.button_register_Reg);


        regButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){                        //注册按钮相应

                User user = new User();                   //  bmob注册
                user.setUsername(name.getText().toString());
                user.setPassword(password.getText().toString());
                user.setPicUser(bmobFile);
                user.setRewardpoint(0);
                user.setScheduleNumber(0);
                user.setDoscheduleNumber(0);

                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User s, BmobException e) {
                        if(e==null){
                            toast("注册成功");
                            finish();
                        }
                        if(e.getErrorCode()==202)
                            toast("该用户名已被占用，请输入一个新的用户名");       //异常处理1
                        if(e.getErrorCode()==304)
                            toast("用户名和密码不能为空");                    //异常处理2
                    }
                });

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty, menu);                           //添加菜单项
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };
}
