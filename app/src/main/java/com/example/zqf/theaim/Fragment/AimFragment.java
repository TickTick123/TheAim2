package com.example.zqf.theaim.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
//
import com.example.zqf.theaim.AddScheduleActivity;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.MainActivity;
import com.example.zqf.theaim.ModifyDialog;
import com.example.zqf.theaim.MyAdapter;
import com.example.zqf.theaim.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by dell on 2018/2/28
 */

public class AimFragment extends Fragment {
    private View view;
    private ExpandableListView expandableListView;
    public static MyAdapter adapter;
    public static List<String> parentList;
    public static Map<String,List<String>> map;
    private ModifyDialog dialog;
    private EditText edit_modify;
    private int currentGroup,currentChild;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String dataMap,dataParentList;
    private User user;
    @Override//初始化Fragment页面
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //加载fragment_aim布局和ExpandableListView控件
        view = inflater.inflate(R.layout.fragment_aim, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandablelistview);
        user = BmobUser.getCurrentUser(User.class);
        //调用initData()方法进行组数据parentList和组对应子项map的初始化
        initData();

        adapter = new MyAdapter(getActivity().getApplicationContext(), parentList, map);
        expandableListView.setAdapter(adapter);

         //设置子项点击事件
         MyOnClickListener myListener = new MyOnClickListener();
         expandableListView.setOnChildClickListener(myListener);

        //设置长按点击事件
        MyOnLongClickListener myLongListener = new MyOnLongClickListener();
        expandableListView.setOnItemLongClickListener(myLongListener);

        return view;

    }
    public void initData(){
        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();
        // getSharedPreferences里面
        // 参数一为要保存的xml文件名，不同的文件名产生的对象不同，但同一文件名可以产生多个引用，
        // 从而可以保证数据共享。此处注意指定参数一时，不用加xml后缀，由系统自动添加
        // 参数二使得SharedPreferences存储的数据只能在本应用内获得
        String file = user.getUsername();
        sp = getActivity().getApplicationContext().getSharedPreferences(user.getUsername()+"", getActivity().MODE_PRIVATE);
       //查询SharedPreferences存储的数据
        // 第一个参数是要查询的键，返回对应的值，当键不存在时，返回参数二作为结果。
        dataMap = sp.getString("dataMap", null);
        dataParentList = sp.getString("dataParentList", null);

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
    //自定义点击监听事件
  public class MyOnClickListener implements ExpandableListView.OnChildClickListener{
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            String str = (String)adapter.getChild(groupPosition, childPosition);
           // String str = "choose"+groupPosition+"-"+childPosition;
           // Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();                  //跳转对应日程

            replaceFragment(new SchAimFragment(str));
            return false;
        }
    }

    //自定义长按监听事件
  public class MyOnLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            //长按子项
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
                long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPos);

                currentGroup = groupPosition;
                currentChild = childPosition;

                String str = (String)adapter.getChild(groupPosition, childPosition);
                alertModifyDialog("修改此项名称",str);
                Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
                return true;
                //长按组
            }else if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPos);

                currentGroup = groupPosition;
                currentChild = childPosition;

                String group = parentList.get(groupPosition);
                alertModifyDialog("修改组名称", group);

                String str = (String)adapter.getGroup(groupPosition);
                Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    //新增组
    public static void addGroup(String newGroupName){
        parentList.add(newGroupName);
        List<String> list = new ArrayList<String>();
        map.put(newGroupName, list);
        adapter.notifyDataSetChanged();
        saveData();
    }

    //新增子项到指定组
    public static void addChild(int groupPosition, String newChildName){
        String groupName = parentList.get(groupPosition);
        List<String> list = map.get(groupName);
        list.add(newChildName);
        adapter.notifyDataSetChanged();
         saveData();
    }

    //删除指定组
    public static void deleteGroup(int groupPos){
        String groupName = parentList.get(groupPos);
        map.remove(groupName);
        parentList.remove(groupPos);
        adapter.notifyDataSetChanged();
        saveData();
    }
    //删除指定子项
    public static void deleteChild(int groupPos, int childPos){
        String groupName = parentList.get(groupPos);
        List<String> list = map.get(groupName);
        list.remove(childPos);
        adapter.notifyDataSetChanged();
        saveData();
    }

    //修改该项名称
    public void modifyName(int groupPosition, int childPosition, String modifyName){
        Toast.makeText(getActivity(), String.valueOf(groupPosition)+'-'+String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
        if(childPosition<0){
            //修改组名称
            String groupName = parentList.get(groupPosition);
            if(!groupName.equals(modifyName)){
                map.put(modifyName, map.get(groupName));
                map.remove(groupName);
                parentList.set(groupPosition, modifyName);
            }

        }else{
            //修改子项名称
            String group = parentList.get(groupPosition);
            List<String> list =map.get(group);
            list.set(childPosition, modifyName);
            map.put(group, list);
        }
        adapter.notifyDataSetChanged();
        saveData();
    }

    //弹修改对话框
    public void alertModifyDialog(String title, String name){
        dialog = new ModifyDialog(getActivity(), title, name);
        edit_modify = dialog.getEditText();
        dialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyName(currentGroup, currentChild, edit_modify.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //弹新增组对话框
    public void alertAddDialog(Context context, String title){
        dialog = new ModifyDialog(context, title, null);
        edit_modify = dialog.getEditText();//获取输入的内容
        dialog.setOnClickCommitListener(new View.OnClickListener() {//设置对话框的点击事件响应
            @Override
            public void onClick(View v) {
                addGroup(edit_modify.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //保存数据
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

    private void replaceFragment(Fragment fragment){                    //fragment切换
        FragmentManager fm=getFragmentManager();              //新的fragment的不同之处
        FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }



}
