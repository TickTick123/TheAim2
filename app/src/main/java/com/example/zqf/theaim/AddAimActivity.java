package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddAimActivity extends AppCompatActivity {

    private TextView textView;
    private ImageButton pointbtn;
    private  ImageButton state;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x11 && resultCode == 0x11){
            Bundle bundle = data.getExtras();
            String t = bundle.getString("time");
            if(TextUtils.isEmpty(t)){
                textView.setText(bundle.getInt("year")+"年"+bundle.getInt("month")+"月"+bundle.getInt("day")+"日");
            }else {
                textView.setText(bundle.getInt("year") + "年" + bundle.getInt("month") + "月" + bundle.getInt("day") + "日"+ bundle.getString("time") );
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头
        //actionBar.setHomeAsUpIndicator(R.drawable.back); //修改actionbar左上角返回按钮的图标
        setContentView(R.layout.activity_add_aim);

        textView = (TextView)findViewById(R.id.aim_time);

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
                        if(strArray[which].equals("3分")){
                            pointbtn.setBackgroundResource(R.drawable.three_point);
                        }else if(strArray[which].equals("2分")){
                            pointbtn.setBackgroundResource(R.drawable.two_point);
                        }else if(strArray[which].equals("1分")){
                            pointbtn.setBackgroundResource(R.drawable.one_point);
                        }else if(strArray[which].equals("0分")){
                            pointbtn.setBackgroundResource(R.drawable.aim_point);
                        }
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
        state.setOnClickListener(new View.OnClickListener() {
            int i = 0;   //用于判断 任务的状态 1为完成 0为未完成

            @Override
            public void onClick(View v) {
                if(i == 0)
                {
                    state.setBackgroundResource(R.drawable.example);
                    i = 1;
                }else{
                    state.setBackgroundResource(R.drawable.aim_state);
                    i = 0;
                }
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }
}
