package com.example.zqf.theaim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Fragment.AimFragment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dell on 2018/2/28.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    private List<String> parentList;
    private Map<String,List<String>> map;
    private Context context;
    private EditText edit_modify;
    private ModifyDialog dialog;
    private TextView childText;

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
        final ImageView image_delete=(ImageView) convertView.findViewById(R.id.delete_parent);
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AimFragment.deleteGroup(groupPos);
                showParentMenu(image_delete,groupPos);

            }
        });
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
        childText = (TextView) convertView.findViewById(R.id.text_child);
        final ImageView image_delete = (ImageView) convertView.findViewById(R.id.delete_child);
        String parentName = parentList.get(groupPosition);
        String childName = map.get(parentName).get(childPosition);
        childText.setText(childName);
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AimFragment.deleteChild(groupPos, childPos);
                showChildtMenu(image_delete,groupPos,childPos);
            }
        });
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

    //父目标菜单
    private void showParentMenu(View view,int groupPosition) {
        final int groupPos = groupPosition;
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this.context, view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.parent_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                if(item.getTitle().equals("添加子目标")){
                   alertAddDialog(MainActivity.fragment.getActivity(), "新增子项", groupPos);
                }else if(item.getTitle().equals("删除父目标")){
                    AimFragment.deleteGroup(groupPos);
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();

            }
        });
        popupMenu.show();
    }

    //子目标菜单
    private void showChildtMenu(View view,int groupPosition,int childPosition) {
        final int groupPos = groupPosition;
        final int childPos = childPosition;
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this.context, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.child_meau, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                if(item.getTitle().equals("添加日程")){
                    Intent intent = new Intent(MainActivity.fragment.getActivity(),AddScheduleActivity.class);
                    String s = childText.getText().toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("title",map.get(parentList.get(groupPos)).get(childPos));
                    intent.putExtras(bundle);
                    MainActivity.fragment.getActivity().startActivity(intent);
                    //alertAddDialog(MainActivity.fragment.getActivity(), "新增日程", groupPos);
                }else if(item.getTitle().equals("删除子目标")){
                    AimFragment.deleteChild(groupPos,childPos);
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }
}
