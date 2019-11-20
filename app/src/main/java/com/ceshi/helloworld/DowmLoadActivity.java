package com.ceshi.helloworld;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.DownLoadRunnable;
import com.ceshi.helloworld.net.DownloadDialog;

import java.io.File;
import java.net.URL;

import retrofit2.http.Url;

public class DowmLoadActivity extends AppCompatActivity {


    private DownloadDialog downloadDialog;
    private DownloadManager mDownloadManager;
    private String url = "http://192.168.0.108/222/MyApp1.apk";




    /*Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DownloadManager.STATUS_SUCCESSFUL:
                    downloadDialog.setProgress(100);
                    canceledDialog();
                    Toast.makeText(DowmLoadActivity.this, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                    break;

                case DownloadManager.STATUS_RUNNING:
                    //int progress = (int) msg.obj;
                    downloadDialog.setProgress((int) msg.obj);
                    //canceledDialog();
                    break;

                case DownloadManager.STATUS_FAILED:
                    canceledDialog();
                    break;

                case DownloadManager.STATUS_PENDING:
                    showDialog();
                    break;
            }
        }
    };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.down_exe);  //设置页面




    }


    public void onClick(View v) {
       // download();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://192.168.0.108/222/MyApp1.apk"));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("任务进行中");
        request.setDescription("zhoupan 的测试apk正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "MyApp1");

        DownloadManager downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);

        queryDownloadProgress(this,id,downManager);


    }


   /* private void download() {
        showDialog();
        //最好是用单线程池，或者intentService取代
        new Thread(new DownLoadRunnable(this,url, handler)).start();
    }



    private void showDialog() {
        if(downloadDialog==null){
            downloadDialog = new DownloadDialog(this);
        }

        if(!downloadDialog.isShowing()){
            downloadDialog.show();
        }
    }

    private void canceledDialog() {
        if(downloadDialog!=null&&downloadDialog.isShowing()){
            downloadDialog.dismiss();
        }
    }*/

    private void queryDownloadProgress(Context   context,long requestId, DownloadManager downloadManager) {


        DownloadManager.Query query=new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging=true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {

                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging=false;

                            //调用安装方法
                            Uri downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);
                            if (downloadFileUri != null) {
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(install);

                            }

                            //handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging=false;
                            //handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            //handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            //handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            //handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if(cursor!=null){
                    cursor.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void  InstallApk(Context context, File file){
        Uri  fileuri= Uri.fromFile(file);
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }


}
