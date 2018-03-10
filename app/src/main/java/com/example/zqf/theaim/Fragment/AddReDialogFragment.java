package com.example.zqf.theaim.Fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zqf.theaim.Bean.Reward;
import com.example.zqf.theaim.Bean.User;
import com.example.zqf.theaim.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReDialogFragment extends DialogFragment {

    private EditText recontent;
    private EditText repoint;
    private Button submit;

    public AddReDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_re_dialog, container, false);
        recontent=(EditText)view.findViewById(R.id.re_content);
        repoint=(EditText)view.findViewById(R.id.re_costpoint);
        submit=(Button) view.findViewById(R.id.re_commit);
        getDialog().setTitle(R.string.add_re_dialog_fragment);
        submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){

                Reward p2 = new Reward();                       //添加奖励
                User user = BmobUser.getCurrentUser(User.class);
                p2.setMaster(user);
                p2.setContent("看电影");
                p2.setCostpoint(30);
//                p2.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String objectId,BmobException e) {
//                        if(e==null){
//                            toast("添加数据成功，返回objectId为："+objectId);
//                        }else{
//                            toast("创建数据失败：" + e.getMessage());
//                        }
//                    }
//                });

            }
        });

        return view;
    }


    public void toast(String toast) {           //Toast便捷使用方法
        Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
    }

}
