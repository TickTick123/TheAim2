package com.example.zqf.theaim.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zqf.theaim.Adapter.ScheduleAdapter;
import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.R;
import com.example.zqf.theaim.Util;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ScheduleFragment extends Fragment {

    private List<Schedule> scheduleList=new ArrayList<>();          //数据源
    ScheduleAdapter adapter;
    User user;

    public ScheduleFragment() {

    }

//    public ScheduleFragment(List<Schedule> scheduleLists) {
//        scheduleList=scheduleLists;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(User.class);        //bmob查询当前缓存

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        final ListView listView=(ListView) view.findViewById(R.id.list1);

        BmobQuery<Schedule> query = new BmobQuery<Schedule>();              //按条件查找
        query.addWhereEqualTo("master", user);  // 查询当前用户的所有日程
        query.order("-updatedAt");
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
                toast("点击"+schedule1.getContent());

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {     //长按
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position1, long l) {

                Schedule schedule1=scheduleList.get(position1);
                toast("您长按了");

                return false;
            }
        });

        return view;
    }


    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
    };

}
