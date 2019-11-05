package com.ceshi.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;
import com.ceshi.helloworld.net.AllInterFace;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.HttpSendRequest;
import com.ceshi.helloworld.net.MyDatabaseHelper;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 没有在清单文件注册 现在应该好了
 * */
public class PosLoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private  SQLiteDatabase querydb;

    private Call<getdeviceinfoEntity> getdeviceinfoEntityCall;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //在这里需要检测，是否已经初始化过，即检查dblite是否存在，存在的话，直接跳转到首界面
        //首先创建出数据库  KhdaInfo ，用于保存信息
        try {
            dbHelper = new MyDatabaseHelper(this, "KhdaInfo.db", null, 1);
            querydb = dbHelper.getWritableDatabase();

        }
        catch(Exception ex){
            //如果创建异常
            Toast.makeText(PosLoginActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        InitData(querydb);

        setContentView(R.layout.activity_l_login);  //设置页面
        if (CommonData.khid!=""){
            //说明已经初始化过了 ，直接跳转到欢迎界面
            setContentView(R.layout.activity_getphone);  //设置页面*//*
            return;
        }



        //否则的话就需要登录，然后绑定相应的登陆事件
        Button Login = findViewById(R.id.Login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {


                //创建成功之后进行写数据
                TextView inputmachine = findViewById(R.id.inputmachine);
                TextView  inputkhid= findViewById(R.id.inputkhid);

                String   s_inputmachine=inputmachine.getText().toString();
                String   s_inputkhid=inputkhid.getText().toString();



                if (s_inputmachine.length()==0||s_inputkhid.length()==0)
                {
                    Toast.makeText(PosLoginActivity.this, "请输入门店号或者设备号", Toast.LENGTH_LONG).show();
                    return;
                }


                //传入门店编号和设备号，进行注册
                getdeviceinfoEntityCall  = RetrofitHelper.getInstance().getdeviceinfobyselfhelpdeviceid(
                        s_inputkhid,
                        s_inputmachine);

                getdeviceinfoEntityCall.enqueue(new Callback<getdeviceinfoEntity>() {
                    @Override
                    public void onResponse(Call<getdeviceinfoEntity> call, Response<getdeviceinfoEntity> response) {
                        if (response != null) {

                            String  use_khid="";
                            String  storeName="";

                            getdeviceinfoEntity body = response.body();

                            int nCode= body.getReturnX().getNCode();

                            Log.e("zhoupan","nCode = "+nCode);

                            if (nCode!=0){
                                String message=body.getReturnX().getStrText();
                                Toast.makeText(PosLoginActivity.this, message, Toast.LENGTH_LONG).show();

                                Log.e("本次返回失败",message);
                                return;
                            }

                            //成功的话，拿到门店的值
                            getdeviceinfoEntity.ResponseBean response1 = body.getResponse();
                            use_khid=response1.getStoreId();


                            //然后根据获取到的 门店的值 ，进行下一次门店商户号的获取




                        }
                    }

                    @Override
                    public void onFailure(Call<getdeviceinfoEntity> call, Throwable t) {

                    }
                });

















                String  use_khid="";

                String useresult="";





                //String Result=AllInterFace.getdeviceinfobyselfhelpdeviceid(inputkhid.getText().toString(),inputmachine.getText().toString());
                //解析门店信息，然后用返回的门店基础信息进行保存

                String  storeName="";
                //注册成功之后 ，拿到 返回的门店编号之后，进行获取微信商户号
                //AllInterFace.getMchIdByStoreId(use_khid);
                //解析出商户号编号
                String  mch_id="";


                //然后将 数据 写入到 本地数据库中
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("khid", use_khid);
                values.put("khsname", storeName);
                values.put("machine_number", inputmachine.getText().toString());
                values.put("app_version", inputmachine.getText().toString());
                values.put("date_lr", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").toString());
                values.put("mch_id", mch_id);

                db.insert("Book", null, values);
                values.clear();
                //写数据成功之后 ，跳转到主页

                setContentView(R.layout.activity_getphone);  //设置页面*//*

            }
        });
    }






    public  void  InitData(SQLiteDatabase querydb) {

        //从本地取出数据来

        Cursor cursor = querydb.query("Khda", null, null, null, null, null, null);

        // 查询Book表中所有的数据
        if (null!=cursor) {
            if(cursor.moveToFirst()) {

                // 遍历Cursor对象，取出数据并打印
                do {
                    String khid = cursor.getString(cursor.getColumnIndex("khid"));

                    String appVersion = cursor.getString(cursor.getColumnIndex("app_version"));

                    String khsname = cursor.getString(cursor.getColumnIndex("khsname"));

                    //然后获取到本地文件的版本，判断是否需要升级
                    String  newversion=getAppVersion(this);

                    if (newversion!=newversion){
                        //调用接口，拿到升级地址进行升级

                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                String Result=AllInterFace.getdeviceinfobyselfhelpdeviceid("11","222");
                            }
                        }).start();

                    }

                    String machine_number = cursor.getString(cursor.getColumnIndex("machine_number"));

                    CommonData.khid = khid;
                    CommonData.machine_number = machine_number;
                    CommonData.app_versio=appVersion;
                    CommonData.machine_name=khsname;

                }
                while
                (cursor.moveToNext());
            }

            cursor.close();
        }
    }


    //获取程序的版本号
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

}




