package com.example.webclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Aozaki_Shiro on 5/26/2014.
 */
public class DailyRecord extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_record);

        tvDailyRecordDate = (TextView) this.findViewById(R.id.daily_record_date);
        lstDailyRecordList = (ListView) this.findViewById(R.id.daily_record_list);


    }

    private TextView tvDailyRecordDate;
    private ListView lstDailyRecordList;

    String[] des = {"饭前", "", "饭后"};
    String[] time = {"7:00", "9:00", "13:00"};

}

class MyAdapter extends BaseAdapter{

    String[] description;
    String[] times;

    public MyAdapter(String[] des, String[] time){
        description = des;
        times = time;
    }

    @Override
    public int getCount() {
        return description.length;
    }

    @Override
    public Object getItem(int i) {
        return times[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }


}