package com.example.zqf.theaim.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Adapter.ScheduleAdapter;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.MainActivity;
import com.example.zqf.theaim.ModifyScheduleActivity;
import com.example.zqf.theaim.R;
import com.example.zqf.theaim.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.zqf.theaim.Fragment.MonthDateView.getMonthDays;

/**
 * Created by Administrator on 2018/2/24.
 */


public class CalendarFragment extends TodayFragment{
    private MonthDateView monthDateView;
    public ListView listView;

    View view;
    Toolbar toolbar;
    private TextView Month;
    int month;

    public CalendarFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Calendar c=Calendar.getInstance();
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

//        List<Integer> list = new ArrayList<Integer>();
//        for(int i=0;i<scheduleList.size();i++){
//            list.add(scheduleList.get(i).getDay());
//        }
        monthDateView = (MonthDateView) view.findViewById(R.id.monthDateView);
        //toolbar.setTitle(monthDateView.getmSelMonth()+1+"月");
        //toast(monthDateView.Smonth+1+"月");
        //toast(monthDateView.getmSelMonth()+"月");
        Month = (TextView)view.findViewById(R.id.month);
        monthDateView.setTextView(Month);
        //monthDateView.setCTime(2018,3,28);
//        monthDateView.setDaysHasThingList(list);


        monthDateView.setDateClick(new MonthDateView.DateClick() {
            public void onClickOnDate() {
                month = monthDateView.getmSelMonth() + 1;
                Toast.makeText(getContext(), monthDateView.getmSelYear() + "年" + month + "月" + monthDateView.getmSelDay() + "日", Toast.LENGTH_SHORT).show();

                //toolbar.setTitle(month + "月");

                //切换日期的listview
                query = new BmobQuery<Schedule>();              //按条件查找
                query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
                query.addWhereEqualTo("year", monthDateView.getmSelYear());
                query.addWhereEqualTo("month", month);
                query.addWhereEqualTo("day", monthDateView.getmSelDay());
                query.order("done");
                query.findObjects(new FindListener<Schedule>() {
                    @Override
                    public void done(List<Schedule> object, BmobException e) {
                        if(e==null){;
                            scheduleList=object;                        //获取传递数据成功
                            adapter=new ScheduleAdapter(getActivity(),R.layout.fragment_item,scheduleList);     //设置适配器
                            listView.setAdapter(adapter);

                        }else{
                            toast("失败："+e.getMessage());
                        }
                    }
                });

            }
        });





                    //listview添加
        listView=(ListView) view.findViewById(R.id.calendar_listview);
        initdata();                 //添加数据
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
                                    toast("删除成功:"+schedule1.getUpdatedAt());
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


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof MainActivity){
            MainActivity mainActivity=(MainActivity)activity;
            toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        }
    }

}



