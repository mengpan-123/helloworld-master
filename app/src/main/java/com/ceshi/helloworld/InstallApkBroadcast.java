package com.ceshi.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class InstallApkBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        install(context);
    }

    private void install(Context context) {

        Intent installintent = new Intent();

        installintent.setAction(Intent.ACTION_VIEW);
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String  pathapk=Environment.getExternalStorageDirectory() + "/MyApp1";

        //pathapk=Environment.getExternalFilesDir();

        //Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);


        Toast.makeText(context, pathapk, Toast.LENGTH_SHORT).show();

        installintent.setDataAndType(Uri.fromFile(new File(pathapk)),
                "application/vnd.android.package-archive");

        ////存储位置为Android/data/包名/file/Download文件夹

        context.startActivity(installintent);
    }
}