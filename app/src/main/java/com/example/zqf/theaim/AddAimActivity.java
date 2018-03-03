package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Schedule;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddAimActivity extends AppCompatActivity {

    private TextView textView;
    private EditText title;
    private EditText describe;
    private ImageButton pointbtn;
    private  ImageButton state;
    Schedule schedule = new Schedule();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bmob .initialize(this,"952c699bad14188ea5ef2af20da327b6");
        if(requestCode == 0x11 && resultCode == 0x11){
            Bundle bundle = data.getExtras();
            String t = bundle.getString("time");
            if(TextUtils.isEmpty(t)){
                textView.setText(bundle.getInt("year")+"年"+bundle.getInt("month")+"月"+bundle.getInt("day")+"日");
            }else {
                textView.setText(bundle.getInt("year") + "年" + bundle.getInt("month") + "月" + bundle.getInt("day") + "日"+ bundle.getString("time") );
                schedule.setTime(t);
            }
            schedule.setYear(bundle.getInt("year"));
            schedule.setMouth(bundle.getInt("month"));
            schedule.setDay(bundle.getInt("day"));
            SaveRecord(schedule.getObjectId(),schedule);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob .initialize(this,"ec3bba86368b1357bc945565b76b617c");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头
        //actionBar.setHomeAsUpIndicator(R.drawable.back); //修改actionbar左上角返回按钮的图标
        setContentView(R.layout.activity_add_aim);

        textView = (TextView)findViewById(R.id.aim_time);

        title = findViewById(R.id.aim_title);
        describe = findViewById(R.id.aim_describe);
        describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                schedule.setDecribe(describe.getText().toString());
                SaveRecord(schedule.getObjectId(),schedule);
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                schedule.setContent(title.getText().toString());
                SaveRecord(schedule.getObjectId(),schedule);
            }
        });



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAimActivity.this,SetTimeActivity.class);
                startActivityForResult(intent,0x11);
            }
        });

        pointbtn = (ImageButton) findViewById(R.id.point_btn);
        pointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strArray = new String[]{"3分","2分","1分","0分"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddAimActivity.this);//实例化builder
                builder.setIcon(R.mipmap.ic_launcher);//设置图标
                builder.setTitle(R.string.points);//设置标题

                //设置单选列表
                builder.setSingleChoiceItems(strArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String point = null;
                        if(strArray[which].equals("3分")){
                            pointbtn.setBackgroundResource(R.drawable.three_point);
                            schedule.setRewardpoint("3");
                        }else if(strArray[which].equals("2分")){
                            pointbtn.setBackgroundResource(R.drawable.two_point);
                            schedule.setRewardpoint("2");
                        }else if(strArray[which].equals("1分")){
                            pointbtn.setBackgroundResource(R.drawable.one_point);
                            schedule.setRewardpoint("1");
                        }else if(strArray[which].equals("0分")){
                            pointbtn.setBackgroundResource(R.drawable.aim_point);
                            schedule.setRewardpoint("0");
                        }
                        SaveRecord(schedule.getObjectId(),schedule);
                    }
                });
                //创建对话框
                AlertDialog dialog = builder.create();
                //设置确定按钮
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();//显示对话框
            }
        });





        state = (ImageButton)findViewById(R.id.state_btn);
        //Bmob .initialize(this,"952c699bad14188ea5ef2af20da327b6");
        schedule.setDone("未完成");   //初始状态为未完成
        SaveRecord(schedule.getObjectId(),schedule);
        state.setOnClickListener(new View.OnClickListener() {
            int i = 0;   //用于判断 任务的状态 1为完成 0为未完成
            @Override
            public void onClick(View v) {
                if(i == 0)
                {
                    state.setBackgroundResource(R.drawable.example);
                    i = 1;
                    schedule.setDone("已完成");
                    SaveRecord(schedule.getObjectId(),schedule);
                }else{
                    state.setBackgroundResource(R.drawable.aim_state);
                    i = 0;
                    schedule.setDone("未完成");
                    SaveRecord(schedule.getObjectId(),schedule);
                }
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }

    public void SaveRecord(String objectId,Schedule schedule){
        schedule.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(getApplication(),"添加数据成功，返回objectId为：" + objectId,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();      //hahaha
                }
            }
        });
    }

}
