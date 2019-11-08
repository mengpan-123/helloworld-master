package com.ceshi.helloworld;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
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
import com.ceshi.helloworld.bean.upCardCacheEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.CreateAddAdapter;
import com.ceshi.helloworld.net.OrderInfo;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.SplnfoList;
import com.ceshi.helloworld.net.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InputGoodsActivity extends AppCompatActivity implements View.OnClickListener, CreateAddAdapter.RefreshPriceInterface {
    private LinearLayout top_bar;
    private ListView listview;
    private TextView price;
    private TextView tv_go_to_pay;
    private TextView shopcar_num;
    private ImageView text_tip;
    private  TextView yhmoney;
    private  TextView phone_view;

    View layout=null;
    View layout_pay=null;
    View layout_paycode=null;
    private List<HashMap<String,String>> listmap=new ArrayList<>();
    private CreateAddAdapter adapter;
    HashMap<String,String> map=new HashMap<String,String>();
    private double totalPrice = 0.00;
    private int totalCount = 0;
    private Call<Addgoods> Addgoodsinfo;

    private Map<String,List<SplnfoList>>   MapList=new  HashMap<String,List<SplnfoList>>();


    private Call<createPrepayIdEntity> createPrepayIdEntityCall;

    private Call<upCardCacheEntity> upCardCacheEntityCall;

    private Call<getCartItemsEntity> getCartItemsEntityCall;

    private OrderInfo  neworderInfo=new OrderInfo();


    private Call<PurchaseBag> GetBagsInfo;

    //支付方式
    private  String  payWay="WXPaymentCodePay";

    //支付识别码
    private  String  payAuthCode="";

    private Call<ResponseSignBean> ResponseSignBeanCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        initView();
        if (CommonData.hyMessage==null){
            phone_view.setText("");
        }else {
            phone_view.setText(CommonData.hyMessage.hytelphone);
        }

        //一进来就得执行。初始化会员支付，初始化订单号
        Prepay();
    }

    private void initView() {
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        text_tip = (ImageView) findViewById(R.id.text_tip);
        price = (TextView) findViewById(R.id.tv_total_price);
        phone_view=(TextView) findViewById(R.id.phone);
        shopcar_num=findViewById(R.id.shopcar_num);
        yhmoney=findViewById(R.id.yhmoney);
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);

        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
        listview.setAdapter(adapter);
        listview.setEmptyView(text_tip);
        adapter.setRefreshPriceInterface(this);
        priceControl(adapter.getPitchOnMap());

    }


    /**
     * Created by zhoupan on 2019/11/6.
     * 预支付相关信息
     */

    public   void  Prepay(){
        //预支付 相关的操作，给公共订单类赋值


        if (CommonData.orderInfo==null){
            //调用接口拿到订单号
            createPrepayIdEntityCall=RetrofitHelper.getInstance().createPrepayId(CommonData.khid);

            createPrepayIdEntityCall.enqueue(new Callback<createPrepayIdEntity>() {
                @Override
                public void onResponse(Call<createPrepayIdEntity> call, Response<createPrepayIdEntity> response) {
                    if (response!=null){
                        createPrepayIdEntity body = response.body();

                        if (body.getReturnX().getNCode()==0){

                            createPrepayIdEntity.ResponseBean response1 = body.getResponse();

                            //拿到预支付流水号
                            neworderInfo.prepayId=response1.getPrepayId();
                            CommonData.orderInfo=neworderInfo;
                        }
                        else{
                            Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<createPrepayIdEntity> call, Throwable t) {

                }
            });

        }

        if (CommonData.hyMessage!=null){
            //进行会员卡预支付设置
            upCardCacheEntityCall=RetrofitHelper.getInstance().upCardCache(CommonData.khid,neworderInfo.prepayId);
            upCardCacheEntityCall.enqueue(new Callback<upCardCacheEntity>() {
                @Override
                public void onResponse(Call<upCardCacheEntity> call, Response<upCardCacheEntity> response) {
                    if (response!=null){
                        upCardCacheEntity body = response.body();

                        if (body.getReturnX().getNCode()==0){

                        }
                    }
                }

                @Override
                public void onFailure(Call<upCardCacheEntity> call, Throwable t) {
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.all_chekbox:
//                AllTheSelected();
//                break;
//            case R.id.tv_delete:
//                checkDelete(adapter.getPitchOnMap());
//                break;
            case R.id.tv_go_to_pay:
                if(totalCount<=0){
                    Toast.makeText(this,"请选择要付款的商品~",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"付款成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
    }



    /**
     * Created by zhoupan on 2019/11/7.
     * 扫码添加商品，封装方法，用于单个添加商品，和添加购物袋
     */

    public   void  AddnewSpid(String  inputbarcode){

        HashMap<String,String> map=new HashMap<>();

        Addgoodsinfo= RetrofitHelper.getInstance().getgoodsinfo(inputbarcode,CommonData.khid,CommonData.userId,"0");
        Addgoodsinfo.enqueue(new Callback<Addgoods>() {
            @Override
            public void onResponse(Call<Addgoods> call, Response<Addgoods> response) {

                if (response.body()!=null) {
                    map.clear();
                    Addgoods addgoods = response.body();
                    int onCode = addgoods.getReturnX().getNCode();
                    if (onCode == 0) {
                        Addgoods.ResponseBean responseBean = addgoods.getResponse();
                        List<Addgoods.ResponseBean.GoodsListBean> maps = responseBean.getGoodsList();

                        //拿到产品编码
                        String spcode = maps.get(0).getGoodsId();
                        double price = maps.get(0).getRealPrice();
                        double disc = 0;

                        //没录入一次，都去增加总数量和总价
                        neworderInfo.totalCount = neworderInfo.totalCount + 1;  //增加总数量
                        neworderInfo.totalPrice = neworderInfo.totalPrice + price;  //增加总价
                        neworderInfo.totalDisc = neworderInfo.totalDisc + disc;    //增加总折扣

                        //1.0先判断 改产品集合中是否存在
                        if (MapList.containsKey(spcode)) {

                            //如果存在，拿到集合，增加数量，总价，折扣
                            MapList.get(spcode).get(0).setPackNum(MapList.get(spcode).get(0).getPackNum() + 1);
                            MapList.get(spcode).get(0).setMainPrice(MapList.get(spcode).get(0).getMainPrice() + price);

                            //更改 映射的 map的内容
                            for (int k = 0; k < listmap.size(); k++) {
                                if (listmap.get(k).get("id").equals(spcode)) {
                                    listmap.get(k).put("count", String.valueOf(Integer.valueOf(listmap.get(k).get("count")) + 1));
                                }
                            }
                        } else {
                            List<SplnfoList> uselist = new ArrayList<SplnfoList>();
                            SplnfoList usesplnfo = new SplnfoList();

                            usesplnfo.setBarcode(maps.get(0).getBarcode());
                            usesplnfo.setGoodsId(maps.get(0).getGoodsId());
                            usesplnfo.setMainPrice(maps.get(0).getMainPrice());
                            usesplnfo.setPackNum(maps.get(0).getPackNum());
                            usesplnfo.setPluName(maps.get(0).getPluName());
                            usesplnfo.setPluTypeId(maps.get(0).getPluTypeId());
                            usesplnfo.setPluUnit(maps.get(0).getPluUnit());
                            usesplnfo.setRealPrice(maps.get(0).getRealPrice());
                            double totalPrice = (maps.get(0).getPackNum() * maps.get(0).getRealPrice());
                            uselist.add(usesplnfo);

                            //说明产品不存在，直接增加进去
                            MapList.put(spcode, uselist);

                            //下面是只取几个字段去改变 界面显示的
                            map.put("id", maps.get(0).getGoodsId());
                            map.put("name", maps.get(0).getPluName());
                            map.put("price", String.valueOf(maps.get(0).getRealPrice()));
                            map.put("count", String.valueOf(maps.get(0).getPackNum()));

                            listmap.add(map);

                        }

                        //界面上实现 增加一个元素
                        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
                        listview.setAdapter(adapter);
                        adapter.setRefreshPriceInterface(InputGoodsActivity.this);
                        priceControl(adapter.getPitchOnMap());
                    }
                    else{
                        String result=addgoods.getReturnX().getStrText();

                        ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", result);
                        //Toast.makeText(InputGoodsActivity.this,result,Toast.LENGTH_SHORT).show();
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
    private void priceControl(Map<String, Integer> pitchOnMap){
        totalCount = 0;
        totalPrice = 0.00;
        for(int i=0;i<listmap.size();i++){
            totalCount=totalCount+Integer.valueOf(listmap.get(i).get("count"));
            double goodsPrice=Integer.valueOf(listmap.get(i).get("count"))*Double.valueOf(listmap.get(i).get("price"));
            totalPrice=totalPrice+goodsPrice;
        }
        price.setText(" ¥ "+totalPrice);
        if (CommonData.orderInfo==null){
            yhmoney.setText("优惠 ￥ "+0.00);
        }else {
            yhmoney.setText("优惠 ￥ "+CommonData.orderInfo.totalDisc);
        }

        shopcar_num.setText("共"+pitchOnMap.size()+"件商品");
        tv_go_to_pay.setText("付款("+totalPrice+")");
    }

    /**
     * 删除 控制价格展示总价
     * @param map
     */
    private void checkDelete(Map<String,Integer> map){
        List<HashMap<String,String>> waitDeleteList=new ArrayList<>();
        Map<String,Integer> waitDeleteMap =new HashMap<>();
        for(int i=0;i<listmap.size();i++){
            if(map.get(listmap.get(i).get("id"))==1){
                waitDeleteList.add(listmap.get(i));
                waitDeleteMap.put(listmap.get(i).get("id"),map.get(listmap.get(i).get("id")));
            }
        }
        listmap.removeAll(waitDeleteList);
        map.remove(waitDeleteMap);
        priceControl(map);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * 手动输入条码  和 取消
     * input_tiaoma
     * **/
    public  void  input_tiaoma(View view){
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

        dialog.show();
        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1=layout.findViewById(R.id.username);
                String  inputbarcode=editText1.getText().toString();

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
     *
     * 选择购物袋
     * input_tiaoma
     * **/
    public  void  input_bags(View view){

        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);

        // 自定义对话框布局
        layout = View.inflate(this, R.layout.bagingo_one,
                null);
        dialog.setContentView(layout);

        ListView listView=(ListView) layout.findViewById(R.id.lv_baginfo);
        /*listView.setVisibility(View.VISIBLE);
        listView.bringToFront();*/
        listView.setDividerHeight(20);
        List<Map<String, Object>> listitem=new ArrayList<Map<String,Object>>();

        GetBagsInfo= RetrofitHelper.getInstance().getBagInfo("526374","S101");
        GetBagsInfo.enqueue(new Callback<PurchaseBag>() {
            @Override
            public void onResponse(Call<PurchaseBag> call, Response<PurchaseBag> response) {

                if (response.body()!=null){
                    PurchaseBag getBagsInfo=response.body();

                    int onCode=getBagsInfo.getReturnX().getNCode();
                    if (onCode==0){
                        PurchaseBag.ResponseBean responseBean=getBagsInfo.getResponse();
                        List<PurchaseBag.ResponseBean.BagMapBean> maps=responseBean.getBagMap();
                        int n=20;
                        int[]  img_expense=new  int[n];
                        String[] tv_bagname=new  String[n];
                        String[] tv_bagpic=new String[n];
                        String[] tv_goodsid=new  String[n];
                        for (int m=0; m<maps.size();m++){

                            Map<String,Object> map=new HashMap<String, Object>();
                            map.put("img_expense",R.mipmap.gwd);
                            map.put("tv_bagname", maps.get(m).getPluName());
                            map.put("tv_bagpic", String.valueOf(maps.get(m).getPluPrice()));
                            map.put("tv_goodsid", maps.get(m).getGoodsId());
                            listitem.add(map);
                        }

                        SimpleAdapter adapter=new SimpleAdapter(InputGoodsActivity.this,listitem,R.layout.baginfo, new String[]{"tv_bagname","tv_bagpic","img_expense","tv_goodsid"},new int[]{R.id.tv_bagname,R.id.tv_bagpic,R.id.img_expense,R.id.tv_goodsid} );
                        listView.setAdapter(adapter);
                        dialog.show();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String,String> map=(HashMap<String, String>) listView.getItemAtPosition(position);
                                String title=map.get("tv_bagname");
                                String bagpic=map.get("tv_bagpic");
                                String bagsid=map.get("tv_goodsid");


                                AddnewSpid(bagsid);


                                dialog.dismiss();
                            }
                        });
                    }
                    else {

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
     *
     * 数字键盘事件
     *
     * */
    public  void  numClick(View view){
        Button bt=(Button)view;
        String text= bt.getText().toString();
        EditText editText1=layout.findViewById(R.id.username);
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
     *
     * 删除按钮事件
     *
     * */
    public  void  deleteClick(View view){

        EditText editText=layout.findViewById(R.id.username);
        if (null!=editText.getText().toString()&&editText.getText().toString().length()>0){
            String old_text=editText.getText().toString();
            editText.setText(old_text.substring(0,old_text.length()-1));
            editText.setSelection(editText.getText().toString().length());
        }
    }

    /**
     *
     *返回首页
     */
    public  void return_home(View view){
        Intent intent = new Intent(InputGoodsActivity.this, IndexActivity.class);
        startActivity(intent);
    }

    /**
     *
     * deletecar
     * 清空购物车
     * */

    public  void  deletecar(View view){
    //清空listmap的值
        listmap.clear();
        //清空列表页面
        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
        listview.setAdapter(adapter);
        adapter.setRefreshPriceInterface(InputGoodsActivity.this);
        priceControl(adapter.getPitchOnMap());

        //加载出空购物车页面
        listview.setEmptyView(text_tip);

    }


    /**
     * to_pay
     * 去支付，首先选择去支付的支付方式
     * */

    public  void  to_pay(View view){

        //首先要判断是否增加了产品
        if (CommonData.orderInfo==null||CommonData.orderInfo.spList==null){
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
                payWay="AliPaymentCodePay";
                dialog_paycode(view);


            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                payWay="WXPaymentCodePay";
                dialog_paycode(view);
            }
        });
        wx_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                payWay="WXFacePay";
                dialog_paycode(view);
            }
        });
    }
    /**
     *
     * 弹出输入支付码的页面
     *
     * */

    public  void  dialog_paycode(View view){

        final Dialog dialog_paycode = new Dialog(this, R.style.myNewsDialogStyle);
        // 自定义对话框布局
        layout_paycode = View.inflate(this, R.layout.input_paycode, null);
        dialog_paycode.setContentView(layout_paycode);
        Window window = dialog_paycode.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0f);
        dialog_paycode.show();
        Button require_code = (Button) layout_paycode.findViewById(R.id.require_code);


        //获取到支付码的控件
        EditText input_code=layout_paycode.findViewById(R.id.pay_code);



        require_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String authcode = input_code.getText().toString();

                    if (authcode.length()<18){
                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "请输入完整的18位支付码");
                        return;
                    }
                    else {
                        payAuthCode = authcode;
                        //去支付
                        Moneypay();
                    }
                }catch(Exception ex){

                }

            }
        });

    }



    /**
     * Moneypay
     * Created by zhoupan on 2019/11/8.
     * 选择支付方式之后进行支付
     * */

    public   void   Moneypay(){

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
        List<RequestSignBean.PluMapBean> pluMap =new ArrayList<RequestSignBean.PluMapBean>();

        try {

            for(Map.Entry<String,List<SplnfoList>> entry : MapList.entrySet()) {

                RequestSignBean.PluMapBean payMapcls = new RequestSignBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getMainPrice()));
                pluMap.add(payMapcls);
            }
        }
        catch(Exception ex){

        }

        //2.0 选取支付方式 ,初始化支付信息
        int PayTypeId=1;
        if (payWay.equals("WXPaymentCodePay")){
            PayTypeId=5;
        }else if(payWay.equals("AliPaymentCodePay")){
            PayTypeId=7;
        }

        List<RequestSignBean.PayMapBean> payMap=new ArrayList<RequestSignBean.PayMapBean>();
        RequestSignBean.PayMapBean pmp=new   RequestSignBean.PayMapBean();
        pmp.setPayTypeId(PayTypeId);
        pmp.setPayVal(neworderInfo.totalPrice);
        payMap.add(pmp);

        //3.0 调起扫码枪的功能，获取支付的付款码。确认支付。
        //payAuthCode="1111111111111111";



        //调用确认支付接口
        ResponseSignBeanCall =RetrofitHelper.getInstance().getSign(payWay,payAuthCode,pluMap,payMap);
        ResponseSignBeanCall.enqueue(new Callback<ResponseSignBean>() {
            @Override
            public void onResponse(Call<ResponseSignBean> call, Response<ResponseSignBean> response) {
                if (response!=null){
                    ResponseSignBean body = response.body();

                    if (body.getReturnX().getNCode()==0){

                        ResponseSignBean.ResponseBean response1 = body.getResponse();

                        //说明当前支付时成功的，跳转到 支付等待界面
                        Intent intent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);

                        startActivity(intent);

                    }
                    else{
                        //Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrText(),Toast.LENGTH_SHORT).show();
                        String  Result=body.getReturnX().getStrText();

                        ToastUtil.showToast(InputGoodsActivity.this, "支付通知", Result);
                        return;

                      /*  try {
                            Intent newintent = new Intent(InputGoodsActivity.this, WaitingFinishActivity.class);
                            startActivity(newintent);
                        }
                        catch(Exception ex){

                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignBean> call, Throwable t) {

            }
        });

    }

}
