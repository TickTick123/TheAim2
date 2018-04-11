package com.example.zqf.theaim;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Note;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.Fragment.AimFragment;
import com.example.zqf.theaim.Fragment.CalendarFragment;
import com.example.zqf.theaim.Fragment.MonthDateView;
import com.example.zqf.theaim.Fragment.NoteFragment;
import com.example.zqf.theaim.Fragment.RewardFragment;
import com.example.zqf.theaim.Fragment.ScheduleFragment;
import com.example.zqf.theaim.Fragment.TodayFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    static  MainActivity mainactivity;
    static AimFragment  fragment;
    private int num;              //标记目前所在fragment
    static Toolbar toolbar;
    User user;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String dataMap,dataParentList;
    public static MyAdapter adapter;
    public static List<String> parentList;
    public static Map<String,List<String>> map;

    private TextView user_id;
    private TextView user_mail;
    private ImageView user_head;
    static String path0="/data/data/com.example.zqf.theaim/cache/bmob/head.jpg";
    String path;
    Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainactivity=MainActivity.this;
//        sp = this.getSharedPreferences(user.getUsername()+"",this.MODE_PRIVATE);
//        editor = sp.edit();
        //initData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        num=0;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);           //悬浮按钮用于日程添加
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(MainActivity.this,AddScheduleActivity.class);
                startActivity(mainIntent);
                //BmobUser.logOut();   //清除缓存用户对象，既退出
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
            }
        });

//        Intent i = getIntent();
//        Bundle bundle = i.getExtras();
//        if(bundle!=null){
//            String s = bundle.getString("image");
//            Bitmap m = BitmapFactory.decodeFile(s);
//            user_head.setImageBitmap(m);
//        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存用
//        toast(user.getUsername());
//        toast(user.getEmail());
        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        user_id = (TextView)headerLayout.findViewById(R.id.User_id);
        user_head = (ImageView)headerLayout.findViewById(R.id.head);
        user_mail = (TextView)headerLayout.findViewById(R.id.User_mail);

        user_id.setText(user.getUsername()+"");
        user_mail.setText(user.getEmail()+"");

        sp = getSharedPreferences(user.getUsername()+"",MODE_PRIVATE);
        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();
        dataMap = sp.getString("dataMap", null);
        dataParentList = sp.getString("dataParentList", null);
        //sp = this.getSharedPreferences(bu.getUsername()+"",this.MODE_PRIVATE);
        editor = sp.edit();
        if(dataMap== null || dataParentList == null){
        }
        else{
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
            } catch (JSONException ex) {
                ex.printStackTrace();
                Log.e("eric","String转Map或List出错"+ex);
            }
        }
        Log.e("eric", dataMap+"!&&!"+dataParentList);
        saveData();

        File F = new File(path0);
        if(!F.exists()){
            BmobQuery<User> query=new BmobQuery<>();
            query.getObject(user.getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                        download(user.getPicUser());
                    }
                }
            });
        }
        Bitmap bt = BitmapFactory.decodeFile(path0);
        user_head.setImageBitmap(bt);

        //toast(user_id.getText().toString());

        user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(headerLayout.getContext(),PersonalCenterActivity.class);
                startActivity(intent);
            }
        });

        user_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(headerLayout.getContext(),PersonalCenterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_aim) {                   //菜单添加奖励，目标按钮相应

            Intent mainIntent=new Intent(MainActivity.this,AddReAimActivity.class);
            startActivity(mainIntent);

            return true;
        }

        if(id == R.id.action_add_note) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.note_create, null);
            final EditText ctitle = (EditText) view.findViewById(R.id.cre_note_title);
            final EditText ccontent = (EditText) view.findViewById(R.id.cre_note_content);
//            创建对话框
            AlertDialog dialog = new AlertDialog.Builder(this).create();
//                dialog.setIcon(R.mipmap.ic_launcher);//设置图标
            dialog.setTitle("记事本");//设置标题
            dialog.setView(view);//添加布局
            //设置按键
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    if(!ctitle.getText().toString().equals(null)){
                        note.setMaster(user);
                        note.setTitle(ctitle.getText().toString());
                        note.setContent(ccontent.getText().toString());
                        SaveNote(user.getObjectId(),note);
                        replaceFragment(new NoteFragment());
                    }
                }

            });
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {                              //今天
            replaceFragment(new TodayFragment());
            toolbar.setTitle("今天");
            num=1;

        } else if (id == R.id.nav_gallery) {                    //目标箱
            fragment=new AimFragment();
            replaceFragment(fragment);
            toolbar.setTitle("目标箱");
            num=2;
        } else if (id == R.id.nav_slideshow) {                  //日程箱
            replaceFragment(new ScheduleFragment());
            toolbar.setTitle("日程箱");
            num=3;
        } else if (id == R.id.nav_manage) {                     //日历
            replaceFragment(new CalendarFragment());
            toolbar.setTitle("日历");
            num=4;

        } else if (id == R.id.nav_share) {                      //奖励箱
            replaceFragment(new RewardFragment());
            toolbar.setTitle("奖励箱");
            num=5;
        }else if(id == R.id.nav_note){
            replaceFragment(new NoteFragment());
            toolbar.setTitle("记录本");
            num = 6;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    protected void onStart(){           //为什么刚开运行了Schedule的语法运行了
        super.onStart();
        if(num==0){
            replaceFragment(new TodayFragment());
        }else
        if(num==1){
            replaceFragment(new TodayFragment());
        }else
        if (num==2){
            fragment=new AimFragment();
            replaceFragment(fragment);
        }else
        if (num==3){
            replaceFragment(new ScheduleFragment());
        }else
        if (num==4){
            replaceFragment(new CalendarFragment());
        }else
        if (num==5){
            replaceFragment(new RewardFragment());
        }else if(num==6){
            replaceFragment(new NoteFragment());
        }

    }

    //记事本添加
    public void SaveNote(String objectId,Note note){
        //schedule.setMastergoal("wqes");
        note.save(new SaveListener<String>() {
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


    private void replaceFragment(Fragment fragment){                    //fragment切换
            FragmentManager fm=getSupportFragmentManager();              //新的fragment的不同之处
            FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
            transaction.replace(R.id.content,fragment);
            transaction.commit();
    }

    static public void getMonth(int i){
        toolbar.setTitle(i);
    }
    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    public void download(BmobFile picUser){
        picUser.download((new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    //toast(s);
                    path = s;
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        }));
    }

    public static void saveData(){
        JSONObject jsonObject = new JSONObject(map);
        dataMap = jsonObject.toString();
        dataParentList = parentList.toString();
        editor = sp.edit();
        editor.putString("dataMap", dataMap);
        editor.putString("dataParentList", dataParentList);
        editor.commit();
        // editor.clear().commit();
    }


}
