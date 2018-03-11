package com.example.zqf.theaim.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.R;

import java.util.Calendar;

import cn.bmob.v3.BmobQuery;

/**
 * A simple {@link Fragment} subclass.
 */

public class SchAimFragment extends TodayFragment{

    private String aimname1;

    public SchAimFragment() {
    }

    @SuppressLint("ValidFragment")
    public SchAimFragment(String aimname) {
         this.aimname1=aimname;
    }



    @Override
    public  void initdata(){
        query = new BmobQuery<Schedule>();              //按条件查找

        query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
        query.addWhereEqualTo("mastergoal", aimname1);
        query.order("done");
    }


}
