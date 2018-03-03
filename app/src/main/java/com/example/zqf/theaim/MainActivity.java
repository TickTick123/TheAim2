package com.example.zqf.theaim;



import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.MotionEvent;
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

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.Fragment.AimFragment;
import com.example.zqf.theaim.Fragment.CalendarFragment;
import com.example.zqf.theaim.Fragment.MonthDateView;
import com.example.zqf.theaim.Fragment.ScheduleFragment;
import com.example.zqf.theaim.Fragment.TodayFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);           //悬浮按钮用于日程添加
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BmobUser.logOut();   //清除缓存用户对象，既退出

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存用户

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
        if (id == R.id.action_settings) {                   //菜单按钮相应
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

        } else if (id == R.id.nav_gallery) {                    //目标箱
            replaceFragment(new AimFragment());
        } else if (id == R.id.nav_slideshow) {                  //日程箱
            replaceFragment(new ScheduleFragment());

//            Schedule p2 = new Schedule();                       //测试
//            User user = BmobUser.getCurrentUser(User.class);
//            p2.setMaster(user);
//            //p2.setMastergoal();
//            p2.setContent("ppp");
//            p2.setDecribe("sdf");
//            p2.setYear("20xcv1sdf8");
//            p2.setMouth("sfxc");
//            p2.setDay("27vcs");
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

        } else if (id == R.id.nav_share) {                      //奖励箱

        }
//        else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        ///
    }

    private void replaceFragment(Fragment fragment){                    //fragment切换
            FragmentManager fm=getSupportFragmentManager();              //新的fragment的不同之处
            FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
            transaction.replace(R.id.content,fragment);
            transaction.commit();
    }
    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };

}
