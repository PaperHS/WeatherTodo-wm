package com.anbillon.weathertodo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class WriteNewTodoActivity extends AppCompatActivity {
    ThingTodoSqlHelper helper;
    TextView tvData,tvTime;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    EditText etContent;
    Switch aSwitch;
    Calendar calendar = Calendar.getInstance();
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_todo);
        helper = new ThingTodoSqlHelper(getApplicationContext());
        tvData = (TextView)findViewById(R.id.tv_data);
        tvTime = (TextView)findViewById(R.id.tv_time);
        etContent = (EditText)findViewById(R.id.et_content);
        aSwitch = (Switch)findViewById(R.id.sw_noti);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                tvData.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        },2017,4,21);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                tvTime.setText(hourOfDay+":"+minute);
            }
        },12,0,true);
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                timePickerDialog.show();
            }
        });
        Button btn = (Button)findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText())){
                    return;
                }
                if (aSwitch.isChecked()) {
                    Intent broadcast = new Intent("com.anbillon.weathertodo.demo");
                    broadcast.putExtra("info", "broadcast :hahaha");
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(WriteNewTodoActivity.this, 0, broadcast, 0);
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                    long triggerAtTime = SystemClock.elapsedRealtime() + 10 * 1000;
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                insertInfo(etContent.getText().toString(),calendar.getTimeInMillis(),aSwitch.isChecked()?1:0);
                ContentValues contentValues = new ContentValues();
                contentValues.put(ThingTodo.THING, etContent.getText().toString());
                contentValues.put(ThingTodo.TIME, calendar.getTimeInMillis());
                contentValues.put(ThingTodo.NOTI, aSwitch.isChecked()?1:0);
                getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
                WriteNewTodoActivity.this.finish();
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    public void insertInfo(String content,long time,int noti) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ThingTodo.THING, content);
        contentValues.put(ThingTodo.TIME, time);
        contentValues.put(ThingTodo.NOTI, noti);
        helper.getWritableDatabase().insert(ThingTodo.TABLE_NAME, null, contentValues);
    }

    public void delInfo(){
        helper.getWritableDatabase().delete(ThingTodo.TABLE_NAME,"gender = ? and name LIKE ?",new String[]{"1","%2%"});
    }

    public void queryInfo(){
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor c = sqLiteDatabase.query(ThingTodo.TABLE_NAME,new String[]{
            ThingTodo.THING,
                ThingTodo.TIME
        },ThingTodo.THING +" LIKE ?",new String[]{"%1"},null,null,null);
        c.moveToFirst();
        String s=c.getString(0);
        c.close();
    }

    public void updateInfo(){
        SQLiteDatabase db = helper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ThingTodo.THING, "123");

        // Which row to update, based on the title
        String selection = ThingTodo.THING + " LIKE ?";
        String[] selectionArgs = { "MyTitle" };
        int count = db.update(ThingTodo.TABLE_NAME, values,selection,selectionArgs);
    }
}
