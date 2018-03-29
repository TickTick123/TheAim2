package com.example.zqf.theaim.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqf.theaim.Adapter.NoteAdapter;
import com.example.zqf.theaim.Adapter.RewardAdapter;
import com.example.zqf.theaim.Bean.Note;
import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.MainActivity;
import com.example.zqf.theaim.Note_Show;
import com.example.zqf.theaim.R;
import com.example.zqf.theaim.Util;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class NoteFragment extends Fragment {
    User user;
    public List<Note> noteList=new ArrayList<>();          //数据源
    NoteAdapter adapter;
    BmobQuery<Note> query;
    ListView listView;
    MainActivity mainActivity;
    public NoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(User.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_note, container, false);
        listView=(ListView) view.findViewById(R.id.note_list);

        query = new BmobQuery<Note>();              //按条件查找
        query.addWhereEqualTo("master", user);  // 查询当前用户的所有note
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
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> object, BmobException e) {
                if(e==null){;
                    noteList=object;                        //获取传递数据成功
                    //toast(object.get(1).getContent().toString());               //获取正确
                    adapter=new NoteAdapter(getActivity(),R.layout.fragment_note_item,noteList);     //设置适配器
                    listView.setAdapter(adapter);               //出错

                }else{
                    toast("失败："+e.getMessage());
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {         //点击
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position1, long l) {
                final Note note = noteList.get(position1);
                Intent intent = new Intent(getContext(),Note_Show.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("key",note);
                intent.putExtras(bundle);
                getContext().startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {     //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position1, long l) {

                final Note note=noteList.get(position1);

                AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());     //确定删除的对话框
                //dialog.setTitle("确认删除");
                dialog.setMessage("是否确定删除此记录？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        adapter.remove(note);      //视图删除

                        //数据库移除此项
                        note.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    toast("删除成功:"+note.getUpdatedAt());
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

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof MainActivity){
            mainActivity=(MainActivity)activity;
        }
    }
}
