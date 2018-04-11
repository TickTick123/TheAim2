package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

import static com.example.zqf.theaim.PersonalCenterActivity.personalCenterActivity;

public class UserImformationActivity extends AppCompatActivity {

    User user;

    private TextView psd_change;
    private TextView kefu;
    private EditText id_change;
    private EditText mail_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_imformation);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头

        user = BmobUser.getCurrentUser(User.class);
        id_change = findViewById(R.id.change_id);
        mail_change = findViewById(R.id.change_mail);
        psd_change = findViewById(R.id.change_password);
        kefu = findViewById(R.id.kefu);



        id_change.setText(user.getUsername()+"");
        mail_change.setText(user.getEmail()+"");




        //编辑昵称
        id_change.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setUsername(id_change.getText().toString());

//                user.setSessionToken(user.getSessionToken());
//                user.updateCurrentUserPassword("yy", "yyy", new UpdateListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if(e==null){
//                            toast("密码修改成功");
//                        }else{
//                            toast("原密码错误"+e.toString() );
//                        }
//                    }
//                });
            }
        });

        //客服
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:13186618559"));
                startActivity(intent);
            }
        });

        //编辑邮箱
        mail_change.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setEmail(mail_change.getText().toString());
            }
        });

      //  更改密码
        psd_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(UserImformationActivity.this).inflate(R.layout.password_change_view,null);
                //找到并对自定义布局中的控件进行操作的示例
                final EditText used = (EditText) view.findViewById(R.id.used_pwd);
                used.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                final EditText new1 = (EditText) view.findViewById(R.id.new_pwd);
                new1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                final EditText new2 = (EditText) view.findViewById(R.id.confirm_new_pwd);
                new2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


                //创建对话框
                AlertDialog dialog = new AlertDialog.Builder(UserImformationActivity.this).create();
//                dialog.setIcon(R.mipmap.ic_launcher);//设置图标
                dialog.setTitle("修改密码");//设置标题
                dialog.setView(view);//添加布局
                //设置按键
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        //BmobUser.updateCurrentUserPassword(used.getText().toString(),new1.getText().toString(), new UpdateListener() {
                         BmobUser.updateCurrentUserPassword("yy",new1.getText().toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    toast("密码修改成功，可以用新密码进行登录啦");
                                }else{
                                    toast("失败:" + e.getMessage());
                                }
                            }

                        });
                }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(TextUtils.isEmpty(id_change.getText())){

                }else if(!TextUtils.isEmpty(id_change.getText())){
                    if(id_change.getText().equals(user.getUsername()) && mail_change.getText().equals(user.getEmail())){

                    }else if(!id_change.getText().equals(user.getUsername()) && mail_change.getText().equals(user.getEmail())){
                        user.setUsername(id_change.getText().toString());
                    }else if(id_change.getText().equals(user.getUsername()) && !mail_change.getText().equals(user.getEmail().toString())){
                        user.setEmail(mail_change.getText().toString());
                    }else if(id_change.getText().equals(user.getUsername()) && mail_change.getText().equals(user.getEmail())){
                        user.setUsername(id_change.getText().toString());
                        user.setEmail(mail_change.getText().toString());
                    }
                }
//                user.setDoscheduleNumber(0);
//                user.setScheduleNumber(0);
                SaveUserRecord(user.getObjectId(),user);
                this.finish();
                super.finish();
                personalCenterActivity.finish();
                Intent i = new Intent(UserImformationActivity.this,PersonalCenterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",id_change.getText().toString());
                bundle.putString("mail",mail_change.getText().toString());
                i.putExtras(bundle);
                startActivity(i);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };

    //用户user更新
    public void SaveUserRecord(String id,User user){
        user.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //  Toast.makeText(getApplication(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
