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
import com.ceshi.helloworld.net.SplnfoList;
import com.ceshi.helloworld.net.ToastUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class FinishActivity extends AppCompatActivity  {
    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;

    public PrinterAPI mPrinter = PrinterAPI.getInstance();  //打印机部分


    //public  MediaPlayer player1=CommonData.player;  //初始化播放音乐对象

    private TextToSpeech textToSpeech = null;//创建自带语音对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finishpay);  //设置页面


        //进入之后设置界面的总金额
        try {
            TextView totalmoney = findViewById(R.id.havepaynet);

            totalmoney.setText("￥" + String.valueOf(CommonData.orderInfo.totalPrice));
        }
        catch(Exception ex)
        {

        }

        CommonData.player.reset();
        CommonData.player=MediaPlayer.create(this,R.raw.finishpay);
        CommonData.player.start();
        CommonData.player.setLooping(false);


        //打印
        print();

        //倒计时30s立即返回到 首界面

        time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();



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
        TextView comback=findViewById(R.id.backhome);


        comback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(FinishActivity.this, NewIndexActivity.class);
                startActivity(intent);
            }
        });

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
                    Intent intent = new Intent(FinishActivity.this, NewIndexActivity.class);
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



    protected void print()
    {
        InterfaceAPI io = null;
        io = new SerialAPI(new File(
                "/dev/ttyS1"),
                38400,
                1);
        StringBuffer sbb = new StringBuffer();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy－MM－dd HH:mm");//初始化Formatter的转换格式。


        String Str="          欢迎光临          "+"\n";
        Str+="     "+CommonData.machine_name+"     "+"\n";
        Str+="流水号："+CommonData.ordernumber+"     "+"\n";
        Str+="日期：     "+formatter+"     "+"\n";
        Str+="================================"+"\n";
        Str+=" 品名  数量   成交价   单价     金额"+"\n";



        for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()) {

            String  barcode=entry.getValue().get(0).getBarcode();
            String sname=entry.getValue().get(0).getPluName();
            double qty=entry.getValue().get(0).getPackNum();
            double dj=entry.getValue().get(0).getMainPrice();

            String zj=entry.getValue().get(0).getRealPrice();

            Str+= barcode+"   "+sname+" "+qty+"  "+dj+"  "+zj+" "+"\n";
        }

        //付款方式
        Str+="================================"+"\n";
        Str+="付款方式    金额   "+"\n";
        String  printpaytype="";
        //只会有一种支付方式
        if (CommonData.usepayway.equals("WXPaymentCodePay")){
            printpaytype="微信支付";
        }
        else if(CommonData.usepayway.equals("AliPaymentCodePay")){
            printpaytype="支付宝支付";
        }
        else{
            printpaytype="刷脸支付";
        }
        Str+=printpaytype+"  "+CommonData.orderInfo.totalPrice+"\n";

        Str+="应收      总数量     找零"+"\n";
        Str+=""+CommonData.orderInfo.totalPrice+"     "+CommonData.orderInfo.totalCount+"     0.00     "+"\n";


        //获取当前时间并 打印时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        Str+="==============="+simpleDateFormat.format(date)+"==============="+"\n";
        Str+="         谢谢惠顾，请妥善保管小票            "+"\n";
        Str+="         开正式发票，当月有效            "+"\n";

        if (PrinterAPI.SUCCESS == mPrinter.connect(io))
        {
            Toast.makeText(FinishActivity.this, "链接成功", Toast.LENGTH_SHORT).show();
            try {
                if (PrinterAPI.SUCCESS == mPrinter.printString(Str,"GBK", true))
                {
//                                Thread.sleep(time);
                }
                else
                {
                    Toast.makeText(FinishActivity.this, "，小票打印失败,请重试", Toast.LENGTH_SHORT).show();
//                                handler.sendEmptyMessage(1);
                }

                mPrinter.printFeed();
                //清楚打印缓存区 避免对后面数据的影响 有些设置会恢复默认值
                mPrinter.init();
                mPrinter.cutPaper(66, 0);

            }
            catch (Exception e)
            {
                e.printStackTrace();
                //printstate.setText("小票打印失败,请重试");
            }
        }
    }


}
