package com.example.zqf.theaim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText phone;
    private EditText password,code;
    private Button codeRutton,changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phone =(EditText) findViewById(R.id.editText_forget_number);             //初始化
        code=(EditText)findViewById(R.id.editText_forget_code);
        codeRutton=(Button)findViewById(R.id.button_forget_code);
        password=(EditText)findViewById(R.id.editText_forget_password);
        changeButton=(Button)findViewById(R.id.button_forget_reg);

        codeRutton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){                //发送验证码
                toast("已发送验证码");
                BmobSMS.requestSMSCode(phone.getText().toString(),"aim", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        if(ex==null){//验证码发送成功
                            Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
                        }else{
                            toast(ex.getMessage());
                        }
                    }
                });

            }
        });

        changeButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){                        //忘记密码按钮相应


                if(password.getText().toString().equals(""))
                    toast("密码不能为空");
                else{
                    BmobUser.resetPasswordBySMSCode(code.getText().toString(),password.getText().toString(),new UpdateListener() {
                        @Override
                        public void done(BmobException ex) {
                            if(ex==null){
                                toast("密码重置成功");
                                Log.i("smile", "密码重置成功");
                                finish();
                            }else{
                                toast("密码重置失败");
                                Log.i("smile", "重置失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                            }
                        }
                    });
                }


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
    }
}
