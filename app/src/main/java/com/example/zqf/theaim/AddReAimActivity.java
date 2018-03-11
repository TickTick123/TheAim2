package com.example.zqf.theaim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddReAimActivity extends AppCompatActivity {
    private EditText recontent;
    private EditText repoint;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_re_aim);
        recontent=(EditText)findViewById(R.id.re_content);
        repoint=(EditText)findViewById(R.id.re_costpoint);
        submit=(Button)findViewById(R.id.re_commit);

        submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){

                Reward p2 = new Reward();                       //添加奖励
                User user = BmobUser.getCurrentUser(User.class);
                p2.setMaster(user);

                if(recontent.getText().toString().equals("")||repoint.getText().toString().equals("")){
                    toast("请输入奖励内容和所需奖励点");
                }
                else {
                    p2.setContent(recontent.getText().toString());
                    p2.setCostpoint(Integer.parseInt(repoint.getText().toString()));
                    p2.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                toast("添加数据成功，返回objectId为：" + objectId);
                            } else {
                                toast("创建数据失败：" + e.getMessage());
                            }
                        }
                    });

                    finish();
                }

            }
        });

    }

    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }
}
