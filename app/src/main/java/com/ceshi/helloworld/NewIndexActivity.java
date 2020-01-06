package com.ceshi.helloworld;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.OrderDetailEntity;
import com.ceshi.helloworld.bean.OrderListEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.HyMessage;
import com.ceshi.helloworld.net.MyDatabaseHelper;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.ToastUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;
import com.tencent.wxpayface.WxPayFace;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewIndexActivity extends Activity {


    public boolean is_click=true;
    View layout=null;
    private  VideoView  video;

   public  String   Str="";
    private  Call<ClearCarEntity>  ClearCarEntityCall;

    private  Context  context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newindex);


        if (CommonData.khid.equals("")||CommonData.userId.equals("")){

            //如果异常之后获取不到 基本参数，就到登陆界面去，重新获取或重新登陆
            Intent intent = new Intent(NewIndexActivity.this, PosLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }



        video = (VideoView) findViewById(R.id.video);

        String path = Environment.getExternalStorageDirectory().getPath()+"/"+"index.mp4";//获取视频路径

        Uri uri = Uri.parse("http://www.ikengee.com.cn/test1/index.mp4");//将路径转换成uri
        video.setVideoURI(uri);//为视频播放器设置视频路径
        video.setMediaController(new MediaController(NewIndexActivity.this));//显示控制栏


        MediaController mc = new MediaController(this);
        mc.setVisibility(View.INVISIBLE);
        video.setMediaController(mc);

        video.start();


        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

        //设置底部的显示信息
        TextView storename=findViewById(R.id.storename);
        storename.setText("门店编号:"+ CommonData.khid);
        TextView storechinesename=findViewById(R.id.idnexstorename);
        storechinesename.setText(CommonData.machine_name);
        TextView appmachine=findViewById(R.id.appmachine);
        appmachine.setText("设备编号:"+ CommonData.machine_number);


        TextView appversion=findViewById(R.id.appversion);
        appversion.setText("Version : "+ CommonData.app_version);
        if (CommonData.app_version.equals("")){
            appversion.setText(getAppVersion(this));//因为出现过版本号未获取到的的问题，所以这么写
        }

        //从数据库取数据，如果万一数据有是从1 开始的话，说明可能 程序中断了，机器重启了，从数据库查找本地保存的已知 的流水号
        if (CommonData.number==1) {
            try {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BaseInfo2.db", null, 1);
                SQLiteDatabase querydb = dbHelper.getWritableDatabase();

                //获取今天
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(date);

                Cursor cursor = querydb.query(CommonData.tablename, null,null,null, null, null, null);

                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        String  get_days = cursor.getString(cursor.getColumnIndex("date_lr"));
                        if (get_days.equals(today)){
                            //如果是同一天
                            CommonData.number=cursor.getInt(cursor.getColumnIndex("number"));
                        }
                        else
                        {
                            CommonData.number=1;
                        }
                    }
                }


            }
            catch (Exception ex) {
                //如果创建异常,不能影响其他进度

            }
        }




        /*需要在这里，第一。每次进入时，清空会员信息，清空订单信息,调用接口清空购物车*/
        ClearCarEntityCall=RetrofitHelper.getInstance().ClearCar(CommonData.khid,CommonData.userId);
        ClearCarEntityCall.enqueue(new Callback<ClearCarEntity>() {
            @Override
            public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {
                if (response!=null){
                    ClearCarEntity body = response.body();
                    if (null!=body) {
                        if (body.getReturnX().getNCode() == 0) {
                            //购物车清空成功。，清空单据
                            CommonData.hyMessage = null;
                            CommonData.orderInfo = null;
                        }
                    }
                    else{
                        ToastUtil.showToast(NewIndexActivity.this, "异常通知", "接口请求异常，请耐心等待恢复");
                    }
                }
            }

            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {
                ToastUtil.showToast(NewIndexActivity.this, "异常通知", "请检查网络配置");
            }
        });


        //避免万一断网情况下，数据未正常清空。清空失败 这种情况呢？
        CommonData.hyMessage=null;
        CommonData.orderInfo=null;


        CommonData.player.reset();
        CommonData.player= MediaPlayer.create(this,R.raw.main);
        CommonData.player.start();
        CommonData.player.setLooping(false);



        //绑定 开始购物
        Button button_shape=findViewById(R.id.shopping);
        button_shape.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                //Toast.makeText(IndexActivity.this,"正在跳转，请等待",Toast.LENGTH_SHORT).show();
                //跳转到商品录入界面
                Intent intent = new Intent(NewIndexActivity.this, CarItemsActicity.class);
                startActivity(intent);
            }
        });

        releasePayFace();


        // 创建PopupWindow对象
        LayoutInflater inflater = LayoutInflater.from(this);

        // 引入窗口配置文件

        View view = inflater.inflate(R.layout.activity_settings, null);

        // 创建PopupWindow对象

        final PopupWindow pop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);



        Button btn = (Button) findViewById(R.id.longclick);

        // 需要设置一下此参数，点击外边可消失

        pop.setBackgroundDrawable(new BitmapDrawable());

        //设置点击窗口外边窗口消失

        pop.setOutsideTouchable(true);

        // 设置此参数获得焦点，否则无法点击

        pop.setFocusable(true);

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int[]  location=new int[2];
                btn.getLocationInWindow(location);
                int x=location[0]-40;
                int y=location[1]-250;

                if (pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();
                } else {
                    // 显示窗口
                    pop.showAtLocation(v, Gravity.TOP | Gravity.START, 20, y);
                    //pop.showAsDropDown(v);

                }
                return true;
            }

        });


        TextView text1=  view.findViewById(R.id.txt1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewIndexActivity.this, NewPrintActivity.class);
                startActivity(intent);
            }
        });


        TextView text2=  view.findViewById(R.id.txt2);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(NewIndexActivity.this, R.style.myNewsDialogStyle);

                // 自定义对话框布局
                layout = View.inflate(NewIndexActivity.this, R.layout.acticity_dialog,
                        null);
                dialog.setContentView(layout);
                ComsetDialogSize(layout);

                ListView listView = (ListView) layout.findViewById(R.id.ten_xsprint);

                listView.setDividerHeight(20);
                List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

               //打印最近十比销售单据
                Call<OrderListEntity>  OrderListEntityCall=RetrofitHelper.getInstance().getgetorderidlist(CommonData.userId,CommonData.khid,"0");
                OrderListEntityCall.enqueue(new Callback<OrderListEntity>() {
                    @Override
                    public void onResponse(Call<OrderListEntity> call, Response<OrderListEntity> response) {
                        if (response!=null){
                            OrderListEntity body = response.body();
                            if (null!=body) {
                                if (body.getReturnX().getNCode() == 0) {

                                    List<OrderListEntity.ResponseBean.OrderListBean>  useList=body.getResponse().getOrderList();
                                    if (useList.size()==0){
                                        ToastUtil.showToast(NewIndexActivity.this, "查询结果", "当前门店暂未获取到有效订单");
                                        return;
                                    }

                                    for (int sm = 0; sm < useList.size(); sm++) {


                                            String transId = useList.get(sm).getTransId();
                                            String storeid = useList.get(sm).getStoreId();
                                            //拿到 订单流水号接口，去请求 查询订单详情接口
                                            Call<OrderDetailEntity> OrderDetailEntityCall = RetrofitHelper.getInstance().upgetorderbyid(CommonData.userId, storeid, transId);
                                            OrderDetailEntityCall.enqueue(new Callback<OrderDetailEntity>() {
                                                @Override
                                                public void onResponse(Call<OrderDetailEntity> call, Response<OrderDetailEntity> response) {

                                                    if (response != null) {
                                                        OrderDetailEntity body = response.body();
                                                        if (null != body) {
                                                            if (body.getReturnX().getNCode() != 0) {
                                                                ToastUtil.showToast(NewIndexActivity.this, "查询通知", body.getReturnX().getStrText());
                                                                return;
                                                            }

                                                            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                                                            Date date = new Date();
                                                            String day = form.format(date);

                                                            String storename = body.getResponse().getStoreName();
                                                            String  Str="";


                                                            Str = "                   欢迎光临                   " + "\n";
                                                            Str += "门店名称:" + storename + "\n";
                                                            Str += "流水号：" + body.getResponse().getOutTransId() + "     " + "\n";
                                                            //Str+="商户订单号："+CommonData.outTransId+"     "+"\n";
                                                            Str += "打印日期   " + day + "     " + "\n";
                                                            Str += "===============================================" + "\n";
                                                            Str += "条码      名称      数量       单价     金额" + "\n";

                                                            List<OrderDetailEntity.ResponseBean.PluMapBean> plumap = body.getResponse().getPluMap();

                                                            for (int sk = 0; sk < plumap.size(); sk++) {

                                                                String barcode = plumap.get(sk).getBarcode();
                                                                String sname = plumap.get(sk).getPluName();
                                                                double qty = 0;
                                                                double weight = plumap.get(sk).getNWeight();
                                                                double dj = 0;
                                                                dj = plumap.get(sk).getPluPrice();
                                                                if (weight == 0.00) {
                                                                    //说明重量是 0.  那就取显示数量
                                                                    qty = plumap.get(sk).getPluQty();

                                                                } else {
                                                                    //净重含量存在值 则显示重量
                                                                    qty = weight;
                                                                }

                                                                double zj = plumap.get(sk).getRealAmount();
                                                                Str += barcode + "\n";
                                                                Str += sname + "     " + qty + "     " + dj + "    " + zj + "  " + "\n";
                                                            }

                                                            //付款方式
                                                            String paytype = body.getResponse().getPayMap().getPayTypeName();
                                                            double paynet = body.getResponse().getPayMap().getPayVal();
                                                            int paycount = body.getResponse().getTotQty();
                                                            Str += "===============================================" + "\n";
                                                            Str += "付款方式             金额          总折扣" + "\n";

                                                            Str += paytype + "             " + paynet + "          " + body.getResponse().getDisAmount() + "\n";

                                                            Str += "总数量         应收        找零" + "\n";
                                                            Str += "" + paycount + "             " + paynet+ "          0.00     " + "\n";

                                                            Str += "===============" + body.getResponse().getTrnTime() + "=============" + "\n";
                                                            Str += "            谢谢惠顾，请妥善保管小票         " + "\n";
                                                            Str += "               开正式发票，当月有效              " + "\n";

                                                            Map<String, Object> map = new HashMap<String, Object>();
                                                            map.put("xs_bill", body.getResponse().getOutTransId());
                                                            map.put("xsnet", String.valueOf(paynet));
                                                            map.put("xsdisc", body.getResponse().getDisAmount());
                                                            map.put("xs_date","销售日期:"+body.getResponse().getTrnTime() );
                                                            map.put("printstr",Str);
                                                            listitem.add(map);


                                                            SimpleAdapter adapter = new SimpleAdapter(NewIndexActivity.this, listitem,
                                                                    R.layout.activity_tenprint, new String[]{"xs_bill", "xsnet", "xsdisc", "xs_date","printstr"},
                                                                    new int[]{R.id.xsbill, R.id.tv_yuan_price, R.id.tv_disc, R.id.tv_goods_name,R.id.printstr});
                                                            listView.setAdapter(adapter);
                                                            dialog.show();
                                                            //usePrint(Str);





                                                        }


                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<OrderDetailEntity> call, Throwable t) {

                                                }
                                            });
                                        }

                                }
                                else{
                                    ToastUtil.showToast(NewIndexActivity.this, "查询通知", body.getReturnX().getStrText());
                                }
                            }
                            else{
                                ToastUtil.showToast(NewIndexActivity.this, "异常通知", "接口请求异常，请耐心等待恢复");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<OrderListEntity> call, Throwable t) {

                    }
                });



                //绑定点击事件
//                TextView print=layout.findViewById(R.id.SurePrint);
//                print.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        usePrint(Str);
//                    }
//                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                        String pr_Str = map.get("printstr");

                        usePrint(pr_Str);

                        dialog.dismiss();
                    }
                });


                TextView closemem=layout.findViewById(R.id.closewindow);
                //设置关闭按钮的事件
                closemem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        TextView text3=  view.findViewById(R.id.txt3);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出应用
                exitAPP();
            }
        });
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {
        Context  context=NewIndexActivity.this;
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }


    private void releasePayFace() {
        WxPayFace.getInstance().releaseWxpayface(this);
    }

    /**
     *
     * MembersLogin 会员登录
     * */

    public  void  MembersLogin(View view){

        // 创建一个Dialog
        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);
        // 自定义对话框布局
        layout = View.inflate(this, R.layout.activity_memberlogin,
                null);
        dialog.setContentView(layout);

        //确定
        TextView title = (TextView) layout.findViewById(R.id.closeHy);

        //closemember 关闭页面
        TextView closemem=layout.findViewById(R.id.closemem);
        setDialogSize(layout);
        dialog.show();
        EditText hyedit=(EditText)layout.findViewById(R.id.phoneorhyNum);
        hyedit.setInputType(InputType.TYPE_NULL);

        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText hyedit=(EditText)layout.findViewById(R.id.phoneorhyNum);

                String hynum=hyedit.getText().toString();

                String sCorpId=CommonData.corpId;

                String sUserId="";

                if (!TextUtils.isEmpty(hynum)){

                    Call<GetHyInfoEntity> hyInfoEntityCall= RetrofitHelper.getInstance().getHyInfoEntityCall(hynum,sCorpId,CommonData.lCorpId,sUserId);
                    hyInfoEntityCall.enqueue(new Callback<GetHyInfoEntity>() {
                        @Override
                        public void onResponse(Call<GetHyInfoEntity> call, Response<GetHyInfoEntity> response) {
                            if (null!=response && response.isSuccessful()) {
                                GetHyInfoEntity body = response.body();
                                if ( null!=body) {
                                    if (body.getReturnX().getNCode()==0){
                                        GetHyInfoEntity.ResponseBean response1 = body.getResponse();
                                        HyMessage hyinfo=new  HyMessage();
                                        hyinfo.cardnumber=response1.getCdoData().getSMemberId();
                                        hyinfo.hySname=response1.getCdoData().getStrName();
                                        hyinfo.hytelphone=response1.getCdoData().getSBindMobile();
                                        hyinfo.sMemberId=response1.getCdoData().getSMemberId();
                                        //给会员信息赋值
                                        CommonData.hyMessage=hyinfo;

                                        //然后跳转到 购物界面
                                        Intent intent = new Intent(NewIndexActivity.this,   CarItemsActicity.class);
                                        startActivity(intent);

                                    }else
                                    {
                                        ToastUtil.showToast(NewIndexActivity.this, "登录提示", body.getReturnX().getStrText());
                                        return;
                                    }
                                }
                                else
                                {
                                    ToastUtil.showToast(NewIndexActivity.this, "登录提示", "接口访问失败，请重试");
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<GetHyInfoEntity> call, Throwable t) {
                            ToastUtil.showToast(NewIndexActivity.this, "登录提示", "请求失败，请检查网络连接");
                            return;
                        }
                    });
                }
                dialog.dismiss();

            }
        });
        //设置关闭按钮的时间
        closemem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    /**
     *
     * 数字键盘事件
     *
     * */
    public  void  hynumClick(View view){
        Button bt=(Button)view;
        String text= bt.getText().toString();
        EditText editText1=layout.findViewById(R.id.phoneorhyNum);
        if (!TextUtils.isEmpty(editText1.getText())){
            if (editText1.getText().toString().length()<=15){
                text=editText1.getText().toString()+text;
                editText1.setText(text);
                editText1.setSelection(text.length());
            }
        }else {
            editText1.setText(text);
            editText1.setSelection(text.length());
        }
    }

    /**
     * 动态控制弹出框的大小
     */
    private void setDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                mView.setLayoutParams(new FrameLayout.LayoutParams(780,
                        1000));


            }
        });
    }

    /**
     *
     * 删除按钮事件
     *
     * */
    public  void  hydeleteClick(View view){

        EditText editText=layout.findViewById(R.id.phoneorhyNum);
        if (null!=editText.getText().toString()&&editText.getText().toString().length()>0){
            String old_text=editText.getText().toString();
            editText.setText(old_text.substring(0,old_text.length()-1));
            editText.setSelection(editText.getText().toString().length());
        }
    }


    /**
     * 动态控制弹出框的大小
     */
    private void ComsetDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
               /* int heightNow = v.getHeight();//dialog当前的高度
                int widthNow = v.getWidth();//dialog当前的宽度
                int needWidth = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8);//最小宽度为屏幕的0.7倍
                int needHeight = (int) (getWindowManager().getDefaultDisplay().getHeight() * 1);//最大高度为屏幕的0.6倍
                if (widthNow < needWidth || heightNow > needHeight) {
                    if (widthNow > needWidth) {
                        needWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    if (heightNow < needHeight) {
                        needHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    mView.setLayoutParams(new FrameLayout.LayoutParams(needWidth,
                            needHeight));
                }*/

                mView.setLayoutParams(new FrameLayout.LayoutParams(980,
                        1300));



            }
        });
    }


    public  String   usePrint(String  printstr){
        PrinterAPI mPrinter = PrinterAPI.getInstance();  //打印机部分
        InterfaceAPI io = null;
        io = new SerialAPI(new File(
                "/dev/ttyS1"),
                38400,
                1);
        try {
            if (PrinterAPI.SUCCESS == mPrinter.connect(io)) {

                try {
                    if (PrinterAPI.SUCCESS == mPrinter.printString(printstr, "GBK", true)) {
//                                Thread.sleep(time);
                    } else {
                        Toast.makeText(NewIndexActivity.this, "，小票打印失败,请重试", Toast.LENGTH_SHORT).show();
//                                handler.sendEmptyMessage(1);
                    }

                    mPrinter.printFeed();
                    //清楚打印缓存区 避免对后面数据的影响 有些设置会恢复默认值
                    mPrinter.init();
                    mPrinter.cutPaper(66, 0);

                    return "1";

                } catch (Exception e) {
                    e.printStackTrace();
                    //printstate.setText("小票打印失败,请重试");
                }
            }
            else
            {
                ToastUtil.showToast(NewIndexActivity.this,"打印通知","打印机连接失败，请检查");
                return null;
            }
        }
        catch(Exception ex){
            return null;
        }

        return "1";
    }

}
