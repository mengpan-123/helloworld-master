package com.ceshi.helloworld;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.Addgoods;
import com.ceshi.helloworld.bean.createPrepayIdEntity;
import com.ceshi.helloworld.bean.upCardCacheEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.CreateAddAdapter;
import com.ceshi.helloworld.net.OrderInfo;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.SplnfoList;

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

    View layout=null;
    View layout_pay=null;
    private List<HashMap<String,String>> listmap=new ArrayList<>();
    private CreateAddAdapter adapter;
    HashMap<String,String> map=new HashMap<String,String>();
    private double totalPrice = 0.00;
    private int totalCount = 0;
    private Call<Addgoods> Addgoodsinfo;
   /* private List<OrderInfo> orderInfos;*/
    OrderInfo  orderInfos=new  OrderInfo();

    private Map<String,List<SplnfoList>>   MapList=new  HashMap<String,List<SplnfoList>>();


    private Call<createPrepayIdEntity> createPrepayIdEntityCall;

    private Call<upCardCacheEntity> upCardCacheEntityCall;

    private OrderInfo  neworderInfo=new OrderInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        initView();
    }

    private void initView() {
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        text_tip = (ImageView) findViewById(R.id.text_tip);
        price = (TextView) findViewById(R.id.tv_total_price);
        shopcar_num=findViewById(R.id.shopcar_num);
//        /**
//         * Created by zhoupan on 2019/11/6.
//         * 点击去支付时触发,优先选择支付方式，暂时默认使用微信把
//         */
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
//        tv_go_to_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public  void  onClick(View view) {
//
//
//                //1.0 初始化所有的产品信息
//
//                //2.0 选取支付方式
//
//                //3.0 调起扫码枪的功能，获取支付的付款码。确认支付
//
//            }
//        });



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
        //预支付 相关的操作
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
     * 手动输入条码tv_go_to_pay
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
                HashMap<String,String> map=new HashMap<>();

                Addgoodsinfo= RetrofitHelper.getInstance().getgoodsinfo(inputbarcode,"S101","526374","0");
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
                                Toast.makeText(InputGoodsActivity.this,result,Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<Addgoods> call, Throwable t) {

                    }
                });

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
     * 去支付
     * */

    public  void  to_pay(View view){

                final Dialog dialog1 = new Dialog(this, R.style.myNewsDialogStyle);
               // 自定义对话框布局
                layout_pay = View.inflate(this, R.layout.activity_chosepay, null);
                dialog1.setContentView(layout_pay);
                Window window = dialog1.getWindow();
                window.setGravity(Gravity.BOTTOM);
                dialog1.show();
                View zfb = (View) layout_pay.findViewById(R.id.zfb);
                View wx = (View) layout_pay.findViewById(R.id.wx);
                View wx_face = (View) layout_pay.findViewById(R.id.wx_face);

                zfb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                wx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                wx_face.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

    }

}
