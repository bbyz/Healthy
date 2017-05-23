package com.example.a22325.healthy.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a22325.healthy.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 22325 on 2017/5/20.
 */

public class DatePick extends LinearLayout {

    private ViewPager datePick;
    private TextView time;

    public DatePick(Context context) {
        super(context);
    }

    public DatePick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DatePick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        datePick = (ViewPager) findViewById(R.id.vp_date);
        time = (TextView) findViewById(R.id.tv_date);
    }

    private ArrayList<DateItem> date = new ArrayList<>();

    private Time myTime = new Time();

    public void refresh(final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        Log.e("tan", year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分" + second + "秒" + "星期" + weekDay);
        myTime.setToNow();
        //获取当前日期
        this.time.setText(myTime.year + "-" + (myTime.month + 1) + "-" + myTime.monthDay);
        datePick.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                DateItem dateContainer = null;
                if (date.size() - 1 >= position) {
                    dateContainer = date.get(position);
                } else {
                    dateContainer = (DateItem) inflater.inflate(R.layout.date_item, container, false);
                    dateContainer.setonDateChangeListener(new DateItem.DateChangeListener() {
                        @Override
                        public void changeDate() {
                            time.setText(myTime.year + "-" + (myTime.month + 1) + "-" + myTime.monthDay);
                        }
                    });
                    dateContainer.setTag(myTime.weekDay);
                    date.add(dateContainer);
                }
                if (date.size() == 1) {
                    myTime = dateContainer.refresh(myTime);
                }
                container.addView(dateContainer);
                return dateContainer;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        datePick.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int prePage;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DateItem dateItem = date.get(position);
                int weekDay = (int) dateItem.getTag();
                if (weekDay != myTime.weekDay) {
                    dateItem.setTag(myTime.weekDay);
                    dateItem.weeks.get(weekDay).setTextColor(0xff000000);
                    dateItem.weeks.get(myTime.weekDay).setTextColor(0xffff0000);
                }
                //更新monthday
                if (position + 1 == date.size()) {
                    myTime.monthDay = myTime.monthDay + 7;
                    myTime = dateItem.refresh(myTime);
                } else {
                    if (position > prePage) {
                        myTime.monthDay = myTime.monthDay + 7;
                        if (myTime.monthDay > myTime.getActualMaximum(Time.MONTH_DAY)) {
                            if (myTime.month > 11) {
                                myTime.year++;
                                myTime.month = 0;
                            }
                            myTime.monthDay = myTime.monthDay - myTime.getActualMaximum(Time.MONTH_DAY);
                            myTime.month++;
                        }
                    } else if (position < prePage) {
                        myTime.monthDay = myTime.monthDay - 7;
                        if (myTime.monthDay <= 0) {
                            myTime.month--;
                            if (myTime.month < 0) {
                                myTime.year--;
                                myTime.month = 11;
                            }
                            myTime.monthDay = myTime.monthDay + myTime.getActualMaximum(Time.MONTH_DAY);
                        }
                    }
                }
                prePage = position;
                time.setText(myTime.year + "-" + (myTime.month + 1) + "-" + myTime.monthDay);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
