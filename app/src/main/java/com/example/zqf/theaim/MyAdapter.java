package com.example.zqf.theaim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zqf.theaim.Fragment.AimFragment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/28.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    private List<String> parentList;
    private Map<String,List<String>> map;
    private Context context;
    private EditText edit_modify;
    private ModifyDialog dialog;

    //构造函数
    public MyAdapter(Context context, List<String> parentList, Map<String,List<String>> map) {
        this.context = context;
        this.parentList = parentList;
        this.map = map;
    }

    //获取分组数
    @Override
    public int getGroupCount() {
        return parentList.size();
    }
    //获取当前组的子项数
    @Override
    public int getChildrenCount(int groupPosition) {
        String groupName = parentList.get(groupPosition);
        return map.get(groupName).size();
    }
    //获取当前组对象
    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }
    //获取当前子项对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String groupName = parentList.get(groupPosition);
        return map.get(groupName).get(childPosition);
    }
    //获取组ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //获取子项ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    //组视图初始化
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final int groupPos = groupPosition;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_parent, null);
        }
        TextView parentText = (TextView) convertView.findViewById(R.id.text_parent);
        parentText.setText(parentList.get(groupPosition));//重绘
        return convertView;
    }
    //子项视图初始化
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final int groupPos = groupPosition;
        final int childPos = childPosition;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_child, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.text_child);

        String parentName = parentList.get(groupPosition);
        String childName = map.get(parentName).get(childPosition);
        childText.setText(childName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //弹新增子项对话框
    public void alertAddDialog(Context context, String title, int currentGroup){
        final int group = currentGroup;

        dialog = new ModifyDialog(context, title, null);
        edit_modify = dialog.getEditText();
        dialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AimFragment.addChild(group, edit_modify.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
