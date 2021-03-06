package com.ceshi.helloworld;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.UpdateVersionEntity;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.DownloadDialog;
import com.ceshi.helloworld.net.MyDatabaseHelper;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.ToastUtil;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 没有在清单文件注册 现在应该好了
 * */
public class PosLoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase querydb;

    private Call<getdeviceinfoEntity> getdeviceinfoEntityCall;

    private Call<StoreIdEntity> StoreIdEntityCall;

    private Call<UpdateVersionEntity> UpdateVersionEntityCall;

    private String use_khid = "";

    private String storeName = "";

    private String mch_id = "";

    private String sub_mch_id = "";

    private String lCorpId = "";  //这个用户会员的验证信息

    private String corpId = "";   //用于用户会员的验证信息

    private String userId = "";
    private String app_version = "";

    private String s_inputmachine = "";

    private String s_inputkhid = "";


    private int click_num = 0;


    /*//以下是和下载相关的*/

    private DownloadDialog downloadDialog;
    private DownloadManager mDownloadManager;
    private String url = "http://192.168.0.108/222/MyApp1.apk";

    public  String  Tag="PosLoginActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.0  首先创建出数据库  BaseInfo ，用于保存信息
        try {
            dbHelper = new MyDatabaseHelper(this, "BaseInfo2.db", null, 1);
            querydb = dbHelper.getWritableDatabase();

        } catch (Exception ex) {
            //如果创建异常
            //Toast.makeText(PosLoginActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            ToastUtil.showToast(PosLoginActivity.this, "异常通知", "初始化本地Sqlite信息失败");
            return;
        }

        app_version=getAppVersion(this);
        CommonData.app_version = app_version;

        //2.0  先从本地选取初始化数据，如果拿到了，说明初始化过，则直接跳转，跳过登录
        InitData(querydb);

        //3.0  比较 app  版本号信息，是否需要升级
        PrepareUpdateVersion();


        if (!CommonData.khid.equals("") && !CommonData.userId.equals("")) {
            //说明已经初始化过了 ，直接跳转到欢迎的首界面
             //显示跳转
            Intent intent = new Intent(PosLoginActivity.this, NewIndexActivity.class);
            startActivity(intent);
            return;
        }
        else
        {
            setContentView(R.layout.activity_l_login);  //设置页面

            EditText edt = findViewById(R.id.inputmachine);
            edt.setText(CommonData.machine_number);
            edt.setCursorVisible(false);
            edt.setFocusable(false);
            edt.setFocusableInTouchMode(false);

        }




        //否则的话就需要登录，然后绑定相应的登陆事件
        Button Login = findViewById(R.id.Login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //创建成功之后进行写数据
                TextView inputmachine = findViewById(R.id.inputmachine);
                TextView inputkhid = findViewById(R.id.inputkhid);

                s_inputmachine = inputmachine.getText().toString();
                s_inputkhid = inputkhid.getText().toString();

                CommonData.inputkhid=s_inputkhid;

                if (s_inputmachine.length() == 0 || s_inputkhid.length() == 0) {
                    ToastUtil.showToast(PosLoginActivity.this, "输入消息通知", "请输入完整的门店号或者设备号");
                    return;
                }
                //传入门店编号和设备号，进行注册
                getdeviceinfoEntityCall = RetrofitHelper.getInstance().getdeviceinfobyselfhelpdeviceid(
                        s_inputkhid,
                        s_inputmachine);

                getdeviceinfoEntityCall.enqueue(new Callback<getdeviceinfoEntity>() {
                    @Override
                    public void onResponse(Call<getdeviceinfoEntity> call, Response<getdeviceinfoEntity> response) {

                        if (response != null) {
                            getdeviceinfoEntity body = response.body();
                            if(null!=body) {
                                int nCode = body.getReturnX().getNCode();

                                if (nCode != 0) {
                                    String message = body.getReturnX().getStrText();

                                    ToastUtil.showToast(PosLoginActivity.this, "输入消息通知", message);
                                    return;
                                }
                            }
                            else {
                                ToastUtil.showToast(PosLoginActivity.this, "接口异常", "接口访问异常，请及时处理");
                                return;
                            }

                            //成功的话，拿到门店的值
                            getdeviceinfoEntity.ResponseBean response1 = body.getResponse();
                            use_khid = response1.getStoreId();
                            storeName = response1.getStoreName();
                            lCorpId = response1.getLCorpId();
                            corpId = response1.getCorpId();
                            userId = response1.getUserId();


                            //然后根据获取到的 门店的值 ，进行下一次门店商户号的获取
                            StoreIdEntityCall = RetrofitHelper.getInstance().getStoreId(use_khid);
                            StoreIdEntityCall.enqueue(new Callback<StoreIdEntity>() {
                                @Override
                                public void onResponse(Call<StoreIdEntity> call, Response<StoreIdEntity> response) {
                                    if (response != null) {
                                        StoreIdEntity body = response.body();
                                        if(null!=body) {
                                            //这里就直接拿到后台返回的对象StoreIdEntity  比如我要取return下的code
                                            StoreIdEntity.ReturnBean returnX = body.getReturnX();

                                            int nCode = returnX.getNCode();
                                            if (nCode == 0) {
                                                StoreIdEntity.ResponseBean response1 = body.getResponse();

                                                mch_id = response1.getMch_id();
                                                sub_mch_id = response1.getSub_mch_id();

                                                //因为请求是异步的。以上基础信息写完之后 在进行操作


                                                WirttenDataToSqlite();
                                            }
                                        }else {
                                            ToastUtil.showToast(PosLoginActivity.this, "接口异常", "接口访问异常，请及时处理");
                                            return;
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<StoreIdEntity> call, Throwable t) {

                                }
                            });


                        }
                    }

                    @Override
                    public void onFailure(Call<getdeviceinfoEntity> call, Throwable t) {

                    }
                });
            }
        });
    }


    public void InitData(SQLiteDatabase querydb) {

        //从本地取出数据来

        Cursor cursor = querydb.query(CommonData.tablename, null, null, null, null, null, null);

        // 查询Book表中所有的数据
        if (null != cursor) {
            if (cursor.moveToFirst()) {

                // 遍历Cursor对象，取出数据并打印
                do {
                    CommonData.khid = cursor.getString(cursor.getColumnIndex("khid"));
                    CommonData.lCorpId = cursor.getString(cursor.getColumnIndex("lCorpId"));

                    CommonData.corpId = cursor.getString(cursor.getColumnIndex("corpId"));

                    CommonData.machine_name = cursor.getString(cursor.getColumnIndex("khsname"));

                    CommonData.mch_id = cursor.getString(cursor.getColumnIndex("mch_id"));
                    //用户信息
                    CommonData.userId = cursor.getString(cursor.getColumnIndex("userId"));


                    CommonData.inputkhid=cursor.getString(cursor.getColumnIndex("sub_mch_id"));

                    //CommonData.machine_number = cursor.getString(cursor.getColumnIndex("machine_number"));

                }
                while
                (cursor.moveToNext());
            }

            cursor.close();
        }
    }


    //获取程序的版本号
    public static int getAppVersioncode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
    //版本号名称
    public static String getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        String code = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public void WirttenDataToSqlite() {

        try {
            //然后将 以上获取到的 数据 写入到 本地数据库中
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("khid", use_khid);
            values.put("khsname", storeName);
            values.put("corpId", corpId);
            values.put("lCorpId", lCorpId);
            values.put("userId", userId);
            values.put("machine_number", CommonData.machine_number);
            values.put("app_version", app_version);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(date);

            values.put("date_lr", today);
            values.put("mch_id", mch_id);
            values.put("sub_mch_id", CommonData.inputkhid); //2019-12-07暂时存储这个字段
            values.put("number", 1);

            db.insert(CommonData.tablename, null, values);

            values.clear();

        }
        catch (Exception ex) {

        }

        //写数据成功之后 ，跳转到主页.所有字段赋值便于以后使用

        CommonData.khid = use_khid;
        CommonData.machine_name = storeName;
        CommonData.userId = userId;
        CommonData.mch_id = mch_id;
        CommonData.sub_mch_id = sub_mch_id;
        CommonData.lCorpId = lCorpId;
        CommonData.corpId = corpId;
        //CommonData.machine_number = s_inputmachine;
        CommonData.app_version = app_version;

        //显示跳转
        Intent intent = new Intent(PosLoginActivity.this, NewIndexActivity.class);
        startActivity(intent);
    }



    //准备预升级
    public   void PrepareUpdateVersion(){

        try {
            String info="";

            if (CommonData.machine_name.equals("")){
                info="未登录";
            }
            else{
                info=CommonData.machine_name;
            }

            UpdateVersionEntityCall = RetrofitHelper.getInstance().UpdateVersion(info, "selfdevice", CommonData.khid);
            UpdateVersionEntityCall.enqueue(new Callback<UpdateVersionEntity>() {
                @Override
                public void onResponse(Call<UpdateVersionEntity> call, Response<UpdateVersionEntity> response) {
                    if (response != null) {
                        UpdateVersionEntity body = response.body();
                        if (null!=body) {
                            if (body.getReturnX().getNCode() == 0) {
                                //接口返回的版本号，进行比较，然后拿到URL进行下载
                                String returnversion=body.getResponse().getAppVersion();

                                returnversion=returnversion.replace(".","");

                                //获取到本地文件的版本，判断是否需要升级
                                int newversion = getAppVersioncode(PosLoginActivity.this);

                                url=body.getResponse().getUpdatePath();

                                if (Double.valueOf(returnversion)>newversion) {
                                    EnsureUPdate();
                                }

                            }
                            else{
                                ToastUtil.showToast(PosLoginActivity.this, "接口返回", body.getReturnX().getStrInfo());
                                return;
                            }
                        }
                        else {
                            ToastUtil.showToast(PosLoginActivity.this, "接口返回", "接口请求异常,请稍后重试");
                            return;
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateVersionEntity> call, Throwable t) {
                    ToastUtil.showToast(PosLoginActivity.this, "接口异常", "接口请求异常");
                    return;
                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(PosLoginActivity.this, "输入消息通知", ex.toString());
        }
    }



    public   void  EnsureUPdate(){

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置在什么网络情况下进行下载
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("任务进行中");
        request.setDescription("应用程序正在下载中");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "AIINBI.apk");

        DownloadManager downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);

        //确认要下载时 ，先删除 sqlite里面的表数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CommonData.tablename,null,null);


        queryDownloadProgress(this,id,downManager);
    }


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
                            Uri downloadFileUri;
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            //调用安装方法,进行自动升级
                            //Uri downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);
                            //Uri downloadFileUri = DownloadManager.COLUMN_LOCAL_URI;
                            boolean haveInstallPermission;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                                downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);

                            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                            { // 6.0 - 7.0
                                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                File apkFile = new File(Uri.parse(uriString).getPath());
                                downloadFileUri = Uri.fromFile(apkFile);

                            } else { // Android 7.0 以上

                                //haveInstallPermission = getPackageManager().canRequestPackageInstalls();  //需要 level版本支持


                                downloadFileUri = FileProvider.getUriForFile(context,
                                        "com.ceshi.helloworld.fileProvider",
                                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "AIINBI.apk"));
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }


                            if (downloadFileUri != null) {

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
}






