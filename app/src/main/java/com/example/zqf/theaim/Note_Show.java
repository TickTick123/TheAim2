package com.example.zqf.theaim;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Note;
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

public class Note_Show extends AppCompatActivity {

    TextView stime;
    EditText stitle;
    EditText scontent;

    Note note = new Note();
    User user;




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 0x11 && resultCode == 0x11){
//            Bundle bundle = data.getExtras();
//            String t = bundle.getString("time");
//            if(TextUtils.isEmpty(t)){
//                textView.setText(bundle.getInt("year")+"年"+bundle.getInt("month")+"月"+bundle.getInt("day")+"日");
//            }else {
//                textView.setText(bundle.getInt("year") + "年" + bundle.getInt("month") + "月" + bundle.getInt("day") + "日"+ bundle.getString("time") );
//                schedule.setTime(t);
//            }
//
//            schedule.setYear(bundle.getInt("year"));
//            schedule.setMonth(bundle.getInt("month"));
//            schedule.setDay(bundle.getInt("day"));
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= BmobUser.getCurrentUser(User.class);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头
        setContentView(R.layout.note_show);


        stime = (TextView)findViewById(R.id.show_note_set_time);
        stitle = (EditText)findViewById(R.id.show_note_title);
        scontent = (EditText)findViewById(R.id.show_note_content);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            note = (Note) bundle.getSerializable("key");       //传来的schedule对象
            stime.setText(note.getCreatedAt());
            stitle.setText(note.getTitle());
            scontent.setText(note.getContent());
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(TextUtils.isEmpty(stitle.getText())){
                    this.finish();
                    return false;
                }else if(!TextUtils.isEmpty(stitle.getText()) && stitle.getText().equals(note.getTitle())){
                    if(scontent.equals(note.getContent())){
                        this.finish();
                    }else if(!scontent.equals(note.getContent())){
                        note.setContent(scontent.getText().toString());
                        SaveNoteRecord(note.getObjectId(),note);
                        this.finish();
                    }
                    return false;
                }else if(!TextUtils.isEmpty(stitle.getText()) && !stitle.getText().equals(note.getTitle())){
                    if(scontent.equals(note.getContent())){
                        note.setTitle(stitle.getText().toString());
                        SaveNoteRecord(note.getObjectId(),note);
                        this.finish();
                }else if(!scontent.equals(note.getContent())){
                        note.setTitle(stitle.getText().toString());
                        note.setContent(scontent.getText().toString());
                        SaveNoteRecord(note.getObjectId(),note);
                        this.finish();
                }
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //note修改
    public void SaveNoteRecord(String id,Note note){
        note.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //Toast.makeText(getApplication(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplication(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };




}

