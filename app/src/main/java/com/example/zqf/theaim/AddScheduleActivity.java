package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.Fragment.MonthDateView;
import com.example.zqf.theaim.Fragment.ScheduleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.zqf.theaim.MainActivity.mainactivity;

public class AddScheduleActivity extends AppCompatActivity {


    private TextView textView;
    private EditText title;
    private EditText describe;
    private ImageButton pointbtn;
    private  ImageButton state;
    private Button actionbar_btn;
    private Chronometer chronometer;

    Schedule schedule = new Schedule();
    User user;

    public static List<String> parentList;
    public static List<String> SecAim;
    public static Map<String,List<String>> map;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String dataMap,dataParentList;
    String groupName,sgroupName;
    MonthDateView monthDateView;



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
                schedule.setTime(t);
            }

            schedule.setYear(bundle.getInt("year"));
            schedule.setMonth(bundle.getInt("month"));
            schedule.setDay(bundle.getInt("day"));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= BmobUser.getCurrentUser(User.class);

        schedule.setDone("false");   //初始状态为未完成
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头
        setContentView(R.layout.activity_add_aim);

//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        int xiaoshi = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
//        chronometer.setFormat("0"+String.valueOf(xiaoshi)+":%s");
//
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

        setCustomActionBar();

        actionbar_btn = (Button)findViewById(R.id.schedule_title_btn);

        SecAim = new ArrayList<String>();
        newfile();      //获取文件
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

                final AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);//实例化builder
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
                        //toast(str[which]);
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
            //toast(bundle.getString("title"));
            actionbar_btn.setText(bundle.getString("title"));
            schedule.setMastergoal(bundle.getString("title"));

        }else{
            actionbar_btn.setText("日程箱");
        }

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
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddScheduleActivity.this,SetTimeActivity.class);
                startActivityForResult(intent,0x11);
            }
        });

        pointbtn = (ImageButton) findViewById(R.id.point_btn);
        schedule.setRewardpoint(0);
        pointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strArray = new String[]{"3点","2点","1点","0点"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);//实例化builder
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

        state = (ImageButton)findViewById(R.id.state_btn);
//        state.setOnClickListener(new View.OnClickListener() {
//            int i = 0;   //用于判断 任务的状态 1为完成 0为未完成
//
//            @Override
//            public void onClick(View v) {
//                if(i == 0)
//                {
//                    state.setBackgroundResource(R.drawable.square_ok);
//                    i = 1;
//                    schedule.setDone("true");
//                }else{
//                    state.setBackgroundResource(R.drawable.square);
//                    i = 0;
//                    schedule.setDone("false");
//                }
//            }
//        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }

    //日程添加
    public void SaveRecord(String objectId,Schedule schedule){
        //schedule.setMastergoal("wqes");
        schedule.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                   // Toast.makeText(getApplication(),"添加数据成功，返回objectId为：" + objectId,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();      //hahaha
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
                  //  Toast.makeText(getApplication(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(TextUtils.isEmpty(title.getText())){
                    this.finish();
                    return false;
                }else if(!TextUtils.isEmpty(title.getText()) && !actionbar_btn.getText().equals("日程箱")){
//                    User user= BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存;
                    schedule.setMaster(user);
                    int i = user.getScheduleNumber();
                    user.setScheduleNumber(i+1);
//                    user.setDoscheduleNumber(4);
//                    user.setScheduleNumber(7);
                    SaveUserRecord(user.getObjectId(),user);
                    SaveRecord(schedule.getObjectId(),schedule);
                    this.finish();
                    mainactivity.finish();
                    Intent intent = new Intent(AddScheduleActivity.this,MainActivity.class);
                    startActivity(intent);
                   // replaceFragment(new ScheduleFragment());
                    return false;
                }else if(!TextUtils.isEmpty(title.getText()) && actionbar_btn.getText().equals("日程箱")){
//                    User user= BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存;
                    schedule.setMaster(user);
                    int i = user.getScheduleNumber();
                    user.setScheduleNumber(i+1);
//                    user.setDoscheduleNumber(0);
//                    user.setScheduleNumber(1);
                    schedule.setMastergoal("日程箱");
                    SaveUserRecord(user.getObjectId(),user);
                    SaveRecord(schedule.getObjectId(),schedule);
                    this.finish();
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };

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

    public void newfile(){              //从文件获取二级列表

        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();
        String file = user.getUsername();
        sp = this.getApplicationContext().getSharedPreferences(user.getUsername()+"", this.MODE_PRIVATE);
        //查询SharedPreferences存储的数据
        // 第一个参数是要查询的键，返回对应的值，当键不存在时，返回参数二作为结果。
        dataMap = sp.getString("dataMap", null);
        dataParentList = sp.getString("dataParentList", null);
        try {
            //初始化parentList
            JSONArray jsonArray = new JSONArray(dataParentList);
            for (int i = 0; i < jsonArray.length(); i++) {
                parentList.add(jsonArray.get(i).toString());
            }

            //初始化map
            JSONObject jsonObject = new JSONObject(dataMap);
            for (int i = 0; i < jsonObject.length(); i++) {
                String key = jsonObject.getString(parentList.get(i));
                JSONArray array = new JSONArray(key);
                List<String> list = new ArrayList<String>();
                for (int j = 0; j < array.length(); j++) {
                    list.add(array.get(j).toString());
                }
                map.put(parentList.get(i), list);
            }

            Log.d("eric", "①："+map+"②："+parentList);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eric","String转Map或List出错"+e);
        }
    }
    private void replaceFragment(Fragment fragment){                    //fragment切换
        FragmentManager fm=mainactivity.getSupportFragmentManager();              //新的fragment的不同之处
        FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }

}

