package com.ceshi.helloworld;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.Addgoods;
import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.RequestSignBean;
import com.ceshi.helloworld.bean.ResponseSignBean;
import com.ceshi.helloworld.bean.createPrepayIdEntity;
import com.ceshi.helloworld.bean.getCartItemsEntity;
import com.ceshi.helloworld.bean.getWXFacepayAuthInfo;
import com.ceshi.helloworld.bean.upCardCacheEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.CreateAddAdapter;
import com.ceshi.helloworld.net.HyMessage;
import com.ceshi.helloworld.net.OrderInfo;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.SplnfoList;
import com.ceshi.helloworld.net.ToastUtil;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarItemsActicity extends AppCompatActivity implements View.OnClickListener, CreateAddAdapter.RefreshPriceInterface{


    private TextToSpeech textToSpeech = null;//创建自带语音对象



    private TextView phone_view;  //显示会员名称的控件
    private TextView price;
    //private TextView tv_go_to_pay;
    public TextView shopcar_num;
    private ImageView text_tip;
    private TextView yhmoney;
    private  TextView storename;
    private TextView tv_go_to_pay;  //去付款按钮的显示作用
    private LinearLayout listtop;

    //当前添加的产品列表的集合
    private Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();
    //用于界面上UI显示 的列表
    private List<HashMap<String, String>> listmap = new ArrayList<>();
    private CreateAddAdapter adapter; //用于显示列表的容器，传输数据
    private ListView listview;  //用于画列表的

    private Boolean isPay=true;

    private Dialog center_dialog;


    //几个视图
    View layout = null;
    View layout_pay = null;
    View layout_paycode = null;



    //下面这里就是只放 所有使用接口的列表
    private Call<Addgoods> Addgoodsinfo;
    private Call<com.ceshi.helloworld.bean.getCartItemsEntity> getCartItemsEntityCall;
    private Call<PurchaseBag> GetBagsInfo;
    private Call<createPrepayIdEntity> createPrepayIdEntityCall;
    private Call<upCardCacheEntity> upCardCacheEntityCall;
    private  Call<ClearCarEntity>  ClearCarEntityCall;

    //支付方式相关的

    //支付方式
    private String payWay = "WXPaymentCodePay";

    private String sPayTypeExt = "";
    //支付识别码
    private String payAuthCode = "";

    private String mAuthInfo;   //刷脸支付 获取的个人授权信息，从接口获取
    private String openid;      //刷脸支付获取微信openid
    private String out_trade_no;   //获取的交易流水号，用于刷脸支付
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    private String responseAppid;
    private String responnse_subAppid;
    private String responsemachid;
    private String responnse_submachid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        initView();
        if (CommonData.hyMessage == null) {
            phone_view.setText("我是会员");
        } else {
            phone_view.setText("欢迎你，"+CommonData.hyMessage.hySname);
        }


       //推出之后回到首页
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarItemsActicity.this, NewIndexActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initView() {
        //一进来就得执行。初始化会员支付，初始化订单号
        Prepay();
        adapter = new CreateAddAdapter(CarItemsActicity.this, listmap);



        //如果从支付失败界面重新 进入的时候 ，应该需要重新加载购物车
       /* if (null!=CommonData.orderInfo){
            if (null!=CommonData.orderInfo.spList){
                HashMap<String, String> temp_map = new HashMap<>();
                for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()){
                    temp_map.clear();
                    temp_map.put("id", entry.getValue().get(0).getGoodsId());
                    temp_map.put("name", entry.getValue().get(0).getPluName());
                    temp_map.put("price", String.valueOf(entry.getValue().get(0).getRealPrice()));
                    temp_map.put("count", String.valueOf(entry.getValue().get(0).getPackNum()));

                    listmap.add(temp_map);
                }

                MapList=CommonData.orderInfo.spList;
            }
        }*/



        listtop = findViewById(R.id.listtop);
        //top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        text_tip = (ImageView) findViewById(R.id.text_tip);

        price = (TextView) findViewById(R.id.tv_total_price);
        phone_view = (TextView) findViewById(R.id.IamHy);
        shopcar_num = findViewById(R.id.shopcar_num);
        yhmoney=findViewById(R.id.yhmoney);
        storename=findViewById(R.id.storename);
        if (null!=CommonData.machine_name){
            storename.setText(CommonData.machine_name);
        }
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        adapter = new CreateAddAdapter(CarItemsActicity.this, listmap);
        listview.setAdapter(adapter);
        listview.setEmptyView(text_tip);

        adapter.setRefreshPriceInterface(this);
        priceControl(adapter.getPitchOnMap());
        CommonData.list_adaptor = adapter;
    }



    public  void  MembersLogin(View view){


        if (!phone_view.getText().equals("我是会员")){
            return;

        }
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

                                        //给会员信息赋值
                                        CommonData.hyMessage=hyinfo;
                                        phone_view.setText("欢迎你，会员"+hyinfo.hySname);


                                    }else
                                    {
                                        ToastUtil.showToast(CarItemsActicity.this, "登录提示", body.getReturnX().getStrText());
                                        return;
                                    }
                                }
                                else
                                {
                                    ToastUtil.showToast(CarItemsActicity.this, "登录提示", "接口访问失败，请重试");
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<GetHyInfoEntity> call, Throwable t) {
                            ToastUtil.showToast(CarItemsActicity.this, "登录提示", "请求失败，请检查网络连接");
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
     * Created by zhoupan on 2019/11/6.
     * 预支付相关信息
     */

    public void Prepay() {

        //设置界面上显示的订单流水号
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String day=form.format(date);

        TextView txtv=findViewById(R.id.ordernumber);
        CommonData.ordernumber=day+String.format("%03d",CommonData.number);

        txtv.setText("流水号:"+CommonData.ordernumber);

        if (null==CommonData.orderInfo) {

            OrderInfo  orders=new  OrderInfo();
            CommonData.orderInfo=orders;
        }
        else
        {
            //预加载之前的产品信息
            if (null!=CommonData.orderInfo.spList){

                for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()){
                    HashMap<String, String> temp_map = new HashMap<>();
                    temp_map.put("id", entry.getValue().get(0).getBarcode());
                    temp_map.put("name", entry.getValue().get(0).getPluName());
                    temp_map.put("MainPrice", String.valueOf(entry.getValue().get(0).getMainPrice()));
                    temp_map.put("realprice", String.valueOf(entry.getValue().get(0).getRealPrice()));
                    temp_map.put("count", String.valueOf(entry.getValue().get(0).getPackNum()));
                    temp_map.put("actname", String.valueOf(entry.getValue().get(0).getActname()));
                    temp_map.put("disc", String.valueOf(entry.getValue().get(0).getTotaldisc()));
                    listmap.add(temp_map);
                }
                MapList=CommonData.orderInfo.spList;
            }

        }

        CommonData.player.reset();
        CommonData.player=MediaPlayer.create(this,R.raw.lusp);
        CommonData.player.start();
        CommonData.player.setLooping(false);



        //预支付 相关的操作，给公共订单类赋值,每一次都重新生成单号，单号暂时设置为不能重复

        //调用接口拿到订单号
        createPrepayIdEntityCall = RetrofitHelper.getInstance().createPrepayId(CommonData.khid);
        createPrepayIdEntityCall.enqueue(new Callback<createPrepayIdEntity>() {
            @Override
            public void onResponse(Call<createPrepayIdEntity> call, Response<createPrepayIdEntity> response) {
                if (response != null) {
                    createPrepayIdEntity body = response.body();
                    if(null!=body) {
                        if (body.getReturnX().getNCode() == 0) {

                            createPrepayIdEntity.ResponseBean response1 = body.getResponse();

                            //拿到预支付流水号
                            if (null != CommonData.orderInfo) {
                                //重新生成 订单号
                                CommonData.orderInfo.prepayId = response1.getPrepayId();
                            }

                            //会员预支付必须要等到 拿到上面的 订单流水号才可以支付，预支付，不成功也无所谓的
                            if (null != CommonData.hyMessage) {
                                //进行会员卡预支付设置
                                upCardCacheEntityCall = RetrofitHelper.getInstance().upCardCache(CommonData.khid, CommonData.orderInfo.prepayId);
                                upCardCacheEntityCall.enqueue(new Callback<upCardCacheEntity>() {
                                    @Override
                                    public void onResponse(Call<upCardCacheEntity> call, Response<upCardCacheEntity> response) {
                                        if (response != null) {
                                            upCardCacheEntity body = response.body();

                                            /*if (body.getReturnX().getNCode() == 0) {
                                                //暂时 先不管 会员卡预支付失败的结果

                                            }*/
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<upCardCacheEntity> call, Throwable t) {
                                    }
                                });
                            }
                        } else {
                            ToastUtil.showToast(CarItemsActicity.this, "商品录入通知", body.getReturnX().getStrInfo());

                        }
                    }
                    else
                    {
                        ToastUtil.showToast(CarItemsActicity.this, "接口通知", "接口访问异常");
                        return;

                    }
                }
            }

            @Override
            public void onFailure(Call<createPrepayIdEntity> call, Throwable t) {

            }
        });
    }




    /**
     * Created by zhoupan on 2019/11/7.
     * <p>
     * 扫码添加商品，封装方法，用于单个添加商品，和添加购物袋，仅用于添加商品，
     * 具体流程为 1：使用添加商品接口，
     *          2：使用购物车列表接口，获取到当前产品的实际售价，折扣价，促销内容等
     */

    public void AddnewSpid(String inputbarcode) {

        if (CommonData.orderInfo != null) {
            if (CommonData.orderInfo.spList != null) {
                MapList = CommonData.orderInfo.spList;
            }
        }
        HashMap<String, String> map = new HashMap<>();
        Addgoodsinfo = RetrofitHelper.getInstance().getgoodsinfo(inputbarcode, CommonData.khid, CommonData.userId, "0");
        Addgoodsinfo.enqueue(new Callback<Addgoods>() {
            @Override
            public void onResponse(Call<Addgoods> call, Response<Addgoods> response) {

                if (response.body() != null) {
                    if (response.body().getReturnX().getNCode() == 0) {

                        //获取 购物车列表。然后解析购物车列表
                        getCartItemsEntityCall = RetrofitHelper.getInstance().getCartItems(CommonData.userId, CommonData.khid);
                        getCartItemsEntityCall.enqueue(new Callback<getCartItemsEntity>() {
                            @Override
                            public void onResponse(Call<getCartItemsEntity> call, Response<getCartItemsEntity> response) {
                                if (response != null) {
                                    getCartItemsEntity body = response.body();
                                    if(null==body){
                                        ToastUtil.showToast(CarItemsActicity.this, "商品查询通知", "接口访问异常，请重试");
                                        return;
                                    }
                                    if (body.getReturnX().getNCode() == 0) {
                                        //下面先  获取到 总价和总金额折扣这些
                                        CommonData.orderInfo.totalCount = body.getResponse().getTotalQty();
                                        CommonData.orderInfo.totalPrice = body.getResponse().getShouldAmount();
                                        CommonData.orderInfo.totalDisc = body.getResponse().getDisAmount();

                                        List<getCartItemsEntity.ResponseBean.ItemsListBean> itemsList = body.getResponse().getItemsList();
                                        for (int sm = 0; sm < itemsList.size(); sm++) {
                                            List<getCartItemsEntity.ResponseBean.ItemsListBean.ItemsBean> sub_itemsList = itemsList.get(sm).getItems();
                                            for (int sk = 0; sk < sub_itemsList.size(); sk++) {
                                                //拿到产品编码
                                                String barcode = sub_itemsList.get(sk).getSBarcode();
                                                double nRealPrice = sub_itemsList.get(sk).getNPluPrice();
                                               /* //有会员登录，会员价低取会员价
                                                //无会员登录，则获取 nPluPrice
                                                if (CommonData.hyMessage != null) {
                                                    //说明有会员
                                                    nRealPrice = sub_itemsList.get(sk).getNRealPrice();
                                                } else {
                                                    nRealPrice = sub_itemsList.get(sk).getNPluPrice();
                                                }*/
                                                if (MapList.containsKey(barcode)) {

                                                    //如果存在，拿到集合，增加数量，总价，折扣
                                                    MapList.get(barcode).get(0).setPackNum(sub_itemsList.get(sk).getNQty());
                                                    MapList.get(barcode).get(0).setMainPrice(sub_itemsList.get(sk).getNRealPrice());  //原价
                                                    MapList.get(barcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));  //实际总售价

                                                    //修改列表的数量
                                                    for (int k = 0; k < listmap.size(); k++) {
                                                        if (listmap.get(k).get("id").equals(barcode)) {
                                                            listmap.get(k).put("count", String.valueOf(sub_itemsList.get(sk).getNQty()));
                                                            listmap.get(k).put("MainPrice", String.valueOf(nRealPrice));
                                                            listmap.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));
                                                            listmap.get(k).put("actname", itemsList.get(sm).getDisRule());
                                                        }
                                                    }
                                                }
                                                else {
                                                    List<SplnfoList> uselist = new ArrayList<SplnfoList>();
                                                    SplnfoList usesplnfo = new SplnfoList();
                                                    usesplnfo.setBarcode(sub_itemsList.get(sk).getSBarcode());
                                                    usesplnfo.setGoodsId(sub_itemsList.get(sk).getSGoodsId());
                                                    usesplnfo.setMainPrice(nRealPrice); //每一个产品的原价，无需计算
                                                    usesplnfo.setPackNum(sub_itemsList.get(sk).getNQty());
                                                    usesplnfo.setPluName(sub_itemsList.get(sk).getSGoodsName());
                                                    usesplnfo.setTotaldisc(sub_itemsList.get(sk).getPluDis());// 总折扣
                                                    usesplnfo.setRealPrice(sub_itemsList.get(sk).getPluRealAmount());  //总实际售价
                                                    usesplnfo.setActname(itemsList.get(sm).getDisRule());
                                                    usesplnfo.setNweight(sub_itemsList.get(sk).getNWeight());  //产品的重量


                                                    uselist.add(usesplnfo);
                                                    //说明产品不存在，直接增加进去
                                                    MapList.put(barcode, uselist);


                                                    //下面是只取几个字段去改变 界面显示的
                                                    map.put("barcode", sub_itemsList.get(sk).getSBarcode());
                                                    map.put("id", barcode);
                                                    map.put("name", sub_itemsList.get(sk).getSGoodsName());
                                                    map.put("MainPrice", String.valueOf(nRealPrice));  //原价
                                                    map.put("disc", String.valueOf(sub_itemsList.get(sk).getPluDis()));  //折扣
                                                    map.put("realprice", sub_itemsList.get(sk).getPluRealAmount());
                                                    map.put("count", String.valueOf(sub_itemsList.get(sk).getNQty()));
                                                    map.put("actname", itemsList.get(sm).getDisRule());
                                                    listmap.add(map);


                                                }
                                            }
                                        }

                                        CommonData.orderInfo.spList = MapList;

                                        //界面上实现  增加一个元素
                                        adapter = new CreateAddAdapter(CarItemsActicity.this, listmap);
                                        listview.setAdapter(adapter);
                                        listview.setSelection(listview.getBottom());  //加这一句话的目的是，超屏幕现实的时候，自动定位在底部
                                        listview.setSelection(adapter.getCount()-1);
                                        adapter.setRefreshPriceInterface(CarItemsActicity.this);
                                        priceControl(adapter.getPitchOnMap());
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<getCartItemsEntity> call, Throwable t) {

                                ToastUtil.showToast(CarItemsActicity.this, "商品查询通知", t.getMessage());
                                return;
                            }
                        });
                    } else {
                        String result = response.body().getReturnX().getStrText();
                        ToastUtil.showToast(CarItemsActicity.this, "商品查询通知", result);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<Addgoods> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
    }


    /**
     * 手动输入条码  和 取消
     * input_tiaoma
     **/
    public void input_tiaoma(View view) {
        // 创建一个Dialog
        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);

        // 自定义对话框布局
        layout = View.inflate(this, R.layout.dialog,
                null);

        dialog.setContentView(layout);
        //确定
        TextView title = (TextView) layout.findViewById(R.id.close1);
        //取消
        TextView title2 = (TextView) layout.findViewById(R.id.close2);
        setDialogSize(layout);
        dialog.show();
        EditText editText1 = layout.findViewById(R.id.username);
        editText1 .setInputType(InputType.TYPE_NULL);
        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = layout.findViewById(R.id.username);
                String inputbarcode = editText1.getText().toString();

                AddnewSpid(inputbarcode);
                dialog.dismiss();

            }
        });
        // 设置取消按钮的事件
        title2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    /**
     * 动态控制弹出框的大小
     */
    private void setDialogSize(final View mView) {
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

                mView.setLayoutParams(new FrameLayout.LayoutParams(780,
                        1400));



            }
        });
    }


    /**
     * 选择购物袋
     * input_tiaoma
     **/
    public void input_bags(View view) {


        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);

        // 自定义对话框布局
        layout = View.inflate(this, R.layout.bagingo_one,
                null);
        dialog.setContentView(layout);

        ListView listView = (ListView) layout.findViewById(R.id.lv_baginfo);

        listView.setDividerHeight(20);
        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

        TextView closegwd=layout.findViewById(R.id.closegwd);

        closegwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        GetBagsInfo = RetrofitHelper.getInstance().getBagInfo(CommonData.userId, CommonData.khid);
        GetBagsInfo.enqueue(new Callback<PurchaseBag>() {
            @Override
            public void onResponse(Call<PurchaseBag> call, Response<PurchaseBag> response) {

                if (response.body() != null) {
                    PurchaseBag getBagsInfo = response.body();

                    int onCode = getBagsInfo.getReturnX().getNCode();
                    if (onCode == 0) {
                        PurchaseBag.ResponseBean responseBean = getBagsInfo.getResponse();
                        List<PurchaseBag.ResponseBean.BagMapBean> maps = responseBean.getBagMap();
                        int n = 20;
                        int[] img_expense = new int[n];
                        String[] tv_bagname = new String[n];
                        String[] tv_bagpic = new String[n];
                        String[] tv_goodsid = new String[n];
                        for (int m = 0; m < maps.size(); m++) {

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("img_expense", R.mipmap.gwd);
                            map.put("tv_bagname", maps.get(m).getPluName());
                            map.put("tv_bagpic", String.valueOf(maps.get(m).getPluPrice()));
                            map.put("tv_goodsid", maps.get(m).getGoodsId());
                            listitem.add(map);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(CarItemsActicity.this, listitem, R.layout.baginfo, new String[]{"tv_bagname", "tv_bagpic", "img_expense", "tv_goodsid"}, new int[]{R.id.tv_bagname, R.id.tv_bagpic, R.id.img_expense, R.id.tv_goodsid});
                        listView.setAdapter(adapter);
                        dialog.show();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                                String title = map.get("tv_bagname");
                                String bagpic = map.get("tv_bagpic");
                                String bagsid = map.get("tv_goodsid");

                                AddnewSpid(bagsid);
                                dialog.dismiss();
                            }
                        });
                    } else {

                    }

                }


            }

            @Override
            public void onFailure(Call<PurchaseBag> call, Throwable t) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    /**
     * 数字键盘事件
     */
    public void numClick(View view) {
        Button bt = (Button) view;
        String text = bt.getText().toString();
        EditText editText1 = layout.findViewById(R.id.username);
        if (!TextUtils.isEmpty(editText1.getText())) {
            if (editText1.getText().toString().length() <= 23) {
                text = editText1.getText().toString() + text;
                editText1.setText(text);
                editText1.setSelection(text.length());
            }
        } else {
            editText1.setText(text);
            editText1.setSelection(text.length());
        }
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
     *键盘的清空数字按钮
     */
    public void   hydeleteClick(View view) {

        EditText editText = layout.findViewById(R.id.phoneorhyNum);
        if (null != editText.getText().toString() && editText.getText().toString().length() > 0) {
            String old_text = editText.getText().toString();
            editText.setText(old_text.substring(0, old_text.length() - 1));
            editText.setSelection(editText.getText().toString().length());
        }
    }


    /**
     * 点击右上角按钮，返回首页
     */
    public void return_home(View view) {
        Intent intent = new Intent(CarItemsActicity.this, NewIndexActivity.class);
        startActivity(intent);
    }

    /**
     * deletecar
     * 清空购物车
     */

    public void deletecar(View view) {
        //清空listmap的值
        listmap.clear();
        MapList.clear();

        //还需要调接口，清空购物车
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
                            CommonData.orderInfo.spList.clear();
                        }
                    }
                    else{
                        ToastUtil.showToast(CarItemsActicity.this, "异常通知", "接口请求异常，请耐心等待回复");
                    }
                }
            }
            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {
                ToastUtil.showToast(CarItemsActicity.this, "异常通知", "请检查网络配置");
            }
        });


        CommonData.orderInfo.totalPrice="0";
        CommonData.orderInfo.totalCount=0;
        CommonData.orderInfo.totalDisc="0";
        CommonData.orderInfo.spList.clear();


        //清空列表页面
        adapter = new CreateAddAdapter(CarItemsActicity.this, listmap);
        listview.setAdapter(adapter);
        adapter.setRefreshPriceInterface(CarItemsActicity.this);

        priceControl(adapter.getPitchOnMap());
        shopcar_num.setText("0");
        //加载出空购物车页面
        listview.setEmptyView(text_tip);

    }


    /**
     *键盘的清空数字按钮
     */
    public void deleteClick(View view) {

        EditText editText = layout.findViewById(R.id.username);
        if (null != editText.getText().toString() && editText.getText().toString().length() > 0) {
            String old_text = editText.getText().toString();
            editText.setText(old_text.substring(0, old_text.length() - 1));
            editText.setSelection(editText.getText().toString().length());
        }
    }
    /**
     * 控制价格展示总价
     */
    public void priceControl(Map<String, Integer> pitchOnMap) {
//        totalCount = 0;
//        totalPrice = 0.00;
//        for (int i = 0; i < listmap.size(); i++) {
//            totalCount = totalCount + Integer.valueOf(listmap.get(i).get("count"));
//            double goodsPrice = Integer.valueOf(listmap.get(i).get("count")) * Double.valueOf(listmap.get(i).get("price"));
//            //double goodsPrice=Double.valueOf(listmap.get(i).get("price"));
//            totalPrice = totalPrice + goodsPrice;
//        }
        //price.setText(" ¥ " + new BigDecimal(String.valueOf(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        if (CommonData.orderInfo == null) {
            yhmoney.setText(" ￥ " + 0.00);
            shopcar_num.setText("0");
            price.setText(" ￥ 0.00");//应付金额

        } else {
            yhmoney.setText(" ￥ " + CommonData.orderInfo.totalDisc);
            shopcar_num.setText(String.valueOf(CommonData.orderInfo.totalCount));

            price.setText(" ￥ " +CommonData.orderInfo.totalPrice);//应付金额
        }



        if (CommonData.orderInfo.totalCount==0) {
            //listtop.setVisibility(View.GONE); //zhoupan  去掉的，默认一进来就应该显示
            tv_go_to_pay.setText("去付款");
            tv_go_to_pay.setBackgroundResource(R.drawable.button_shape_black); // 使用Color类转换
            tv_go_to_pay.setEnabled(false);
        } else {
            try {
                tv_go_to_pay.setText("去付款(" + CommonData.orderInfo.totalPrice + ")");
            }
            catch(Exception ex){

            }
            //listtop.setVisibility(View.VISIBLE);
            tv_go_to_pay.setEnabled(true);
            tv_go_to_pay.setBackgroundResource(R.drawable.button_shape);

        }
    }


    //扫码枪的回车事件
    String barcode = "";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", barcode);
            if (!TextUtils.isEmpty(barcode)) {
                if (barcode.contains("\n")) {
                    barcode = barcode.substring(0, barcode.length() - 1);
                }
                AddnewSpid(barcode);
                barcode = "";
            }

        }
        return true;

    }


    /**
     * to_pay
     * 去支付，首先选择去支付的支付方式
     */

    public void to_pay(View view) {
        //首先要判断是否增加了产品
        if (CommonData.orderInfo == null || CommonData.orderInfo.spList.size()==0) {
            ToastUtil.showToast(CarItemsActicity.this, "支付通知", "请先录入要支付的商品");
            return;
        }

        //Intent intent = new Intent(CarItemsActicity.this, payWayActivity.class);
        Intent intent = new Intent(CarItemsActicity.this, FinishActivity.class);

        startActivity(intent);

    }

    /**
     * 弹出输入支付码的页面
     */

   /* public void dialog_paycode(View view,String payway) {

        final Dialog dialog_paycode = new Dialog(this, R.style.myNewsDialogStyle);

        center_dialog = dialog_paycode;


        // 自定义对话框布局
        layout_paycode = View.inflate(this, R.layout.input_paycode, null);
        dialog_paycode.setContentView(layout_paycode);
        Window window = dialog_paycode.getWindow();
        window.setGravity(Gravity.CENTER);
        // window.setDimAmount(0f);
        dialog_paycode.show();
        Button require_code = (Button) layout_paycode.findViewById(R.id.require_code);

        //获取到支付码的控件
        EditText input_code = layout_paycode.findViewById(R.id.pay_code);


       *//* textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    textToSpeech.speak("请打开"+payway+"付款码进行付款",//输入中文，若不支持的设备则不会读出来
                            TextToSpeech.QUEUE_FLUSH, null);

                } else {
                    Toast.makeText(CarItemsActicity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });*//*
        CommonData.player.reset();
        if (payway.equals("支付宝")){
            CommonData.player=MediaPlayer.create(this,R.raw.alipay);
        }
        else{
            CommonData.player=MediaPlayer.create(this,R.raw.wxpay);
        }
        CommonData.player.start();
        CommonData.player.setLooping(false);


        input_code.setOnKeyListener(new View.OnKeyListener() {

            String input_paucode = input_code.getText().toString();

            @Override
            public boolean onKey(View view, int keycode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    char pressedKey = (char) event.getUnicodeChar();
                    input_paucode = input_code.getText().toString();

                    input_paucode += pressedKey;
                    input_code.setText(input_paucode);
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    input_paucode = input_paucode.replace("\n", "");
                    payAuthCode = input_paucode;
                    Moneypay();
                }

                input_code.setSelection(input_code.getText().toString().length());
                return true;
            }

        });


        require_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String authcode = input_code.getText().toString();

                    if (authcode.length() < 18) {
                        ToastUtil.showToast(CarItemsActicity.this, "支付通知", "请输入完整的18位支付码");
                        return;
                    } else {
                        payAuthCode = authcode;
                        //去支付
                        Moneypay();
                    }
                } catch (Exception ex) {

                }

            }
        });

    }*/





}
