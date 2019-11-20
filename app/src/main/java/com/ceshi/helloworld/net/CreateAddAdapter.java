package com.ceshi.helloworld.net;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ceshi.helloworld.R;
import com.ceshi.helloworld.bean.RequestDeleteGoods;
import com.ceshi.helloworld.bean.ResponseDeleteGoods;
import com.ceshi.helloworld.bean.getCartItemsEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAddAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, String>> list;
    private HashMap<String, Integer> pitchOnMap;
    public Call<ResponseDeleteGoods> ResponseDeleteGoodsCall;
    private Call<com.ceshi.helloworld.bean.getCartItemsEntity> getCartItemsEntityCall;

    private OnItemRemoveListener adapterListener;
    public HashMap<String, Integer> getPitchOnMap() {
        return pitchOnMap;
    }

    public void setPitchOnMap(HashMap<String, Integer> pitchOnMap) {
        this.pitchOnMap = pitchOnMap;
    }

    public CreateAddAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;

        pitchOnMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            pitchOnMap.put(list.get(i).get("id"), 0);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context,  R.layout.activity_addgoods, null);
        final ListView listView1;
        listView1=convertView.findViewById(R.id.listview);
        convertView = View.inflate(context,  R.layout.shopcar_list, null);
        final CheckBox checkBox;
        ImageView icon;
        final TextView name, price, num, type, reduce, add,describe,y_price,y_title ;

        name = convertView.findViewById(R.id.tv_goods_name);//商品名称
        price = convertView.findViewById(R.id.tv_goods_price);//商品价格
        num = convertView.findViewById(R.id.tv_num);//商品数量
        reduce = convertView.findViewById(R.id.tv_reduce);//减号
        y_price=convertView.findViewById(R.id.tv_yuan_price);//原价格
        y_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        describe=convertView.findViewById(R.id.describe);//促销信息
        y_title=convertView.findViewById(R.id.tv_yuan_title);

       // add = convertView.findViewById(R.id.tv_add);

        try {

            name.setText(list.get(position).get("name"));//产品名称
            y_price.setText(list.get(position).get("MainPrice"));//原价
            price.setText(list.get(position).get("realprice"));//实际售价
            num.setText(list.get(position).get("count"));//商品数量
            describe.setText(list.get(position).get("actname"));//促销活动

            if ("0.00".equals(list.get(position).get("disc"))||"0".equals(list.get(position).get("disc"))||"0.0".equals(list.get(position).get("disc"))){

                y_price.setVisibility(View.INVISIBLE);
                y_title.setVisibility(View.INVISIBLE);
            }
            else {

                y_price.setVisibility(View.VISIBLE);
                y_title.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){


        }


        //商品数量减
        /*reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  CommonData.list_adaptor = new CreateAddAdapter(InputGoodsActivity.this, list);
                listView.setAdapter(CommonData.list_adaptor);*//*

                List<RequestDeleteGoods.ItemMapBean> itemMap =new ArrayList<RequestDeleteGoods.ItemMapBean>();
                RequestDeleteGoods.ItemMapBean itemMapcls = new RequestDeleteGoods.ItemMapBean();
                itemMapcls.setBarcode(list.get(position).get("id"));
                itemMap.add(itemMapcls);
                ResponseDeleteGoodsCall=RetrofitHelper.getInstance().responseDeleteGoods(itemMap);
                ResponseDeleteGoodsCall.enqueue(new Callback<ResponseDeleteGoods>() {
                    @Override
                    public void onResponse(Call<ResponseDeleteGoods> call, Response<ResponseDeleteGoods> response) {
                        if (response!=null){
                            ResponseDeleteGoods body=response.body();
                            if (body.getReturnX().getNCode()==0){
                                ResponseDeleteGoods.ResponseBean response1 = body.getResponse();
                                if (Integer.valueOf(list.get(position).get("count")) <= 1) {
                                    // Toast.makeText(context, "数量不能再减啦,只能删除!", Toast.LENGTH_SHORT).show();
                                    //list.remove(position);
                                    try {
                                        CommonData.orderInfo.spList.remove(list.get(position).get("id"));
//                                        BigDecimal price= BigDecimal.valueOf(Double.valueOf(list.get(position).get("price")));

                                        pitchOnMap.remove(list.get(position).get("id"));
                                        list.remove(position);
                                        CommonData.orderInfo.totalCount=CommonData.orderInfo.totalCount-1;

                                        //CommonData.orderInfo.totalPrice=(BigDecimal.valueOf(CommonData.orderInfo.totalPrice).subtract(price)).doubleValue();


                                    }
                                    catch(Exception ex){}

                                } else {
                                    //pitchOnMap.remove(list.get(position).get("id"));
                                    list.get(position).put("count", (Integer.valueOf(list.get(position).get("count")) - 1) + "");
//                                    BigDecimal price= BigDecimal.valueOf(Double.valueOf(list.get(position).get("price")));
                                    CommonData.orderInfo.totalCount=CommonData.orderInfo.totalCount-1;
                                    //CommonData.orderInfo.totalPrice=(BigDecimal.valueOf(CommonData.orderInfo.totalPrice).subtract(price)).doubleValue();

                                }

                                //重新 设置 界面的显示值
                                //InputGoodsActivity.shopcar_num.setText(CommonData.orderInfo.totalCount);

                                  CommonData.list_adaptor = new CreateAddAdapter(CreateAddAdapter.this.context, list);
                                   listView1.setAdapter(CommonData.list_adaptor);
                                 //   CommonData.list_adaptor.setRefreshPriceInterface(CreateAddAdapter.this);

                                 notifyDataSetChanged();
                               *//* pitchOnMap = new HashMap<>();
                                for (int i = 0; i < list.size(); i++) {
                                    pitchOnMap.put(list.get(i).get("id"), 0);
                                }*//*
                                mrefreshPriceInterface.refreshPrice(pitchOnMap);

                            }else {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDeleteGoods> call, Throwable t) {

                    }
                });

                mrefreshPriceInterface.refreshPrice(pitchOnMap);
            }
        });*/




        /**
         * Created by zhoupan on 2019/11/18.
         * 删减购物车数量，因为数据显示原因，重新写
         */

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //调用减少购物车商品数量接口 ，然后调用接口获取购物车列表

                //初始化报文信息
                List<RequestDeleteGoods.ItemMapBean> itemMap =new ArrayList<RequestDeleteGoods.ItemMapBean>();
                RequestDeleteGoods.ItemMapBean itemMapcls = new RequestDeleteGoods.ItemMapBean();
                itemMapcls.setBarcode(list.get(position).get("barcode"));
                itemMap.add(itemMapcls);

                ResponseDeleteGoodsCall=RetrofitHelper.getInstance().responseDeleteGoods(itemMap);
                ResponseDeleteGoodsCall.enqueue(new Callback<ResponseDeleteGoods>() {
                    @Override
                    public void onResponse(Call<ResponseDeleteGoods> call, Response<ResponseDeleteGoods> response) {
                        if (response!=null){
                            if (response.body().getReturnX().getNCode()==0) {

                                //获取 购物车列表。然后解析购物车列表
                                getCartItemsEntityCall = RetrofitHelper.getInstance().getCartItems(CommonData.userId, CommonData.khid);
                                getCartItemsEntityCall.enqueue(new Callback<getCartItemsEntity>() {
                                    @Override
                                    public void onResponse(Call<getCartItemsEntity> call, Response<getCartItemsEntity> response) {
                                        if (response != null) {
                                            getCartItemsEntity body = response.body();
                                            if (body.getReturnX().getNCode() == 0) {
                                                //在这里重新写一个
                                                Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();

                                                CommonData.orderInfo.spList.clear();

                                                list.clear();
                                                pitchOnMap.clear();
                                                //下面先  获取到 总价和总金额折扣这些
                                                CommonData.orderInfo.totalCount = body.getResponse().getTotalQty();
                                                CommonData.orderInfo.totalPrice = body.getResponse().getShouldAmount();
                                                CommonData.orderInfo.totalDisc = body.getResponse().getDisAmount();

                                                //因为 可能存在组合促销啥的，少一个产品，描述会不一样，所以 移除最好就是全部重新加载一次
                                                List<getCartItemsEntity.ResponseBean.ItemsListBean> itemsList = body.getResponse().getItemsList();
                                                for (int sm = 0; sm < itemsList.size(); sm++) {
                                                    List<getCartItemsEntity.ResponseBean.ItemsListBean.ItemsBean> sub_itemsList = itemsList.get(sm).getItems();
                                                    for (int sk = 0; sk < sub_itemsList.size(); sk++) {
                                                        HashMap<String, String> map = new HashMap<>();
                                                        //拿到产品编码
                                                        String spcode = sub_itemsList.get(sk).getSGoodsId();
                                                        double nRealPrice = sub_itemsList.get(sk).getNPluPrice();

                                                        if (MapList.containsKey(spcode)) {

                                                            //如果存在，拿到集合，增加数量，总价，折扣
                                                            MapList.get(spcode).get(0).setPackNum(sub_itemsList.get(sk).getNQty());
                                                            MapList.get(spcode).get(0).setMainPrice(sub_itemsList.get(sk).getNRealPrice());

                                                            //修改列表的数量
                                                            for (int k = 0; k < list.size(); k++) {
                                                                if (list.get(k).get("id").equals(spcode)) {
                                                                    list.get(k).put("count", String.valueOf(sub_itemsList.get(sk).getNQty()));
                                                                    list.get(k).put("RealPrice", String.valueOf(nRealPrice));
                                                                    list.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));
                                                                    list.get(k).put("actname", itemsList.get(sm).getDisRule());
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

                                                            uselist.add(usesplnfo);
                                                            //说明产品不存在，直接增加进去
                                                            MapList.put(spcode, uselist);


                                                            //下面是只取几个字段去改变 界面显示的
                                                            map.put("barcode", sub_itemsList.get(sk).getSBarcode());
                                                            map.put("id", spcode);
                                                            map.put("name", sub_itemsList.get(sk).getSGoodsName());
                                                            map.put("MainPrice", String.valueOf(nRealPrice));  //原价
                                                            map.put("disc", String.valueOf(sub_itemsList.get(sk).getPluDis()));  //折扣
                                                            map.put("realprice", sub_itemsList.get(sk).getPluRealAmount());
                                                            map.put("count", String.valueOf(sub_itemsList.get(sk).getNQty()));
                                                            map.put("actname", itemsList.get(sm).getDisRule());
                                                            list.add(map);
                                                            pitchOnMap.put(spcode,0);

                                                        }
                                                    }
                                                }
                                                //在上面 遍历过 之后重新赋值
                                                CommonData.orderInfo.spList = MapList;

                                                //界面上实现  增加一个元素
                                                CommonData.list_adaptor = new CreateAddAdapter(CreateAddAdapter.this.context, list);
                                                listView1.setAdapter(CommonData.list_adaptor);
                                                notifyDataSetChanged();
                                                mrefreshPriceInterface.refreshPrice(pitchOnMap);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<getCartItemsEntity> call, Throwable t) {

                                    }
                                });
                                mrefreshPriceInterface.refreshPrice(pitchOnMap);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDeleteGoods> call, Throwable t) {

                    }
                });

                mrefreshPriceInterface.refreshPrice(pitchOnMap);
            }
        });






        //商品数量加
       // add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.get(position).put("count", (Integer.valueOf(list.get(position).get("count")) + 1) + "");
//                notifyDataSetChanged();
//                mrefreshPriceInterface.refreshPrice(pitchOnMap);
//
//            }

     //   });

        return convertView;
    }

    /**
     * 创建接口
     */
    public interface RefreshPriceInterface {
        /**
         * 把价格展示到总价上
         * @param pitchOnMap
         */
        void refreshPrice(HashMap<String, Integer> pitchOnMap);
    }

    /**
     * 定义一个接口对象
     */
    private RefreshPriceInterface mrefreshPriceInterface;

    /**
     * 向外部暴露一个方法
     * 把价格展示到总价上
     * @param refreshPriceInterface
     */
    public void setRefreshPriceInterface(RefreshPriceInterface refreshPriceInterface) {
        mrefreshPriceInterface = refreshPriceInterface;
    }

    public static interface OnItemRemoveListener {
        public void onItemRemove(int position);
    }

}
