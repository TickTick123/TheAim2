package com.example.zqf.theaim;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zqf.theaim.Fragment.MonthDateView;

import java.util.Calendar;

public class SetTimeActivity extends AppCompatActivity {
    private Calendar calendar;
    //private MonthDateView monthDateView;
    private TextView clicktime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setHomeAsUpIndicator(R.drawable.confirm); //修改actionbar左上角返回按钮的图标
        setContentView(R.layout.activity_set_time);

        calendar = Calendar.getInstance();
        final TextView textView = (TextView)findViewById(R.id.set_time);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(SetTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        if(minute<10)
                        {
                            textView.setText(hourOfDay+":0"+minute);
                            String time = hourOfDay+":0"+minute;
                            bundle.putCharSequence("time",time);
                            intent.putExtras(bundle);
                            setResult(0x11,intent);
                        }else{
                            textView.setText(+hourOfDay+":"+minute);
                            String time = hourOfDay+":"+minute;
                            bundle.putCharSequence("time",time);
                            intent.putExtras(bundle);
                            setResult(0x11,intent);
                        }

                    }
                },hour,minute,true).show();
            }
        });

        clicktime = (TextView)findViewById(R.id.click_time);
        final MonthDateView monthDateView = (MonthDateView)findViewById(R.id.monthDateView);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                Intent intent = getIntent();
                int year = monthDateView.getmSelYear();
                int month = monthDateView.getmSelMonth();
                month = month + 1;
                int day = monthDateView.getmSelDay();
                Bundle bundle = new Bundle();
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                bundle.putInt("day",day);
                intent.putExtras(bundle);
                setResult(0x11,intent);
                clicktime.setText(year+"年"+month+"月"+day+"日");
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);                           //添加菜单项
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
