package com.example.zqf.theaim.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class MonthDateView extends View {
    private static final int NUM_COLUMNS = 7;   //一周7天
    private static final int NUM_ROWS = 6;     //月份天数5~6行
    private Paint mPaint;
    private int mDayColor = Color.parseColor("#000000");  //平常日为黑色
    private int mSelectDayColor = Color.parseColor("#ffffff");  //选中的日期为白色
    private int mSelectBGColor = Color.parseColor("#1FC2F3");   //选中日期的背景色为浅蓝
    private int mCurrentColor = Color.parseColor("#ff0000");   //今天为红色
    private int mCurrYear, mCurrMonth, mCurrDay;   //今天日期
    private int mSelYear, mSelMonth, mSelDay;      //选中日期
    private int mColumnSize, mRowSize;     //行列大小
    private DisplayMetrics mDisplayMetrics;  //获取屏幕大小
    private int mDaySize = 18;
    private TextView tv_date, tv_week;
    private int weekRow;
    private int[][] daysString;
    private int mCircleRadius = 6;
    private DateClick dateClick;
    private int mCircleColor = Color.parseColor("#ff0000");  //有事项的日期设为红色标记
    private List<Integer> daysHasThingList;  //链表存储有事务的日期
    private GestureDetector detector;

    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();  //获取屏幕尺寸
        Calendar calendar = Calendar.getInstance();   //日历控件
        mPaint = new Paint();  //画图
        mCurrYear = calendar.get(Calendar.YEAR);  //获取当前年份
        mCurrMonth = calendar.get(Calendar.MONTH); //获取当前月份
        mCurrDay = calendar.get(Calendar.DATE);  //获取当前天
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);



        detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 120) {
                    // 像左滑动
                    enterPrevMonth();
                    return true;
                } else if (e1.getX() - e2.getX() < -120) {
                    // 向右滑动
                    enterNextMonth();
                    return true;
                }
                return false;
            }
        });
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //测量
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //测量模式下的规格大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); //测量模式

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize); //设置view的大小
    }

    @Override
    protected void onDraw(Canvas canvas) { //绘制
        initSize();
        daysString = new int[6][7];  //存放日历的数组
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity); //根据手机分辨率设置字体大小
        String dayString;
        int mMonthDays = getMonthDays(mSelYear, mSelMonth);//根据年份和月份获取本月天数
        int weekNumber = getFirstDayWeek(mSelYear, mSelMonth);//根据年份和月份获得第一天是星期几
        Log.d("DateView", "DateView:" + mSelMonth + "月1号周" + weekNumber);
        for (int day = 0; day < mMonthDays; day++) {   //放置日期
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2); //根据手机分辨率设置坐标
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (dayString.equals(mSelDay + "")) {
                //绘制背景色矩形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                mPaint.setColor(mSelectBGColor);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row, column, day + 1, canvas);
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth) {
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            } else {
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
            if (tv_date != null) {
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }

            if (tv_week != null) {
                tv_week.setText("第" + weekRow + "周");
            }
        }
    }

    private void drawCircle(int row, int column, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day)) return;
            mPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
            float circley = (float) (mRowSize * row + mRowSize * 0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) { //触摸滑动操作
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        if (detector.onTouchEvent(event))
        {return true;}
        return true;
    }




    private void enterNextMonth() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    private void enterPrevMonth() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

        /**
         * 初始化列宽行高
         */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取选择的年份
     *
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getmSelMonth() {
        mSelMonth = mSelMonth + 1 ;
        return mSelMonth;
    }

    /**
     * 获取选择的日期
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     *
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     *
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     *
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认红色
     *
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     *
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置显示当前日期的控件
     *
     * @param tv_date 显示日期
     * @param tv_week 显示周
     */
    public void setTextView(TextView tv_date, TextView tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 设置事务天数
     *
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     *
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }

    @SuppressLint("WrongConstant")
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }
}