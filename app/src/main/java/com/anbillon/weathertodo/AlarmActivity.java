package com.anbillon.weathertodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlarmActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent toSomeAcitivty = new Intent(this,TextActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,0,toSomeAcitivty,0);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // 5秒后发送广播
                long triggerAtTime = SystemClock.elapsedRealtime() + 10 * 1000;

                /**
                 * set(int type，long startTime，PendingIntent pi)；
                 * 设置一次性闹钟
                 *      第一个参数：闹钟类型
                 *      第二个参数：闹钟执行的时间
                 *      第三个参数：闹钟响应动作
                 *
                 *    5秒后发送广播
                 */
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pendingIntent);
            }
        });




        /**
         * setRepeating(int type，long startTime，long intervalTime，PendingIntent pi)；
         * 设置重复闹钟
         *      参数1：闹钟类型
         *      参数2：闹钟首次执行时间
         *      参数3：闹钟两次执行的时间间隔
         *      参数4：闹钟响应动作
         *
         *    5秒后发送广播，然后每隔10秒重复发送
         */
        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //triggerAtTime, 10 * 1000, pendingIntent);


        /**
         * setInexactRepeating（int type，long startTime，long intervalTime，PendingIntent pi）；
         * 同上：设置重复闹钟  间隔时间不固定，更节能，系统可能将几个差不多的闹钟合并为一个执行,减少唤醒次数
         *      参数3：intervalTime
         *          INTERVAL_FIFTEEN_MINUTES
         *          INTERVAL_HALF_HOUR
         *          INTERVAL_HOUR
         *          INTERVAL_HALF_DAY
         *          INTERVAL_DAY
         */
        //alarmManager.setInexactRepeating();

        /**
         * 闹钟取消
         */
        //alarmManager.cancel(pendingIntent);

        /**
         * 闹钟类型：
         *      AlarmManager.ELAPSED_REALTIME：        闹钟在手机睡眠状态下不可用，该状态下闹钟使用相对时间（相对于系统启动开始, SystemClock.elapsedRealtime()），状态值为3；
         *      AlarmManager.ELAPSED_REALTIME_WAKEUP： 闹钟在手机睡眠状态下回唤醒系统并执行提示功能，该状态下闹钟也使用相对时间，状态值为2；
         *      AlarmManager.RTC：                     闹钟在睡眠状态下不可用，改状态下闹钟使用绝对时间，即当前系统时间，状态值为1；
         *      AlarmManager.RTC_WAKEUP：              闹钟在睡眠状态下回唤醒系统并执行提示功能，使用绝对时间，状态值为0；
         *      AlarmManager.POWER_OFF_WAKEUP：        闹钟在手机关机状态下也能正常进行提示功能，使用最多，使用绝对时间(System.currentTimeMillis())，状态值为4，受SDK版本影响。
         */

    }
}
