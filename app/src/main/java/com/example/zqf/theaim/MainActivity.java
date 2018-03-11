package com.example.zqf.theaim;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.Fragment.AimFragment;
import com.example.zqf.theaim.Fragment.CalendarFragment;
import com.example.zqf.theaim.Fragment.RewardFragment;
import com.example.zqf.theaim.Fragment.ScheduleFragment;
import com.example.zqf.theaim.Fragment.TodayFragment;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    static AimFragment  fragment;
    private int num;              //标记目前所在fragment
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存用

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
        if (id == R.id.action_add_reward) {               //菜单添加奖励按钮相应

            BmobUser.logOut();
            return true;
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

//            Schedule p2 = new Schedule();                       //测试
//            User user = BmobUser.getCurrentUser(User.class);
//            p2.setMaster(user);
//            //p2.setMastergoal();
//            p2.setContent("ppp");
//            p2.setDecribe("sdf");
//            p2.setYear(2016);
//            p2.setMouth(3);
//            p2.setDay(2);
//            p2.setDone("flase");
//            p2.setTime("22sdmin");
//            p2.save(new SaveListener<String>() {
//                @Override
//                public void done(String objectId,BmobException e) {
//                    if(e==null){
//                        toast("添加数据成功，返回objectId为："+objectId);
//                    }else{
//                        toast("创建数据失败：" + e.getMessage());
//                    }
//                }
//            });


        } else if (id == R.id.nav_manage) {                     //日历
            replaceFragment(new CalendarFragment());
            toolbar.setTitle("日历");
            num=4;

        } else if (id == R.id.nav_share) {                      //奖励箱
            replaceFragment(new RewardFragment());
            toolbar.setTitle("奖励箱");
            num=5;

        }
//        else if (id == R.id.nav_send) {                       //备忘录
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    protected void onStart(){
        super.onStart();
        //toast("和和"+num);                //无问题
        if(num==0){
            toast("今天"+num);
            replaceFragment(new TodayFragment());           //有问题
        }
        if(num==1){
            toast("今天"+num);
            replaceFragment(new TodayFragment());           //有问题
        }
        if (num==2){
            fragment=new AimFragment();
            replaceFragment(fragment);
        }
        if (num==3){
            replaceFragment(new ScheduleFragment());
        }
        if (num==4){
            replaceFragment(new CalendarFragment());
        }
        if (num==5){
            replaceFragment(new RewardFragment());
        }
        //toast("和和"+num);                //无问题
    }

    @Override
    protected void onRestart(){             //调回界面的方法
        super.onRestart();

//        if(num==1){
//            toast("今天"+num);                        //没问题
////            replaceFragment(new TodayFragment());           //有问题
//        }
//        if (num==2){
//            fragment=new AimFragment();
//            replaceFragment(fragment);
//        }
//        if (num==3){
//            //replaceFragment(new ScheduleFragment());
//        }
//        if (num==4){
//            replaceFragment(new CalendarFragment());
//        }
//        if (num==5){
//            replaceFragment(new RewardFragment());
//        }
//        toast("和和"+num);                //无问题

    }


    private void replaceFragment(Fragment fragment){                    //fragment切换
            FragmentManager fm=getSupportFragmentManager();              //新的fragment的不同之处
            FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
            transaction.replace(R.id.content,fragment);
            transaction.commit();
    }

    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }


}
