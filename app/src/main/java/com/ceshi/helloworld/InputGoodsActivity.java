package com.ceshi.helloworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView delete;
    private TextView tv_go_to_pay;
    private TextView shopcar_num;
    View layout=null;
    private List<HashMap<String,String>> listmap=new ArrayList<>();
    private CreateAddAdapter adapter;
    HashMap<String,String> map=new HashMap<String,String>();
    private double totalPrice = 0.00;
    private int totalCount = 0;
    private Call<Addgoods> Addgoodsinfo;
   /* private List<OrderInfo> orderInfos;*/
    OrderInfo  orderInfos=new  OrderInfo();
    List<SplnfoList>   spList  =new ArrayList<SplnfoList>() ;


    private Call<createPrepayIdEntity> createPrepayIdEntityCall;

    private Call<upCardCacheEntity> upCardCacheEntityCall;

    private OrderInfo  neworderInfo=new OrderInfo();

private  Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        initView();



    }

    private void initView() {
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        price = (TextView) findViewById(R.id.tv_total_price);
        shopcar_num=findViewById(R.id.shopcar_num);


        /**
         * Created by zhoupan on 2019/11/6.
         * 点击去支付时触发,优先选择支付方式，暂时默认使用微信把
         */
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        //tv_go_to_pay.setOnClickListener(this);
        tv_go_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {
                //1.0 初始化所有的产品信息

                //2.0 选取支付方式

                //3.0 调起扫码枪的功能，获取支付的付款码。确认支付

            }
        });



        adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
        listview.setAdapter(adapter);
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
    /**
     * 数据
     */
//    private void initDate() {
//        for (int i =0;i<)
//
////        for(int i=0;i<10;i++){
////            HashMap<String,String> map=new HashMap<>();
////            map.put("id",i+"");
////            map.put("name",i+"name");
////            map.put("type",i+"type");
////            map.put("price",i+"");
////            map.put("count",i+"");
////            listmap.add(map);
////        }
  //  }

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
                orderInfos.spList=spList;
                EditText editText1=layout.findViewById(R.id.username);
                String  barcode=editText1.getText().toString();
                Addgoodsinfo= RetrofitHelper.getInstance().getgoodsinfo("6907992501468","S101","526374","0");
                Addgoodsinfo.enqueue(new Callback<Addgoods>() {
                    @Override
                    public void onResponse(Call<Addgoods> call, Response<Addgoods> response) {
                        Addgoods addgoods=response.body();

                        int onCode=addgoods.getReturnX().getNCode();
                        Addgoods.ResponseBean responseBean=addgoods.getResponse();
                        List<Addgoods.ResponseBean.GoodsListBean> maps=responseBean.getGoodsList();
                        for (int i=0;i<maps.size(); i++){
                          SplnfoList splnfoList=new SplnfoList();
                          if (orderInfos.spList.size()!=0){

                              for (int j=0;j<orderInfos.spList.size();j++){
                                  if (orderInfos.spList.get(j).getGoodsId()!=maps.get(i).getGoodsId()){
                                      splnfoList.setBarcode(maps.get(i).getBarcode());
                                      splnfoList.setGoodsId(maps.get(i).getGoodsId());
                                      splnfoList.setMainPrice(maps.get(i).getMainPrice());
                                      splnfoList.setPackNum(maps.get(i).getPackNum());
                                      splnfoList.setPluName(maps.get(i).getPluName());
                                      splnfoList.setPluTypeId(maps.get(i).getPluTypeId());
                                      splnfoList.setPluUnit(maps.get(i).getPluUnit());
                                      splnfoList.setRealPrice(maps.get(i).getRealPrice());
                                      double totalPrice=(maps.get(i).getPackNum()*maps.get(i).getRealPrice());
                                      splnfoList.setTotalPrice(totalPrice);
                                  }else {
                                      splnfoList=orderInfos.spList.get(j);
                                      splnfoList.setPackNum( splnfoList.getPackNum()+maps.get(i).getPackNum());
                                  }

                              }
                          }else {
                              splnfoList.setBarcode(maps.get(i).getBarcode());
                              splnfoList.setGoodsId(maps.get(i).getGoodsId());
                              splnfoList.setMainPrice(maps.get(i).getMainPrice());
                              splnfoList.setPackNum(maps.get(i).getPackNum());
                              splnfoList.setPluName(maps.get(i).getPluName());
                              splnfoList.setPluTypeId(maps.get(i).getPluTypeId());
                              splnfoList.setPluUnit(maps.get(i).getPluUnit());
                              splnfoList.setRealPrice(maps.get(i).getRealPrice());
                              double totalPrice=(maps.get(i).getPackNum()*maps.get(i).getRealPrice());
                              splnfoList.setTotalPrice(totalPrice);
                          }
                          orderInfos.spList.add(splnfoList);
                        }
                        for (int i=0;i<orderInfos.spList.size();i++){

                            map.put("id",orderInfos.spList.get(i).getGoodsId());
                            map.put("name",orderInfos.spList.get(i).getPluName());
                            map.put("price",String.valueOf(orderInfos.spList.get(i).getRealPrice()));
                            map.put("count",String.valueOf(orderInfos.spList.get(i).getPackNum()));
                            listmap.add(map);
                        }


                      /*  final  List<HashMap<String,String>> result=listmap;
                        InputGoodsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapter = new CreateAddAdapter(InputGoodsActivity.this, result);
                                listview.setAdapter(adapter);
                                adapter.setRefreshPriceInterface(InputGoodsActivity.this);
                                priceControl(adapter.getPitchOnMap());
                            }
                        });*/

                                adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
                                listview.setAdapter(adapter);
                                adapter.setRefreshPriceInterface(InputGoodsActivity.this);
                                priceControl(adapter.getPitchOnMap());


                    }

                    @Override
                    public void onFailure(Call<Addgoods> call, Throwable t) {

                    }
                });
//                adapter = new CreateAddAdapter(InputGoodsActivity.this, listmap);
//                listview.setAdapter(adapter);
//                adapter.setRefreshPriceInterface(InputGoodsActivity.this);
//                priceControl(adapter.getPitchOnMap());
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

}
