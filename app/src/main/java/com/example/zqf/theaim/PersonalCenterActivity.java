package com.example.zqf.theaim;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zqf.theaim.Bean.Schedule;
import com.example.zqf.theaim.Bean.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.functions.Action1;

public class PersonalCenterActivity extends AppCompatActivity implements OnChartValueSelectedListener{
    private User user;
    private PieChart mPieChart;
    private TextView Id;
    private TextView Mail;
    private TextView samount;
    private TextView dsamount;
    private TextView rwdamount;
    private ImageView head;
    private ImageView user_head;
    private TextView user_id;
    private TextView user_mail;
    private Bitmap Head;
    private String path;
    static String path0="/data/data/com.example.zqf.theaim/cache/bmob/head.jpg";
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        user = BmobUser.getCurrentUser(User.class);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //显示返回箭头
        //actionBar.setDisplayShowTitleEnabled(false); //隐藏标题
        initView();

        Id = (TextView)findViewById(R.id.id);
        Mail = (TextView)findViewById(R.id.mail);
        samount = (TextView)findViewById(R.id.schedule_amount_text);
        dsamount = (TextView)findViewById(R.id.done_schedule_amount_text);
        rwdamount = (TextView)findViewById(R.id.reward_point_text);
        head = (ImageView)findViewById(R.id.touxiang);
        //user_head = (ImageView)findViewById(R.id.head);
//        user_id = (TextView)findViewById(R.id.User_id);
//        user_mail = (TextView)findViewById(R.id.User_mail);

        samount.setText(user.getScheduleNumber()+"");
        Id.setText(user.getUsername()+"");
        Mail.setText(user.getEmail()+"");
        dsamount.setText(user.getDoscheduleNumber()+"");
        rwdamount.setText(user.getRewardpoint()+"");

        //获取修改后的信息
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle!=null){
            Id.setText(bundle.getString("id"));
            Mail.setText(bundle.getString("mail"));
        }

//        user_id.setText(user.getUsername()+"");
//        user_mail.setText(user.getEmail()+"");



        File F = new File(path0);
        if(!F.exists()){
            BmobQuery<User> query=new BmobQuery<>();
            query.getObject(user.getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                        download(user.getPicUser());
                    }
                }
            });
        }

        Bitmap bt = BitmapFactory.decodeFile(path0);
        head.setImageBitmap(bt);
//        user_head.setImageBitmap(bt);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder=new AlertDialog.Builder(PersonalCenterActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("请选择获取图片方式");
                final String[] Items={"从相册中选择","使用相机拍摄"};
                builder.setItems(Items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0) {
                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);//返回被选中项的URI  
                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//得到所有图片的URI
                            startActivityForResult(intent1,1);
                        }
                        if(i==1){
                            try{
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//开启相机应用程序获取并返回图片（capture：俘获）  
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"head.jpg")));//指明存储图片或视频的地址URI  
                                startActivityForResult(intent2,2);//采用ForResult打开  
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"相机无法启动，请先开启相机权限", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //return view;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //初始化View
    private void initView() {

        //饼状图
        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        //数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(user.getDoscheduleNumber(), "已完成"));
        entries.add(new PieEntry(user.getScheduleNumber()-user.getDoscheduleNumber(), "未完成"));
        //toast(i+"");
       // samount.setText(i);
        //设置数据
        setData(entries);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("已完成日程数饼图");
        return s;
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void toast(String toast) {                   //Fragment里面的Toast便捷使用方法
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    this.finish();
                    super.finish();
                    Intent i = new Intent(PersonalCenterActivity.this,MainActivity.class);
                    startActivity(i);
                    return false;
//            case android.R.id.exit
            case R.id.information:
                Intent intent = new Intent(PersonalCenterActivity.this,UserImformationActivity.class);
                startActivity(intent);
            case R.id.exit:
//                user.logOut();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //从相册里面取相片的返回结果  
            case 1:
                if(resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片  
                }
                break;
            //相机拍照后的返回结果
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()+"/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片  
                }
                break;
            //调用系统裁剪图片后  
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Head = extras.getParcelable("data");
                    if (Head != null) {
                        // 上传服务器代码
                        saveBitmap(Head);
                        final BmobFile bmobFile = new BmobFile(new File(path0));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null) {
                                    user.setPicUser(bmobFile);
                                    user.updateObservable().subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            //Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                                        }
                                    },new Action1<Throwable>(){
                                        @Override
                                        public void call(Throwable throwable) {
                                            Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else
                                    Toast.makeText(getApplicationContext(),"上传失败", Toast.LENGTH_LONG).show();
                            }
                        });
                        head.setImageBitmap(Head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void cropPhoto(Uri uri) {          //调用系统的裁剪功能
        Intent intent = new Intent("com.android.camera.action.CROP");
        //找到指定URI对应的资源图片  
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        // outputX outputY 是裁剪图片宽高  
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        //进入系统裁剪图片的界面  
        startActivityForResult(intent,3);
    }

    public void saveBitmap(Bitmap bm) {//bitmap保存到本地路径
        File f = new File(path0);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




}
