package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.Fragment.ScheduleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyScheduleActivity extends AddScheduleActivity {
    private TextView textView;
    private EditText title;
    private EditText describe;
    private ImageButton pointbtn;
    private  ImageButton state;
    private Schedule schedule;
    private Button actionbar_btn;
    private Chronometer chronometer;

//    public static List<String> parentList;
//    public static List<String> SecAim;
//    public static Map<String,List<String>> map;
//    public static SharedPreferences sp;
//    public static SharedPreferences.Editor editor;
//    public static String dataMap,dataParentList;
//    String groupName,sgroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aim);

        setCustomActionBar();
        //toast(user.getScheduleNumber()+"");
        //toast(user.getDoscheduleNumber()+"");
//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        int xiaoshi = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
//        chronometer.setFormat("0"+String.valueOf(xiaoshi)+":%s");
//        Button button1 = (Button)findViewById(R.id.button1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 设置开始时间
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                // 开始计时
//                chronometer.start();
//            }
//        });
//
//        Button button2 = (Button)findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chronometer.stop();
//                toast(chronometer.getText().toString());
//            }
//        });


        textView = (TextView)findViewById(R.id.aim_time);
        state = (ImageButton)findViewById(R.id.state_btn);
        title = findViewById(R.id.aim_title);
        describe = findViewById(R.id.aim_describe);
        pointbtn = (ImageButton) findViewById(R.id.point_btn);
        actionbar_btn = (Button)findViewById(R.id.schedule_title_btn);

        SecAim = new ArrayList<String>();
        super.newfile();
        for(int i=0;i<parentList.size();i++){
            groupName=parentList.get(i);
            for(int j=0;j<map.get(groupName).size();j++){
                sgroupName=map.get(groupName).get(j);
                SecAim.add((sgroupName));
            }
        }
        actionbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ModifyScheduleActivity.this);//实例化builder
                builder.setIcon(R.mipmap.drawers);//设置图标
                builder.setTitle("移动到子目标");//设置标题
                final String[] str= new String[SecAim.size()];
                for (int i = 0; i < SecAim.size(); i++) {
                    str[i] = SecAim.get(i);
                }

                //设置单选列表
                builder.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionbar_btn.setText(str[which]);
                        schedule.setMastergoal(str[which]);
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


        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            schedule = (Schedule) bundle.getSerializable("key");       //传来的schedule对象
            actionbar_btn.setText(schedule.getMastergoal());
        }
        //toast("ss"+schedule.getMonth()+schedule.getContent());

        //获取日程时间
        String t = schedule.getTime();
        if(TextUtils.isEmpty(t) && schedule.getYear()!=0 && schedule.getMonth()!=0 && schedule.getDay()!=0){
            textView.setText(schedule.getYear()+"年"+schedule.getMonth()+"月"+schedule.getDay()+"日");
        }else if(!TextUtils.isEmpty(t) && schedule.getYear()!=0 && schedule.getMonth()!=0 && schedule.getDay()!=0){
            textView.setText(schedule.getYear()+"年"+schedule.getMonth()+"月"+schedule.getDay()+"日"+t);
        }else if(TextUtils.isEmpty(t) && schedule.getYear()==0 && schedule.getMonth()==0 && schedule.getDay()==0){
            textView.setText("今天");
        }else if(!TextUtils.isEmpty(t) && schedule.getYear()==0 && schedule.getMonth()==0 && schedule.getDay()==0){
            textView.setText("今天"+t);
        }

        //获取日程状态
        if(schedule.getDone().equals("true")){
            state.setBackgroundResource(R.drawable.square_ok);
        }else if(schedule.getDone().equals("false")){
            state.setBackgroundResource(R.drawable.square);
        }

        //获取日程奖励点
        if(schedule.getRewardpoint()==0){
            pointbtn.setBackgroundResource(R.drawable.aim_point);
        }else if(schedule.getRewardpoint()==1){
            pointbtn.setBackgroundResource(R.drawable.one_point);
        }else if(schedule.getRewardpoint()==2){
            pointbtn.setBackgroundResource(R.drawable.two_point);
        }else if(schedule.getRewardpoint()==3){
            pointbtn.setBackgroundResource(R.drawable.three_point);
        }

        //获取日程标题和描述
        title.setText(schedule.getContent());
        describe.setText(schedule.getDecribe());

        //监听描述文本改动
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
            }
        });

        //监听日程标题文本改动
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
            }
        });

        //点击跳转到日历
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyScheduleActivity.this,SetTimeActivity.class);
                startActivityForResult(intent,0x11);
            }
        });

        //奖励点设置
        pointbtn = (ImageButton) findViewById(R.id.point_btn);
        pointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strArray = new String[]{"3点","2点","1点","0点"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(ModifyScheduleActivity.this);//实例化builder
                builder.setIcon(R.mipmap.reward_icon);//设置图标
                builder.setTitle(R.string.points);//设置标题

                //设置单选列表
                builder.setSingleChoiceItems(strArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String point = null;
                        if(strArray[which].equals("3点")){
                            pointbtn.setBackgroundResource(R.drawable.three_point);
                            schedule.setRewardpoint(3);
                        }else if(strArray[which].equals("2点")){
                            pointbtn.setBackgroundResource(R.drawable.two_point);
                            schedule.setRewardpoint(2);
                        }else if(strArray[which].equals("1点")){
                            pointbtn.setBackgroundResource(R.drawable.one_point);
                            schedule.setRewardpoint(1);
                        }else if(strArray[which].equals("0点")){
                            pointbtn.setBackgroundResource(R.drawable.aim_point);
                            schedule.setRewardpoint(0);
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

        //设置日程状态
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast(schedule.getDone().toString());
                if(schedule.getDone().equals("false"))
                {
                    state.setBackgroundResource(R.drawable.square_ok);
                    schedule.setDone("true");
                    int Done = user.getDoscheduleNumber();
                    user.setDoscheduleNumber(Done + 1);
                    int point = user.getRewardpoint();
                    int reward = schedule.getRewardpoint();
                    user.setRewardpoint(point+reward);
                    SaveUserRecord(user.getObjectId(),user);
                }else if(schedule.getDone().equals("true")){
                    state.setBackgroundResource(R.drawable.square);
                    schedule.setDone("false");
                    int Done = user.getDoscheduleNumber();
                    user.setDoscheduleNumber(Done - 1);
                    int point = user.getRewardpoint();
                    int reward = schedule.getRewardpoint();
                    user.setRewardpoint(point-reward);
                    SaveUserRecord(user.getObjectId(),user);
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            schedule.setMonth(bundle.getInt("month"));
            schedule.setDay(bundle.getInt("day"));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }

    public void SaveRecord(String objectId,Schedule schedule){
        String id = schedule.getObjectId();
        schedule.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                    if (e == null) {
                        //Toast.makeText(getApplication(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //用户user更新

    public void SaveUserRecord(String id,User user){
        user.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //Toast.makeText(getApplication(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setCustomActionBar() {
        //ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_modify, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView);
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }


    //保存数据到文件
    public static void saveData(){
        JSONObject jsonObject = new JSONObject(map);
        dataMap = jsonObject.toString();
        dataParentList = parentList.toString();

        editor = sp.edit();
        editor.putString("dataMap", dataMap);
        editor.putString("dataParentList", dataParentList);
        editor.commit();
        //  editor.clear().commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(TextUtils.isEmpty(title.getText())){
                    this.finish();
                    return false;
                }else{
                    User user= BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存;
                    schedule.setMaster(user);
                    this.finish();
                    SaveRecord(schedule.getObjectId(),schedule);
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }



}
