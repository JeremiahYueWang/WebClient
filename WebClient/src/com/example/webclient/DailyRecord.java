package com.example.webclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Aozaki_Shiro on 5/26/2014.
 */
public class DailyRecord extends Activity {

    static private int index = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_record);

        tvDailyRecordDate = (TextView) this.findViewById(R.id.daily_record_date);
        lstDailyRecordList = (ListView) this.findViewById(R.id.daily_record_list);

        lstDailyRecordList.setAdapter(new MyAdapter(time, des));
        lstDailyRecordList.setOnItemClickListener(new  AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvTime = (TextView) view.findViewById(R.id.daily_record_item_time);
                TextView tvDescription = (TextView) view.findViewById(R.id.daily_record_item_timedescription);

                Toast.makeText(DailyRecord.this, tvTime.getText().toString()+" "+tvDescription.getText().toString(), 1000).show();
                DailyRecord.index = i;

            }
        });

    }

    private TextView tvDailyRecordDate;
    private ListView lstDailyRecordList;

    String[] time = {"7:00", "9:00", "13:00", "7:00", "9:00", "13:00", "7:00", "9:00", "13:00", "7:00", "9:00", "13:00"};
    String[] des = {"饭前", "", "饭后", "饭前", "", "饭后", "饭前", "", "饭后", "饭前", "", "饭后"};


    class MyAdapter extends BaseAdapter{

        String[] description;
        String[] times;

        public MyAdapter( String[] time,String[] des){

            times = time;
            description = des;

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

            if(view == null) {

                LayoutInflater inflater = (LayoutInflater) DailyRecord.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View recordItem = inflater.inflate(R.layout.daily_record_item, null);
                TextView tvTime = (TextView) recordItem.findViewById(R.id.daily_record_item_time);
                TextView tvDescription = (TextView) recordItem.findViewById(R.id.daily_record_item_timedescription);
                Button btnDelete = (Button) recordItem.findViewById(R.id.daily_record_item_delete);
                tvTime.setText(times[i]);
                tvDescription.setText(description[i]);


                recordItem.setOnTouchListener(new View.OnTouchListener() {
                    int UpX, DownX;
                    Context context = DailyRecord.this;
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch(motionEvent.getAction())//根据动作来执行代码
                        {
                            case MotionEvent.ACTION_MOVE://滑动
                                Toast.makeText(context, "move...", 1000).show();
                                break;
                            case MotionEvent.ACTION_DOWN://按下
                                Toast.makeText(context, "down...", 1000).show();
                                DownX = (int) motionEvent.getX();
                                break;
                            case MotionEvent.ACTION_UP://松开
                                UpX = (int) motionEvent.getX();
                                Toast.makeText(context, "up..." + Math.abs(UpX-DownX), 1000).show();
                                if(Math.abs(UpX-DownX) > 20){
                                    btnDelete.setVisibility(View.VISIBLE);
                                }
                                break;
                            default:
                        }
                        Button buttonDelete = new Button(DailyRecord.this);
                        buttonDelete.setEnabled(true);
                        buttonDelete.setText("delete");


                        return true;
                    }
                });

                return recordItem;

            }else{

                TextView tvTime = (TextView) view.findViewById(R.id.daily_record_item_time);
                TextView tvDescription = (TextView) view.findViewById(R.id.daily_record_item_timedescription);
                tvTime.setText(times[i]);
                tvDescription.setText(description[i]);
                return view;

            }

        }

    }

}