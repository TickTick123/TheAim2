package com.example.zqf.theaim;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class AddReAimActivity extends AppCompatActivity {
    private EditText recontent;
    private EditText repoint;
    private Button submit;
    private EditText aimcontent;
    private Button aimsubmit;
    private User user;

    public static List<String> parentList;
    public static Map<String,List<String>> map;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String dataMap,dataParentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_re_aim);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recontent=(EditText)findViewById(R.id.re_content);
        repoint=(EditText)findViewById(R.id.re_costpoint);
        submit=(Button)findViewById(R.id.re_commit);
        aimcontent=(EditText)findViewById(R.id.aim_content);
        aimsubmit=(Button)findViewById(R.id.aim_commit);

        user = BmobUser.getCurrentUser(User.class);
        submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){

                Reward p2 = new Reward();                       //添加奖励
                User user = BmobUser.getCurrentUser(User.class);
                p2.setMaster(user);

                if(recontent.getText().toString().equals("")||repoint.getText().toString().equals("")){
                    toast("请输入奖励内容和所需奖励点");
                }
                else {
                    p2.setContent(recontent.getText().toString());
                    p2.setCostpoint(Integer.parseInt(repoint.getText().toString()));
                    p2.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                //toast("添加数据成功，返回objectId为：" + objectId);
                            } else {
                                toast("创建数据失败：" + e.getMessage());
                            }
                        }
                    });

                    finish();
                }

            }
        });


        aimsubmit.setOnClickListener(new Button.OnClickListener(){      //添加目标
            @Override
            public void onClick(View v){
                String aimname=aimcontent.getText().toString();
                newfile();
                parentList.add(aimname);
                List<String> list = new ArrayList<String>();
                map.put(aimname, list);
                saveData();
                finish();
            }
        });


    }

    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    public void newfile(){              //从文件获取二级列表

        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();
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
    };


    //保存数据到文件
    public static void saveData(){
        JSONObject jsonObject = new JSONObject(map);
        dataMap = jsonObject.toString();
        dataParentList = parentList.toString();

        editor = sp.edit();
        editor.putString("dataMap", dataMap);
        editor.putString("dataParentList", dataParentList);
        editor.commit();
        //  editor.clear().commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty, menu);                           //添加菜单项
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
