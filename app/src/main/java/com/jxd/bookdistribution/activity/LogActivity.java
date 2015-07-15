package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jxd.bookdistribution.R;

import java.util.Calendar;

public class LogActivity extends Activity implements View.OnClickListener {
    EditText metStart=null;
    EditText metEnd=null;
    Button mbtnQuery=null;
    Button mbtnBack=null;
    TextView mTvTitle=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mTvTitle = (TextView)this.findViewById(R.id.tvAppTitle);
        mTvTitle.setText(getString(R.string.menuLog));

        metStart=(EditText)this.findViewById(R.id.etStart);
        metEnd=(EditText)this.findViewById(R.id.etEnd);

        metStart.setOnClickListener(this);
        metEnd.setOnClickListener(this);

        mbtnQuery=(Button)this.findViewById(R.id.btnLogQuery);
        mbtnQuery.setOnClickListener(this);

        mbtnBack=(Button)this.findViewById(R.id.btnLogBack);
        mbtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if( view == metStart){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View pView = View.inflate(this, R.layout.layoutdatedialog , null);
            final DatePicker datePicker = (DatePicker) pView.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) pView.findViewById(R.id.time_picker);
            builder.setView( pView );

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            builder.setTitle("选取起始时间");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    StringBuffer sb = new StringBuffer();
                    sb.append(String.format("%d-%02d-%02d",
                            datePicker.getYear(),
                            datePicker.getMonth() + 1,
                            datePicker.getDayOfMonth()));
                    sb.append("  ");
                    sb.append(timePicker.getCurrentHour())
                            .append(":").append(timePicker.getCurrentMinute());

                    metStart.setText(sb);
                    metEnd.requestFocus();
                    dialog.cancel();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();

        }else if( view == metEnd){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View pView = View.inflate(this, R.layout.layoutdatedialog , null);
            final DatePicker datePicker = (DatePicker) pView.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) pView.findViewById(R.id.time_picker);
            builder.setView( pView );

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            builder.setTitle("选取结束时间");
            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    StringBuffer sb = new StringBuffer();
                    sb.append(String.format("%d-%02d-%02d",
                            datePicker.getYear(),
                            datePicker.getMonth() + 1,
                            datePicker.getDayOfMonth()));
                    sb.append("  ");
                    sb.append(timePicker.getCurrentHour())
                            .append(":").append(timePicker.getCurrentMinute());
                    metEnd.setText(sb);
                    dialog.cancel();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }else if( view == mbtnQuery){

            String start=metStart.getText().toString();
            String end=metEnd.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("starttime", start);
            intent.putExtra("endtime",end);
            intent.setClass(LogActivity.this, LogListActivity.class);
            LogActivity.this.startActivity(intent);
        }else if( view == mbtnBack){
            LogActivity.this.finish();
        }
    }
}
