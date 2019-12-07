package com.ceshi.helloworld;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
    private PendingIntent mAlarmSender;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it=new Intent(context,PosLoginActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
        Toast.makeText(context,"我自启动成功了哈", Toast.LENGTH_LONG).show();
    }
}
