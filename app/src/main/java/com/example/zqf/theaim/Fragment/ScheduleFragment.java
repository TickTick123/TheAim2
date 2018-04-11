package com.example.zqf.theaim.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Adapter.ScheduleAdapter;
import com.example.zqf.theaim.AddScheduleActivity;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.MainActivity;
import com.example.zqf.theaim.ModifyScheduleActivity;
import com.example.zqf.theaim.R;
import com.example.zqf.theaim.Util;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }OnListFragmentInteractionListener
 * interface.
 */
public class ScheduleFragment extends Fragment {

    public List<Schedule> scheduleList=new ArrayList<>();          //数据源
    ScheduleAdapter adapter;
    User user,user1,user2;
    BmobQuery<Schedule> query;
    MonthDateView monthDateView;
    List<Integer> Event = new ArrayList<Integer>();

    public ScheduleFragment() {

    }

//    public ScheduleFragment(List<Schedule> scheduleLists) {
//        scheduleList=scheduleLists;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存

        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    user1=object;
                }else{
                    toast("失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


//        BmobQuery<User> query1 = new BmobQuery<User>();
//        query1.getObject(user1.getObjectId(), new QueryListener<User>() {
//            @Override
//            public void done(User object, BmobException e) {
//                if(e==null){
//                    user2=object;
////                    toast(user1.getScheduleNumber()+"");
////                    toast(user1.getDoscheduleNumber()+"");
//                }else{
//                    toast("失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//        });

//        Schedule p2 = new Schedule();                       //测试
//        p2.setMaster(user);
//        //p2.setMastergoal();
//        p2.setContent("ppp");
//        p2.setDecribe("sdf");
//        p2.setYear("20xcv1sdf8");
//        p2.setMouth("sfxc");
//        p2.setDay("27vcs");
//        p2.setDone("flase");
//        p2.setTime("22sdmin");
//        scheduleList.add(p2);
//        toast("wde："+scheduleList.size());          //1

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        final ListView listView=(ListView) view.findViewById(R.id.list1);


        initdata();

        if( Util.isNetworkConnected(getContext()))       {        //居然可以不创建对象的使用
            //toast("有网");                                  //缓存不是本地数据库，也慢，不可直接created时完成
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                                                                // 先从网络读取数据，如果没有，再从缓存中获取。
        }else{
            toast("亲，您当前没有联网噢~");
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
                                                            //先从缓存获取数据，如果没有，再从网络获取
        }
        query.findObjects(new FindListener<Schedule>() {
            @Override
            public void done(List<Schedule> object, BmobException e) {
                if(e==null){;
                    scheduleList=object;                        //获取传递数据成功
                    adapter=new ScheduleAdapter(getActivity(),R.layout.fragment_item,scheduleList);     //设置适配器
                    listView.setAdapter(adapter);
                    //replaceFragment(new ScheduleFragment());

                    if(scheduleList.size()!=0){
                        for(int i = 0;i<scheduleList.size();i++) {
                            Schedule schedule = scheduleList.get(i);
                            if(schedule.getMonth()==3){
                                Event.add(schedule.getDay());
                                //monthDateView.setEvent3(Event);
                                //toast(Event.get(i).toString());
                            }
                        }
                        //monthDateView.getEvent3(Event);
                    }else if(scheduleList.size()==0){
                        getView().setBackgroundDrawable(getResources().getDrawable(R.drawable.free_day));
                    }

                }else{
                    toast("失败："+e.getMessage());
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {         //点击
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position1, long l) {

                Schedule schedule1=scheduleList.get(position1);
                Intent intent=new Intent(getContext(), ModifyScheduleActivity.class);       //修改界面
                Bundle bundle=new Bundle();
                bundle.putSerializable("key",schedule1);
                intent.putExtras(bundle);
                getContext().startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {     //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position1, long l) {


                final Schedule schedule1=scheduleList.get(position1);

                AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());     //确定删除的对话框
                //dialog.setTitle("确认删除");
                dialog.setMessage("是否确定删除此日程？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        adapter.remove(schedule1);      //视图删除

                        //数据库移除此项
                        schedule1.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    if(schedule1.getDone().equals("true")){
                                        int DoSum = user1.getDoscheduleNumber();
                                        user1.setDoscheduleNumber(DoSum - 1);
                                        int Sum = user1.getScheduleNumber();
                                        //toast(Sum+"d");
                                        user1.setScheduleNumber(Sum - 1);
                                       // replaceFragment(new ScheduleFragment());
                                        SaveUserRecord(user1.getObjectId(),user1);
                                    }else if(schedule1.getDone().equals("false")){
                                        int Sum = user1.getScheduleNumber();
                                        //toast(Sum+"d");
                                        user1.setScheduleNumber(Sum - 1);
                                        //replaceFragment(new ScheduleFragment());
                                        SaveUserRecord(user1.getObjectId(),user1);
                                    }

                                   // toast("删除成功:"+schedule1.getUpdatedAt());
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

    public  void initdata(){
        query = new BmobQuery<Schedule>();              //按条件查找
        query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
        query.order("done");
    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
    }

    public void SaveUserRecord(String id,User user){
        user.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                   // Toast.makeText(getContext(),"修改数据成功" ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment){                    //fragment切换
        FragmentManager fm=getActivity().getSupportFragmentManager();              //新的fragment的不同之处
        FragmentTransaction transaction=fm.beginTransaction();      //fragment控制器
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }
}
