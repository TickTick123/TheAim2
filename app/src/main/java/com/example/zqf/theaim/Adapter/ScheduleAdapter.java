package com.example.zqf.theaim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.R;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by zqf on 2018/3/3.
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private int resourcedId;

    private List<Schedule> scheduleList = null;

    public ScheduleAdapter(Context context, int textViewResourcedId, List<Schedule> object){
        super(context,textViewResourcedId,object);
        resourcedId=textViewResourcedId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Schedule schedule=getItem(position);    //获取当前项的Schedule
        View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);

        ImageButton Tickimage =(ImageButton)view.findViewById(R.id.tick_btn);
        TextView content=(TextView)view.findViewById(R.id.content);
        TextView decribe=(TextView)view.findViewById(R.id.decribe);

//        content.setText(scheduleList.get(position).getContent());
//        decribe.setText(scheduleList.get(position).getDecribe());
        content.setText(schedule.getContent());
        decribe.setText(schedule.getDecribe());
        return view;
    }

    // 刷新列表中的数据
    public void refresh(List<Schedule> list) {
        scheduleList = list;
        //将数字的类型编号转换为文字
        //exchangeType(mType);
        notifyDataSetChanged();
    }

}
