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
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private  String  printpaytype="";
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

            TextView ordernumber = findViewById(R.id.ordernumber);
            ordernumber.setText(CommonData.ordernumber);


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

            TextView paytype = findViewById(R.id.paytype);
            paytype.setText(printpaytype);
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

       /* time=(TextView) findViewById(R.id.tv_time);
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

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day=form.format(date);



        String Str="                   欢迎光临                   "+"\n";
        //Str+="门店编号:"+CommonData.inputkhid+"       门店名称:"+CommonData.machine_name+"\n";
        Str+="门店名称:"+CommonData.machine_name+"\n";

        Str+="流水号："+CommonData.outTransId+"     "+"\n";
        //Str+="商户订单号："+CommonData.outTransId+"     "+"\n";
        Str+="日期   "+day+"     "+"\n";
        Str+="==============================================="+"\n";
        Str+="条码     名称     数量        单价     金额"+"\n";

        for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()) {

            String  barcode=entry.getValue().get(0).getBarcode();
            String sname=entry.getValue().get(0).getPluName();
            String  qty="0";
            String weight=entry.getValue().get(0).getNweight();
            String   dj="0";
            if (weight.equals("0")||weight.equals("0.0")||weight.equals("0.00")){
                //说明重量是 0.  那就取显示数量
                qty = String.valueOf(entry.getValue().get(0).getPackNum());
                dj=String.valueOf(entry.getValue().get(0).getMainPrice());

            }else {
                //净重含量存在值 则显示重量
                qty = entry.getValue().get(0).getNweight();
                //根据 实际售价/重量  计算  单价
                double a = entry.getValue().get(0).getMainPrice();
                try{
                dj = txfloat(a, Double.valueOf(qty));}
                catch(Exception ex)
                {

                }
            }

            String zj=entry.getValue().get(0).getRealPrice();
            Str+= barcode+"\n";
            Str+= sname+"     "+qty+"     "+dj+"    "+zj+"  "+"\n";
        }

        //付款方式
        Str+="==============================================="+"\n";
        Str+="付款方式             金额          总折扣"+"\n";

        Str+=printpaytype+"             "+CommonData.orderInfo.totalPrice+ "      "+CommonData.orderInfo.totalDisc+"\n";

        Str+="总数量         应收        找零"+"\n";
        Str+=""+CommonData.orderInfo.totalCount+"             "+CommonData.orderInfo.totalPrice+"          0.00     "+"\n";


        if (  CommonData.hyMessage!=null) {
            //如果会员信息不等于null 的，则需要打印会员基础信息
            Str += "会员卡号:" + CommonData.hyMessage.cardnumber + "\n";
            Str += "本次消费获得会员积分：" + CommonData.get_jf+ "\n";

        }


        //获取当前时间并 打印时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        Date newdate = new Date(System.currentTimeMillis());

        Str+="====================="+simpleDateFormat.format(newdate)+"==================="+"\n";
        Str+="            谢谢惠顾，请妥善保管小票         "+"\n";
        Str+="               开正式发票，当月有效              "+"\n";

        try {
            if (PrinterAPI.SUCCESS == mPrinter.connect(io)) {
                Toast.makeText(FinishActivity.this, "链接成功", Toast.LENGTH_SHORT).show();
                try {
                    if (PrinterAPI.SUCCESS == mPrinter.printString(Str, "GBK", true)) {
//                                Thread.sleep(time);
                    } else {
                        Toast.makeText(FinishActivity.this, "，小票打印失败,请重试", Toast.LENGTH_SHORT).show();
//                                handler.sendEmptyMessage(1);
                    }

                    mPrinter.printFeed();
                    //清楚打印缓存区 避免对后面数据的影响 有些设置会恢复默认值
                    mPrinter.init();
                    mPrinter.cutPaper(66, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                    //printstate.setText("小票打印失败,请重试");
                }
            }
        }
        catch(Exception ex){
            ToastUtil.showToast(FinishActivity.this, "打印异常通知", "请检查当前打印机连接");
        }
    }



    /**
     * TODO 除法运算，保留小数
     * @author 袁忠明
     * @date 2018-4-17下午2:24:48
     * @param num1 被除数
     * @param num2 除数
     * @return 商
     */
    public static String txfloat(double num1,double num2) {
        // TODO 自动生成的方法存根

        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数

        return df.format((float)num1/num2);

    /*   BigDecimal  bignum = new  BigDecimal(num1/num2);
        double myNum3 = bignum.setScale(2, java.math.BigDecimal.ROUND_CEILING).doubleValue();

        return myNum3;*/
    }


}
