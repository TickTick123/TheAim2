package com.example.zqf.theaim.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Adapter.RewardAdapter;
import com.example.zqf.theaim.Adapter.ScheduleAdapter;
import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.ModifyScheduleActivity;
import com.example.zqf.theaim.R;
import com.example.zqf.theaim.Util;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 */
public class RewardFragment extends Fragment{

    public List<Reward> rewardList=new ArrayList<>();          //数据源
    RewardAdapter adapter;
    User user;
    BmobQuery<Reward> query;
    ListView listView;
    TextView textView;
    private PopupWindow mPopWindow;
    public RewardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_reward, container, false);
        listView=(ListView) view.findViewById(R.id.re_list);
        textView=(TextView)view.findViewById(R.id.reward_amount) ;
        textView.setText(user.getRewardpoint()+"");
        //showPopupWindow();
        query = new BmobQuery<Reward>();              //按条件查找
        query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
        //query.order("updatedAt");
        if( Util.isNetworkConnected(getContext()))       {        //居然可以不创建对象的使用
            //toast("有网");                                  //缓存不是本地数据库，也慢，不可直接created时完成
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            // 先从网络读取数据，如果没有，再从缓存中获取。
        }else{
            toast("亲，您当前没有联网噢~");
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            //先从缓存获取数据，如果没有，再从网络获取
        }
        query.findObjects(new FindListener<Reward>() {
            @Override
            public void done(List<Reward> object, BmobException e) {
                if(e==null){;
                    rewardList=object;                        //获取传递数据成功
                    //toast(object.get(1).getContent().toString());               //获取正确
                    adapter=new RewardAdapter(getActivity(),R.layout.fragment_re_item,rewardList);     //设置适配器
                    listView.setAdapter(adapter);               //出错

                }else{
                    toast("失败："+e.getMessage());
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {         //点击
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position1, long l) {
                final Reward reward=rewardList.get(position1);
                AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());     //确定删除的对话框
                //dialog.setTitle("确认删除");

                dialog.setMessage("是否确定花费"+reward.getCostpoint()+"奖励点？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        user.setRewardpoint(user.getRewardpoint()-reward.getCostpoint());
                        String id = user.getObjectId();
                        user.update(id, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //toast("修改数据成功");
                                } else {
                                    toast("创建数据失败：" + e.getMessage());
                                }
                            }
                        });



                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {     //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position1, long l) {

                final Reward reward=rewardList.get(position1);

                AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());     //确定删除的对话框
                //dialog.setTitle("确认删除");
                dialog.setMessage("是否确定删除此奖励？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        adapter.remove(reward);      //视图删除

                        //数据库移除此项
                        reward.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    toast("删除成功:"+reward.getUpdatedAt());
                                }else{
                                    toast("删除失败：" + e.getMessage());
                                }
                            }

                        });

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                return false;
            }
        });

        return view;

    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
    }

//    private void showPopwindow() {
//        //View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//        View popView = View.inflate(getContext(), R.layout.buttom_view, null);
//
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//
//        final PopupWindow popWindow = new PopupWindow(popView,width,height);
//        //popWindow.setAnimationStyle(R.style.AnimBottom);
//        popWindow.setFocusable(true);
//        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
//
//
//
//
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popWindow.setBackgroundDrawable(dw);
//        popWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }

    private void PopWindow() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.buttom_view, null);
        //设置屏幕的高度和宽度
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        final PopupWindow pop = new PopupWindow(view, (w_screen)*4/5, (h_screen)*3/10);
        //如果不设置背景颜色的话，无法是pop dimiss掉。
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.style.MyPopupWindow_anim_style);
    }

//    private void showPopupWindow() {
//        //设置contentView
//        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_note_item, null);
//        mPopWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        mPopWindow.setContentView(contentView);
//        //设置各个控件的点击响应
//
//        //显示PopupWindow
//        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_reward, null);
//        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
//
//        mPopWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
//        //mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
//       // mPopWindow.setOutsideTouchable(true);
//    }

//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.used_pwd:
//                mPopWindow.dismiss();
//                break;
//        }
//
//    }
}
