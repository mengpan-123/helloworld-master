package com.ceshi.helloworld;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class BootReceiver extends BroadcastReceiver {
    private PendingIntent mAlarmSender;
    @Override
    public void onReceive(Context context, Intent intent) {
       /* Intent it=new Intent(context,PosLoginActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
        Toast.makeText(context,"我自启动成功了哈", Toast.LENGTH_LONG).show();*/


        //Toast.makeText(context, "我自启动成功了哈", Toast.LENGTH_LONG).show();
        //Log.d("XRGPS", "BootReceiver.onReceive: " + intent.getAction());

        if (ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent mBootIntent = new Intent(context, PosLoginActivity.class);
            mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        } else {
            Log.w("XRGPS", "BootReceiver: unsupported action");
        }

    }
}
