package com.example.zqf.theaim.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.R;

import java.util.Calendar;

import cn.bmob.v3.BmobQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends ScheduleFragment {

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public  void initdata(){
        query = new BmobQuery<Schedule>();              //按条件查找
        final Calendar c=Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH)+1;
        mDay=c.get(Calendar.DAY_OF_MONTH);    //可用
        toast("m:"+mMonth);

        query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
        query.addWhereEqualTo("year", mYear);
        query.addWhereEqualTo("month", mMonth);
        query.addWhereEqualTo("day", mDay);
        query.order("done");
    }

}
