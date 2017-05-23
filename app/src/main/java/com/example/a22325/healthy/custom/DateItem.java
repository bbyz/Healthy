package com.example.a22325.healthy.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a22325.healthy.R;

import java.util.ArrayList;

/**
 * Created by 22325 on 2017/5/20.
 */

public class DateItem extends LinearLayout implements View.OnClickListener {
    public TextView saturday;
    public TextView friday;
    public TextView thursday;
    public TextView wednesday;
    public TextView tuesday;
    public TextView monday;
    public TextView sunday;

    public ArrayList<TextView> weeks = new ArrayList<>();

    public DateItem(Context context) {
        super(context);
    }

    public DateItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DateItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        sunday = (TextView) findViewById(R.id.tv_sunday);
        monday = (TextView) findViewById(R.id.tv_monday);
        tuesday = (TextView) findViewById(R.id.tv_tuesday);
        wednesday = (TextView) findViewById(R.id.tv_wednesday);
        thursday = (TextView) findViewById(R.id.tv_thursday);
        friday = (TextView) findViewById(R.id.tv_friday);
        saturday = (TextView) findViewById(R.id.tv_saturday);
        sunday.setOnClickListener(this);
        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        weeks.add(sunday);
        weeks.add(monday);
        weeks.add(tuesday);
        weeks.add(wednesday);
        weeks.add(thursday);
        weeks.add(friday);
        weeks.add(saturday);
    }

    private int tempDay;

    private Time myTime;

    public Time refresh(Time myTime) {
        try {
            //设置选中日期
            this.myTime = myTime;
            int actualMaximum = myTime.getActualMaximum(Time.MONTH_DAY);
            if (myTime.monthDay > actualMaximum) {
                myTime.month++;
                if (myTime.month > 11) {
                    myTime.year++;
                    myTime.month = 0;
                }
            }
            weeks.get(myTime.weekDay).setTextColor(0xffff0000);
            for (int i = 0; i < 7; i++) {
                if (i < myTime.weekDay) {
                    tempDay = myTime.monthDay - (myTime.weekDay - i);
                } else {
                    tempDay = myTime.monthDay + (i - myTime.weekDay);
                }
                if (tempDay <= 0) {
                    tempDay = tempDay + actualMaximum;
                } else if (tempDay > actualMaximum) {
                    tempDay = tempDay - actualMaximum;
                }
                if (i == myTime.weekDay) {
                    myTime.monthDay = tempDay;
                }
                weeks.get(i).setText(tempDay + "");
            }
        } catch (Exception e) {
            Log.e("tan", e.getMessage());
        }
        return myTime;
    }

    interface DateChangeListener {
        void changeDate();
    }

    private DateItem.DateChangeListener dateChangedListener;

    public void setonDateChangeListener(DateItem.DateChangeListener dateChangedListener) {
        this.dateChangedListener = dateChangedListener;
    }

    @Override
    public void onClick(View v) {
        TextView textView = null;
        int newWeekDay = weeks.indexOf(v);
        if (newWeekDay != myTime.weekDay) {
            textView = (TextView) v;
            textView.setTextColor(0xffff0000);
            weeks.get(myTime.weekDay).setTextColor(0xff000000);
            //重新设置weekday,monthday
            int index = newWeekDay - myTime.weekDay;
            myTime.monthDay = myTime.monthDay + index;
            int actualMaximum = myTime.getActualMaximum(Time.MONTH_DAY);
            if (myTime.monthDay > actualMaximum) {
                myTime.month++;
                myTime.monthDay = myTime.monthDay - actualMaximum;
            } else if (myTime.monthDay <= 0) {
                myTime.month--;
                actualMaximum = myTime.getActualMaximum(Time.MONTH_DAY);
                myTime.monthDay = myTime.monthDay + actualMaximum;
            }
            myTime.weekDay = newWeekDay;
            this.setTag(myTime.weekDay);
            this.dateChangedListener.changeDate();
        }
    }
}
