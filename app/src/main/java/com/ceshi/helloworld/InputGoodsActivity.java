package com.ceshi.helloworld;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.Addgoods;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.RequestSignBean;
import com.ceshi.helloworld.bean.ResponseSignBean;
import com.ceshi.helloworld.bean.createPrepayIdEntity;
import com.ceshi.helloworld.bean.getCartItemsEntity;
import com.ceshi.helloworld.bean.getWXFacepayAuthInfo;
import com.ceshi.helloworld.bean.upCardCacheEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.CreateAddAdapter;
import com.ceshi.helloworld.net.OrderInfo;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.SplnfoList;
import com.ceshi.helloworld.net.ToastUtil;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InputGoodsActivity extends AppCompatActivity implements View.OnClickListener, CreateAddAdapter.RefreshPriceInterface {
    private LinearLayout top_bar;
    private ListView listview;
    private TextView price;
    private TextView tv_go_to_pay;
    public TextView shopcar_num;
    private ImageView text_tip;
    private TextView yhmoney;
    private TextView phone_view;
    private  TextView storename;

    View layout = null;
    View layout_pay = null;
    View layout_paycode = null;
    private List<HashMap<String, String>> listmap = new ArrayList<>();
    private CreateAddAdapter adapter;
    HashMap<String, String> map = new HashMap<String, String>();
    private double totalPrice = 0.00;
    private int totalCount = 0;
    private Call<Addgoods> Addgoodsinfo;

    private Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();

    private Call<getWXFacepayAuthInfo> getWXFacepayAuthInfoCall;


    private LinearLayout listtop;
    private Call<createPrepayIdEntity> createPrepayIdEntityCall;

    private Call<upCardCacheEntity> upCardCacheEntityCall;

    private Call<getCartItemsEntity> getCartItemsEntityCall;

    private OrderInfo neworderInfo = new OrderInfo();


    private Dialog center_dialog;

    private Call<PurchaseBag> GetBagsInfo;


    private String responseAppid;


    private String responnse_subAppid;

    private String responsemachid;


    private String responnse_submachid;

    //支付方式
    private String payWay = "WXPaymentCodePay";

    private String sPayTypeExt = "";

    //支付识别码
    private String payAuthCode = "";

    private Call<ResponseSignBean> ResponseSignBeanCall;

    private String mAuthInfo;   //刷脸支付 获取的个人授权信息，从接口获取
    private String openid;      //刷脸支付获取微信openid
    private String out_trade_no;   //获取的交易流水号，用于刷脸支付

    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        initView();
        if (CommonData.hyMessage == null) {
            phone_view.setText("");
        } else {
            phone_view.setText(CommonData.hyMessage.hytelphone);
        }

        //一进来就得执行。初始化会员支付，初始化订单号
        Prepay();

    }

    private void initView() {
        listtop = findViewById(R.id.listtop);
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        text_tip = (ImageView) findViewById(R.id.text_tip);
        price = (TextView) findViewById(R.id.tv_total_price);
        phone_view = (TextView) findViewById(R.id.phone);
        shopcar_num = findViewById(R.id.shopcar_num);
        yhmoney=findViewById(R.id.yhmoney);
        storename=findViewById(R.id.storename);
        if (null!=CommonData.machine_name){
            storename.setText(CommonData.machine_name);
        }
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
        listview.setAdapter(adapter);
        listview.setEmptyView(text_tip);
        adapter.setRefreshPriceInterface(this);
        priceControl(adapter.getPitchOnMap());
        CommonData.list_adaptor = adapter;
    }


    /**
     * Created by zhoupan on 2019/11/6.
     * 预支付相关信息
     */

    public void Prepay() {
        //预支付 相关的操作，给公共订单类赋值

        if (CommonData.orderInfo == null) {
            //调用接口拿到订单号
            createPrepayIdEntityCall = RetrofitHelper.getInstance().createPrepayId(CommonData.khid);

            createPrepayIdEntityCall.enqueue(new Callback<createPrepayIdEntity>() {
                @Override
                public void onResponse(Call<createPrepayIdEntity> call, Response<createPrepayIdEntity> response) {
                    if (response != null) {
                        createPrepayIdEntity body = response.body();

                        if (body.getReturnX().getNCode() == 0) {

                            createPrepayIdEntity.ResponseBean response1 = body.getResponse();

                            //拿到预支付流水号
                            neworderInfo.prepayId = response1.getPrepayId();
                            CommonData.orderInfo = neworderInfo;


                            //会员预支付必须要等到 拿到上面的 订单流水号才可以支付
                            if (CommonData.hyMessage != null) {
                                //进行会员卡预支付设置
                                upCardCacheEntityCall = RetrofitHelper.getInstance().upCardCache(CommonData.khid, neworderInfo.prepayId);
                                upCardCacheEntityCall.enqueue(new Callback<upCardCacheEntity>() {
                                    @Override
                                    public void onResponse(Call<upCardCacheEntity> call, Response<upCardCacheEntity> response) {
                                        if (response != null) {
                                            upCardCacheEntity body = response.body();

                                            if (body.getReturnX().getNCode() == 0) {

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<upCardCacheEntity> call, Throwable t) {
                                    }
                                });
                            }


                        } else {
                            ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", body.getReturnX().getStrInfo());

                        }
                    }
                }

                @Override
                public void onFailure(Call<createPrepayIdEntity> call, Throwable t) {

                }
            });

        }


    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
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
     * Created by zhoupan on 2019/11/7.
     * <p>
     * 扫码添加商品，封装方法，用于单个添加商品，和添加购物袋
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
                    map.clear();
                    Addgoods addgoods = response.body();
                    int onCode = addgoods.getReturnX().getNCode();
                    if (onCode == 0) {
                        Addgoods.ResponseBean responseBean = addgoods.getResponse();
                        List<Addgoods.ResponseBean.GoodsListBean> maps = responseBean.getGoodsList();

                        //拿到产品编码
                        String spcode = maps.get(0).getGoodsId();
                        double useprice = maps.get(0).getRealPrice();
                        double disc = 0;
                        //mainprice原价,extprice特价，vipprice会员价
                        //有会员登录，会员价低取会员价
                        //无会员登录，特价有就取特价，没有就是原价
                        if (CommonData.hyMessage != null) {
                            //说明有会员
                            useprice = maps.get(0).getVipPrice();
                        } else {
                            useprice = maps.get(0).getExtPrice();
                            if (useprice <= 0) {
                                //如果没有特价时，则取原价
                                useprice = maps.get(0).getMainPrice();
                            }
                        }
                        //每录入一次，都去增加总数量和总价
                        neworderInfo.totalCount = neworderInfo.totalCount + 1;  //增加总数量
                        neworderInfo.totalPrice = neworderInfo.totalPrice + useprice;  //增加总价
                        neworderInfo.totalDisc = neworderInfo.totalDisc + disc;    //增加总折扣

                        //1.0先判断 改产品集合中是否存在
                        if (MapList.containsKey(spcode)) {

                            //如果存在，拿到集合，增加数量，总价，折扣
                            MapList.get(spcode).get(0).setPackNum(MapList.get(spcode).get(0).getPackNum() + 1);
                            MapList.get(spcode).get(0).setMainPrice(MapList.get(spcode).get(0).getMainPrice() + useprice);

                            //更改 映射的 map的内容
                            for (int k = 0; k < listmap.size(); k++) {
                                if (listmap.get(k).get("id").equals(spcode)) {
                                    listmap.get(k).put("count", String.valueOf(Integer.valueOf(listmap.get(k).get("count")) + 1));
                                    //listmap.get(k).put("price", String.valueOf( Double.valueOf(listmap.get(k).get("price"))+ useprice));
                                    listmap.get(k).put("price", String.valueOf(Double.valueOf(listmap.get(k).get("price"))));
                                }
                            }
                        } else {
                            List<SplnfoList> uselist = new ArrayList<SplnfoList>();
                            SplnfoList usesplnfo = new SplnfoList();
                            usesplnfo.setBarcode(maps.get(0).getBarcode());
                            usesplnfo.setGoodsId(maps.get(0).getGoodsId());
                            usesplnfo.setMainPrice(useprice);
                            usesplnfo.setPackNum(maps.get(0).getPackNum());
                            usesplnfo.setPluName(maps.get(0).getPluName());
                            usesplnfo.setPluTypeId(maps.get(0).getPluTypeId());
                            usesplnfo.setPluUnit(maps.get(0).getPluUnit());
                            usesplnfo.setpluPrice(maps.get(0).getPluPrice());
                            usesplnfo.setRealPrice(useprice);
                            uselist.add(usesplnfo);
                            //说明产品不存在，直接增加进去
                            MapList.put(spcode, uselist);
                            //下面是只取几个字段去改变 界面显示的
                            map.put("id", maps.get(0).getGoodsId());
                            map.put("name", maps.get(0).getPluName());
                            map.put("price", String.valueOf(useprice));
                            map.put("count", String.valueOf(maps.get(0).getPackNum()));

                            CommonData.orderInfo = neworderInfo;
                            CommonData.orderInfo.spList = MapList;


                            listmap.add(map);
                        }
                        //界面上实现  增加一个元素
                        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
                        listview.setAdapter(adapter);
                        adapter.setRefreshPriceInterface(InputGoodsActivity.this);
                        priceControl(adapter.getPitchOnMap());
                    } else {
                        String result = addgoods.getReturnX().getStrText();
                        ToastUtil.showToast(InputGoodsActivity.this, "商品查询通知", result);
                    }
                }
            }

            @Override
            public void onFailure(Call<Addgoods> call, Throwable t) {

            }
        });
    }

    /**
     * 控制价格展示总价
     */
    public void priceControl(Map<String, Integer> pitchOnMap) {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < listmap.size(); i++) {
            totalCount = totalCount + Integer.valueOf(listmap.get(i).get("count"));
            double goodsPrice = Integer.valueOf(listmap.get(i).get("count")) * Double.valueOf(listmap.get(i).get("price"));
            //double goodsPrice=Double.valueOf(listmap.get(i).get("price"));
            totalPrice = totalPrice + goodsPrice;
        }
        price.setText(" ¥ " + new BigDecimal(String.valueOf(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        if (CommonData.orderInfo == null) {
            yhmoney.setText(" ￥ " + 0.00);
        } else {
            yhmoney.setText(" ￥ " + CommonData.orderInfo.totalDisc);
        }
        if (CommonData.orderInfo != null) {

            shopcar_num.setText(String.valueOf(CommonData.orderInfo.totalCount));
        } else {

            shopcar_num.setText("0");
        }

        tv_go_to_pay.setText("去付款(" + new BigDecimal(String.valueOf(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
        if (totalPrice == 0) {
            listtop.setVisibility(View.GONE);
            tv_go_to_pay.setText("去付款");
            tv_go_to_pay.setBackgroundResource(R.drawable.button_shape_black); // 使用Color类转换
            tv_go_to_pay.setEnabled(false);
        } else {
            listtop.setVisibility(View.VISIBLE);
            tv_go_to_pay.setEnabled(true);
            tv_go_to_pay.setBackgroundResource(R.drawable.button_shape);

        }
    }

    /**
     * 删除 控制价格展示总价
     *
     * @parammap
     */
    public void checkDelete() {
        List<HashMap<String, String>> waitDeleteList = new ArrayList<>();
        Map<String, Integer> waitDeleteMap = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        map = adapter.getPitchOnMap();
        for (int i = 0; i < listmap.size(); i++) {
            if (map.get(listmap.get(i).get("id")) == 1) {
                waitDeleteList.add(listmap.get(i));
                waitDeleteMap.put(listmap.get(i).get("id"), map.get(listmap.get(i).get("id")));
            }
        }
        listmap.removeAll(waitDeleteList);
        map.remove(waitDeleteMap);

        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);

        listview.setAdapter(adapter);
        adapter.setRefreshPriceInterface(InputGoodsActivity.this);
        priceControl(adapter.getPitchOnMap());
        CommonData.list_adaptor = adapter;
       /* priceControl(map);
        adapter.notifyDataSetChanged();*/
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
                int heightNow = v.getHeight();//dialog当前的高度
                int widthNow = v.getWidth();//dialog当前的宽度
                int needWidth = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.55);//最小宽度为屏幕的0.7倍
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
                }
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
        /*listView.setVisibility(View.VISIBLE);
        listView.bringToFront();*/
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

                        SimpleAdapter adapter = new SimpleAdapter(InputGoodsActivity.this, listitem, R.layout.baginfo, new String[]{"tv_bagname", "tv_bagpic", "img_expense", "tv_goodsid"}, new int[]{R.id.tv_bagname, R.id.tv_bagpic, R.id.img_expense, R.id.tv_goodsid});
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
            if (editText1.getText().toString().length() <= 15) {
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
     * 删除按钮事件
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
     * 返回首页
     */
    public void return_home(View view) {
        Intent intent = new Intent(InputGoodsActivity.this, IndexActivity.class);
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
        //清空给基础信息
        map.clear();
        MapList.clear();
        neworderInfo = new OrderInfo();
        CommonData.orderInfo = neworderInfo;
        //清空列表页面
        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
        listview.setAdapter(adapter);
        adapter.setRefreshPriceInterface(InputGoodsActivity.this);

        priceControl(adapter.getPitchOnMap());
        shopcar_num.setText("0");
        //加载出空购物车页面
        listview.setEmptyView(text_tip);

    }


    /**
     * to_pay
     * 去支付，首先选择去支付的支付方式
     */

    public void to_pay(View view) {

        //首先要判断是否增加了产品
        if (CommonData.orderInfo == null || MapList.size() == 0) {
            ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "请先录入要支付的商品");
            return;
        }


        final Dialog dialog1 = new Dialog(this, R.style.myNewsDialogStyle);
        // 自定义对话框布局
        layout_pay = View.inflate(this, R.layout.activity_chosepay, null);
        dialog1.setContentView(layout_pay);
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0f);
        dialog1.show();
        View zfb = (View) layout_pay.findViewById(R.id.zfb);
        View wx = (View) layout_pay.findViewById(R.id.wx);
        View wx_face = (View) layout_pay.findViewById(R.id.wx_face);

        zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                payWay = "AliPaymentCodePay";
                sPayTypeExt="devicealimicropay";
                dialog_paycode(view);


            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                payWay = "WXPaymentCodePay";
                sPayTypeExt="devicewxmicropay";
                dialog_paycode(view);
            }
        });
        wx_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPayTypeExt="devicewxface";
                payWay = "WXFacePay";

                wxFacepay();
            }
        });
    }

    /**
     * 弹出输入支付码的页面
     */

    public void dialog_paycode(View view) {

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
                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "请输入完整的18位支付码");
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

    }


    /**
     * Moneypay
     * Created by zhoupan on 2019/11/8.
     * 选择支付方式之后进行支付
     */

    public void Moneypay() {

        //获取 购物车 列表

        /*getCartItemsEntityCall=RetrofitHelper.getInstance().getCartItems(CommonData.userId,CommonData.khid);
        getCartItemsEntityCall.enqueue(new Callback<getCartItemsEntity>() {
            @Override
            public void onResponse(Call<getCartItemsEntity> call, Response<getCartItemsEntity> response) {
                if (response != null) {
                    getCartItemsEntity body = response.body();

                    if (body.getReturnX().getNCode() == 0) {

                    }
                }
            }

            @Override
            public void onFailure(Call<getCartItemsEntity> call, Throwable t) {

            }
        });*/

        //1.0 初始化所有的产品信息,
        List<RequestSignBean.PluMapBean> pluMap = new ArrayList<RequestSignBean.PluMapBean>();

        try {

            for (Map.Entry<String, List<SplnfoList>> entry : MapList.entrySet()) {

                RequestSignBean.PluMapBean payMapcls = new RequestSignBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getMainPrice()));
                payMapcls.setPluPrice(Double.valueOf(entry.getValue().get(0).getpluPrice()));
                payMapcls.setPluAmount(Double.valueOf(entry.getValue().get(0).getMainPrice()));
                pluMap.add(payMapcls);
            }
        } catch (Exception ex) {

        }

        //2.0 选取支付方式 ,初始化支付信息
        int PayTypeId = 1;
