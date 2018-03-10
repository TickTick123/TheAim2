package com.example.zqf.theaim.Adapter;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.R;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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

        final Schedule schedule=getItem(position);    //获取当前项的Schedule
        View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);

        final ImageButton Tickimage =(ImageButton)view.findViewById(R.id.tick_btn);
        TextView content=(TextView)view.findViewById(R.id.content);
        TextView decribe=(TextView)view.findViewById(R.id.decribe);

//        content.setText(scheduleList.get(position).getContent());
//        decribe.setText(scheduleList.get(position).getDecribe());

        if(schedule.getDone().equals("true"))
            Tickimage.setImageResource(R.drawable.square_ok);            //完成的显示勾号
        if(schedule.getDone().equals("false"))
            Tickimage.setImageResource(R.drawable.square);            //未完成的显示括号

        content.setText(schedule.getContent());
        decribe.setText(schedule.getDecribe());



        Tickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(schedule.getDone().equals("false")) {
                    schedule.setDone("true");
                    Tickimage.setImageResource(R.drawable.square_ok);        //修改图标
                    String id1 = schedule.getObjectId();
                    schedule.update(id1, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast("更新成功:" + schedule.getUpdatedAt());
                            } else {
                                toast("更新失败：" + e.getMessage());
                            }
                        }
                    });
                } else if(schedule.getDone().equals("true")){
                    schedule.setDone("false");
                    Tickimage.setImageResource(R.drawable.square);        //修改图标
                    String id1 = schedule.getObjectId();
                    schedule.update(id1, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast("更新成功:" + schedule.getUpdatedAt());
                            } else {
                                toast("更新失败：" + e.getMessage());
                            }
                        }
                    });
                }

            }
        });
        return view;

    }

    // 刷新列表中的数据
    public void refresh(List<Schedule> list) {
        scheduleList = list;
        //将数字的类型编号转换为文字
        //exchangeType(mType);
        notifyDataSetChanged();
    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(getContext(), toast, Toast.LENGTH_LONG).show();
    };

}
