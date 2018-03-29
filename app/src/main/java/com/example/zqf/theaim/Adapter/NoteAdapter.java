package com.example.zqf.theaim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Note;
import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.R;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zqf on 2018/3/11.
 */

public class NoteAdapter extends ArrayAdapter<Note> {
    private int resourcedId;
//    private List<Reward> rewardList = null;

    public NoteAdapter(Context context, int textViewResourcedId, List<Note> object){
        super(context,textViewResourcedId,object);
        resourcedId=textViewResourcedId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Note note=getItem(position);    //获取当前项的Schedule
        View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);

        TextView note_title=(TextView)view.findViewById(R.id.note_content);
        TextView note_time=(TextView)view.findViewById(R.id.note_time);

        note_title.setText(note.getTitle());
        note_time.setText(note.getCreatedAt());

        return view;

    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(getContext(), toast, Toast.LENGTH_LONG).show();
    }
}
