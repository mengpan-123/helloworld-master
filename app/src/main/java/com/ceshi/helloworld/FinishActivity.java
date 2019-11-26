package com.ceshi.helloworld;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.MyDatabaseHelper;
import com.ceshi.helloworld.net.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class FinishActivity extends AppCompatActivity  {
    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;


    //public  MediaPlayer player1=CommonData.player;  //初始化播放音乐对象

    private TextToSpeech textToSpeech = null;//创建自带语音对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finishpay);  //设置页面




        //进入之后设置界面的总金额
//        try {
//            TextView totalmoney = findViewById(R.id.totalmoney);
//
//            totalmoney.setText("￥" + String.valueOf(CommonData.orderInfo.totalPrice));
//        }
//        catch(Exception ex)
//        {
//
//        }

        //播放 支付成功的语音提示
        /*textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);

                    textToSpeech.speak("支付已完成，请拿走商品和小票，祝您购物愉快",//输入中文，若不支持的设备则不会读出来
                            TextToSpeech.QUEUE_FLUSH, null);

                } else {
                    Toast.makeText(FinishActivity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });*/


        CommonData.player.reset();
        CommonData.player=MediaPlayer.create(this,R.raw.finishpay);
        CommonData.player.start();
        CommonData.player.setLooping(false);


        //倒计时30s立即返回到 首界面

        /*time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();*/



        //一旦支付成功，这个参数就自动加上1
        CommonData.number=CommonData.number+1;
        //1.0  首先  更新本地数据库的记录号，避免机器无端关机导致其等于0
        try {
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BaseInfo2.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("number", CommonData.number);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(date);

            values.put("date_lr", today);

            db.update(CommonData.tablename,values,"khid= ?",new String[] { CommonData.khid });


        } catch (Exception ex) {
            //如果创建异常,不能影响其他

        }




        /**
         * Created by zhoupan on 2019/11/8.
         * 点此立即返回到首页
         * */
        /*TextView comback=findViewById(R.id.comback);


        comback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });*/

    }
    /**
     * 返回首页
     */
    public void return_home(View view) {
        Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
        startActivity(intent);
    }


    public void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (i > 0) {   //加入判断不能小于0
                    i--;
                    Message message = mHandler.obtainMessage();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                }else {
                    Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.schedule(task, 1000);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            time.setText(msg.arg1 + "");
            startTime();
        };
    };
}
