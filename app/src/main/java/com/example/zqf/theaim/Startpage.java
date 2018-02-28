package com.example.zqf.theaim;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Startpage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        Bmob.initialize(this, "ec3bba86368b1357bc945565b76b617c");      //初始化正常，可使用

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {                     //Intent跳转

                User user = BmobUser.getCurrentUser(User.class);        //有本地缓存就直接登陆
                if(user != null){
                    Intent mainIntent=new Intent(Startpage.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Intent mainIntent=new Intent(Startpage.this,LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }


            }
        },3000);

    }

    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };  //Toast便捷使用方法
}
