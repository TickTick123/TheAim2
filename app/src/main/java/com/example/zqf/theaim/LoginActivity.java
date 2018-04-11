package com.example.zqf.theaim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_name;
    private EditText et_password;
    private CheckBox ch_remember;
    private final static String SP_INFOS="login";
    private final static String USERNAME="uname";
    private final static String USERPASS="upass";
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String dataMap,dataParentList;
    public static MyAdapter adapter;
    public static List<String> parentList;
    public static Map<String,List<String>> map;
    String path;
    static  LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = LoginActivity.this;

        et_name =(EditText) findViewById(R.id.editText_name);             //初始化
        et_password=(EditText)findViewById(R.id.editText_password);
        ch_remember=(CheckBox)findViewById(R.id.checkBox_remember);

        findViewById(R.id.button_login).setOnClickListener(this);       //设置监听器
        findViewById(R.id.textView_forget).setOnClickListener(this);       //设置监听器
        findViewById(R.id.textView_reg).setOnClickListener(this);       //设置监听器
        findViewById(R.id.textView_phonereg).setOnClickListener(this);       //设置监听器



    }
    @Override
    protected void onStart(){
        super.onStart();
        checkIfRemember();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(ch_remember.isChecked()) {
            this.rememberMe(et_name.getText().toString(),et_password.getText().toString());
        }
    }

    public void rememberMe(String uname,String upass) {
        SharedPreferences sp=getSharedPreferences(SP_INFOS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(USERNAME,uname);
        editor.putString(USERPASS,upass);
        editor.commit();
    }
    public void checkIfRemember(){
        SharedPreferences sp=getSharedPreferences(SP_INFOS,MODE_PRIVATE);
        String username =sp.getString(USERNAME,null);
        String userpass =sp.getString(USERPASS,null);
        if(username!=null&&userpass!=null){
            et_name.setText(username);
            et_password.setText(userpass);
            ch_remember.setChecked(true);
        }
    }



    @Override
    public void onClick(View view){

        if(view.getId()==R.id.button_login){            //登录按钮
            final User bu = new User();                   //  bmob登录

            bu.loginByAccount(et_name.getText().toString(),et_password.getText().toString(),   //用户名登录
                    new LogInListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(user!=null){
                                BmobQuery<User> query=new BmobQuery<>();
                                query.getObject(user.getObjectId(), new QueryListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if(e==null){
                                            download(user.getPicUser());
                                        }
                                    }
                                });
                                finish();
                                Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("image",path);
//                                mainIntent.putExtras(bundle);
                                startActivity(mainIntent);

                            }
                            if(e.getErrorCode()==9018)
                                toast("用户名和密码不能为空");
                            if(e.getErrorCode()==101)
                                toast("用户名获密码不正确");
                        }
                    });

        }

        if(view.getId()==R.id.textView_forget){         //忘记密码按钮
            Intent mainIntent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(mainIntent);
        }
        if(view.getId()==R.id.textView_reg){             //一键登录按钮
            Intent mainIntent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(mainIntent);
        }
        if(view.getId()==R.id.textView_phonereg){        //手机号登录按钮
            Intent mainIntent=new Intent(LoginActivity.this,PhoneRegisterActivity.class);
            startActivity(mainIntent);
        }
    }

    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };

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

    public void initData(){
        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();
        // getSharedPreferences里面
        // 参数一为要保存的xml文件名，不同的文件名产生的对象不同，但同一文件名可以产生多个引用，
        // 从而可以保证数据共享。此处注意指定参数一时，不用加xml后缀，由系统自动添加
        // 参数二使得SharedPreferences存储的数据只能在本应用内获得
        //String file = user.getUsername();
//        sp = getBaseContext().getSharedPreferences(user.getUsername()+"",getBaseContext().MODE_PRIVATE);
        //查询SharedPreferences存储的数据
        // 第一个参数是要查询的键，返回对应的值，当键不存在时，返回参数二作为结果。
        dataMap = sp.getString("dataMap", null);
        dataParentList = sp.getString("dataParentList", null);
        //sp = this.getSharedPreferences(bu.getUsername()+"",this.MODE_PRIVATE);
        editor = sp.edit();
        if(dataMap== null || dataParentList == null){
            //Toast.makeText(getActivity(),"请添加组",Toast.LENGTH_SHORT).show();//tgy的
            //getView().setBackgroundDrawable(getResources().getDrawable(R.drawable.free_day));
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
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("eric","String转Map或List出错"+e);
            }
        }
        Log.e("eric", dataMap+"!&&!"+dataParentList);
        saveData();
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