//        if (payWay.equals("WXPaymentCodePay")){
//            PayTypeId=5;
//        }else if(payWay.equals("AliPaymentCodePay")){
//            PayTypeId=7;
//        }

        List<RequestSignBean.PayMapBean> payMap = new ArrayList<RequestSignBean.PayMapBean>();
        RequestSignBean.PayMapBean pmp = new RequestSignBean.PayMapBean();
        pmp.setPayTypeId(PayTypeId);
        pmp.setPayVal(neworderInfo.totalPrice);
        payMap.add(pmp);

        //3.0 调起扫码枪的功能，获取支付的付款码。确认支付。
        //payAuthCode="1111111111111111";


        //调用确认支付接口
        ResponseSignBeanCall = RetrofitHelper.getInstance().getSign(payWay, payAuthCode, sPayTypeExt,openid,"",pluMap, payMap);
        ResponseSignBeanCall.enqueue(new Callback<ResponseSignBean>() {
            @Override
            public void onResponse(Call<ResponseSignBean> call, Response<ResponseSignBean> response) {
                if (response != null) {
                    ResponseSignBean body = response.body();

                    if (body.getReturnX().getNCode() == 0) {

                        ResponseSignBean.ResponseBean response1 = body.getResponse();

                        //说明当前支付时成功的，跳转到 支付等待界面
                        Intent intent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);

                        startActivity(intent);

                    } else {
                        //Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrText(),Toast.LENGTH_SHORT).show();
                        String Result = body.getReturnX().getStrText();

                        //66是等待用户输入密码的过程。也会跳转到支付等待界面
                        if ((Result.equals("支付等待") && body.getReturnX().getNCode() == 1) ||
                                body.getReturnX().getNCode() == 66) {

                            Intent intent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);
                            startActivity(intent);
                            return;
                        }

                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", Result);
                        center_dialog.dismiss();
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignBean> call, Throwable t) {

            }
        });

    }


    //刷脸支付初始化
    public  void wxFacepay(){
        Map<String, String> m1 = new HashMap<>();
        //m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
        //m1.put("port", "8888");//若没有代理,则不需要此行*/
        try {
            //1.0 刷脸支付初始化成功
            WxPayFace.getInstance().initWxpayface(InputGoodsActivity.this, m1, new IWxPayfaceCallback() {
                @Override
                public void response(Map info) throws RemoteException {
                    if (!isSuccessInfo(info)) {
                        return;
                    }

                    // 2.0微信刷脸  获取rawdata和AuthInfo 数据
                    try {
                        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (!isSuccessInfo(info)) {
                                    return;
                                }

                                String rawdata = info.get("rawdata").toString();
                                try {
                                    //selfgetAuthInfo(rawdata);

                                    InterfaceGetAuthInfo(rawdata);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    catch(Exception ex){
                        //ToastUtil.showToast(OneActivity.this, "温馨提示", ex.toString());
                    }

                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(InputGoodsActivity.this, "温馨提示", "当前系统未安装微信刷脸程序");
        }
    }




    private boolean isSuccessInfo(Map info) {
        if (info == null) {

            ToastUtil.showToast(InputGoodsActivity.this, "温馨提示", "调用返回为空");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String code = (String) info.get(RETURN_CODE);
        String msg = (String) info.get(RETURN_MSG);


        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
            ToastUtil.showToast(InputGoodsActivity.this, "温馨提示", "调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
            return false;
        }

        return true;
    }


    private void InterfaceGetAuthInfo(String rowdata) {

        getWXFacepayAuthInfoCall = RetrofitHelper.getInstance().getWXFacepayAuthInfo(CommonData.khid, CommonData.userId, rowdata);
        getWXFacepayAuthInfoCall.enqueue(new Callback<getWXFacepayAuthInfo>() {
            @Override
            public void onResponse(Call<getWXFacepayAuthInfo> call, Response<getWXFacepayAuthInfo> response) {

                if (response != null) {
                    getWXFacepayAuthInfo body = response.body();

                    if (body.getReturnX().getNCode() == 0) {

                        getWXFacepayAuthInfo.ResponseBean response1 = body.getResponse();
                        mAuthInfo = response1.getAuthinfo();

                        if (mAuthInfo == null) {

                            ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "设备认证信息获取失败");
                            return;
                        }
                        out_trade_no=response1.getOut_trade_no();
                        responseAppid=response1.getAppid();
                        responnse_subAppid=response1.getSubAppid();
                        responsemachid=response1.getMch_id();
                        responnse_submachid=response1.getSubMchId();

                        double a = CommonData.orderInfo.totalPrice*10*10;
                        int total_fee= new Double(a).intValue();

                        //3.0 然后调用首先获取  facecode。用于人脸支付
                        Map<String, String> m1 = new HashMap<String, String>();
                        m1.put("appid", responseAppid); // 公众号，必填
                        m1.put("mch_id", responsemachid); // 商户号，必填
                        m1.put("sub_appid", responnse_subAppid); // 商户号，必填
                        m1.put("sub_mch_id", responnse_submachid); // 商户号，必填
                        m1.put("store_id", CommonData.khid); // 门店编号，必填
                        m1.put("out_trade_no", out_trade_no); // 商户订单号， 必填
                        m1.put("total_fee", String.valueOf(total_fee)); // 订单金额（数字），单位：分，必填
                        m1.put("face_authtype", "FACEPAY"); // FACEPAY：人脸凭证，常用于人脸支付    FACEPAY_DELAY：延迟支付   必填
                        m1.put("authinfo", mAuthInfo);
                        m1.put("ask_face_permit", "0"); // 展开人脸识别授权项，详情见上方接口参数，必填
                        m1.put("ask_ret_page", "0"); // 是否展示微信支付成功页，可选值："0"，不展示；"1"，展示，非必填

                        WxPayFace.getInstance().getWxpayfaceCode(m1, new IWxPayfaceCallback() {
                            @Override
                            public void response(final Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述
                                String faceCode = info.get("face_code").toString(); // 人脸凭证，用于刷脸支付
                                openid = info.get("openid").toString(); // 人脸凭证，用于刷脸支付


                                if (!code.equals("SUCCESS")) {

                                    //没有成功需要关掉 人脸支付
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("authinfo", mAuthInfo); // 调用凭证，必填
                                    WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                                        @Override
                                        public void response(Map info) throws RemoteException {
                                            if (info == null) {
                                                new RuntimeException("调用返回为空").printStackTrace();
                                                return;
                                            }
                                            String code = (String) info.get("return_code"); // 错误码
                                            String msg = (String) info.get("return_msg"); // 错误码描述
                                            if (code == null || !code.equals("SUCCESS")) {
                                                new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                                return;
                                            }
                                        }
                                    });
                                }
                                        /*String openid = info.get("openid").toString(); // openid
                                        String sub_openid = ""; // 子商户号下的openid(服务商模式)
                                        int telephone_used = 0; // 获取的`face_code`，是否使用了请求参数中的`telephone`
                                        int underage_state = 0; // 用户年龄信息（需联系微信支付开通权限）
                                        if (info.get("sub_openid") != null) sub_openid = info.get("sub_openid").toString();
                                        if (info.get("telephone_used") != null) telephone_used = Integer.parseInt(info.get("telephone_used").toString());
                                        if (info.get("underage_state") != null) underage_state = Integer.parseInt(info.get("underage_state").toString());
                                        if (code == null || faceCode == null || openid == null || !code.equals("SUCCESS")) {
                                            new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                            return ;
                                        }*/
                                //在这里处理您自己的业务逻辑
                                payAuthCode = faceCode;//将刷脸支付返回码进行订单调用

                                wxFaceMoneypay();

                            }
                        });

                    } else {

                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", body.getReturnX().getStrInfo());

                    }
                }

            }

            @Override
            public void onFailure(Call<getWXFacepayAuthInfo> call, Throwable t) {

            }
        });


    }


    public void wxFaceMoneypay() {

        //1.0 初始化所有的产品信息,
        List<RequestSignBean.PluMapBean> pluMap = new ArrayList<RequestSignBean.PluMapBean>();


        try {

            for (Map.Entry<String, List<SplnfoList>> entry : MapList.entrySet()) {

                RequestSignBean.PluMapBean payMapcls = new RequestSignBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getRealPrice()));  //单项实付金额
                payMapcls.setPluPrice(Double.valueOf(entry.getValue().get(0).getMainPrice()));  //单品单价
                payMapcls.setPluAmount(Double.valueOf(entry.getValue().get(0).getMainPrice()));   //单项小计
                pluMap.add(payMapcls);
            }
        } catch (Exception ex) {
            ToastUtil.showToast(InputGoodsActivity.this, "温馨提示", ex.toString());

        }

        //2.0 选取支付方式 ,初始化支付信息
        int PayTypeId = 1;


        List<RequestSignBean.PayMapBean> payMap = new ArrayList<RequestSignBean.PayMapBean>();
        RequestSignBean.PayMapBean pmp = new RequestSignBean.PayMapBean();
        pmp.setPayTypeId(PayTypeId);
        pmp.setPayVal(neworderInfo.totalPrice);
        payMap.add(pmp);

        //调用确认支付接口
        ResponseSignBeanCall = RetrofitHelper.getInstance().getSign(payWay, payAuthCode, sPayTypeExt,openid,out_trade_no,pluMap, payMap);

        ResponseSignBeanCall.enqueue(new Callback<ResponseSignBean>() {
            @Override
            public void onResponse(Call<ResponseSignBean> call, Response<ResponseSignBean> response) {
                if (response!=null){
                    ResponseSignBean body = response.body();
                    if (payWay.equals("WXFacePay")){
                        //首先要关闭微信人脸识别支付的接口
                        /*HashMap<String, String> map = new HashMap<String, String>();
                        map.put("authinfo", mAuthInfo); // 调用凭证，必填
                        WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述
                                if (code == null || !code.equals("SUCCESS")) {
                                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                    return ;
                                }
                            }
                        });*/

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("appid", responseAppid); // 公众号，必填
                        map.put("mch_id", responsemachid); // 商户号，必填
                        map.put("store_id", CommonData.khid); // 门店编号，必填
                        map.put("authinfo", mAuthInfo); // 调用凭证，必填
                        map.put("payresult", "SUCCESS"); // 支付结果，SUCCESS:支付成功   ERROR:支付失败   必填
                        WxPayFace.getInstance().updateWxpayfacePayResult(map, new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述
                                if (code == null || !code.equals("SUCCESS")) {
                                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                    return ;
                                }
                /*
                在这里处理您自己的业务逻辑：
                执行到这里说明用户已经确认支付结果且成功了，此时刷脸支付界面关闭，您可以在这里选择跳转到其它界面
                 */
                            }
                        });


                    }


                    if (body.getReturnX().getNCode()==0){

                        ResponseSignBean.ResponseBean response1 = body.getResponse();
                        //说明当前支付时成功的，跳转到 支付等待界面
                        Intent intent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);

                        startActivity(intent);

                    }
                    else{
                        //Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrText(),Toast.LENGTH_SHORT).show();
                        String  Result=body.getReturnX().getStrText();

                        //返回标识 66 是等待用户输入密码的过程。也会跳转到支付等待界面
                        if ((Result.equals("支付等待")&&body.getReturnX().getNCode()==1)||
                                body.getReturnX().getNCode()==66) {

                            Intent intent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);
                            startActivity(intent);
                            return;
                        }

                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", Result);

                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignBean> call, Throwable t) {

            }
        });

    }



}
