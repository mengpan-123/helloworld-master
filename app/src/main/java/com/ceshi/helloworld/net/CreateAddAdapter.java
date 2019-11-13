package com.ceshi.helloworld.net;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceshi.helloworld.InputGoodsActivity;
import com.ceshi.helloworld.R;
import com.ceshi.helloworld.bean.RequestDeleteGoods;
import com.ceshi.helloworld.bean.ResponseDeleteGoods;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAddAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, String>> list;
    private HashMap<String, Integer> pitchOnMap;
    public Call<ResponseDeleteGoods> ResponseDeleteGoodsCall;
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
        final TextView name, price, num, type, reduce, add;

        name = convertView.findViewById(R.id.tv_goods_name);
        price = convertView.findViewById(R.id.tv_goods_price);
        num = convertView.findViewById(R.id.tv_num);
        reduce = convertView.findViewById(R.id.tv_reduce);
       // add = convertView.findViewById(R.id.tv_add);

        name.setText(list.get(position).get("name"));
        price.setText("¥ " + new BigDecimal(String.valueOf((Double.valueOf(list.get(position).get("price"))) * (Integer.valueOf(list.get(position).get("count"))))).setScale(2, BigDecimal.ROUND_HALF_UP).toString() );

        num.setText(list.get(position).get("count"));

        if(pitchOnMap.get(list.get(position).get("id"))== 0){

        }else{

        }



        //商品数量减
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  CommonData.list_adaptor = new CreateAddAdapter(InputGoodsActivity.this, list);
                listView.setAdapter(CommonData.list_adaptor);*/

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
                                        BigDecimal price= BigDecimal.valueOf(Double.valueOf(list.get(position).get("price")));

                                        pitchOnMap.remove(list.get(position).get("id"));
                                        list.remove(position);
                                        CommonData.orderInfo.totalCount=CommonData.orderInfo.totalCount-1;

                                        CommonData.orderInfo.totalPrice=(BigDecimal.valueOf(CommonData.orderInfo.totalPrice).subtract(price)).doubleValue();


                                    }
                                    catch(Exception ex){}

                                } else {
                                    //pitchOnMap.remove(list.get(position).get("id"));
                                    list.get(position).put("count", (Integer.valueOf(list.get(position).get("count")) - 1) + "");
                                    BigDecimal price= BigDecimal.valueOf(Double.valueOf(list.get(position).get("price")));
                                    CommonData.orderInfo.totalCount=CommonData.orderInfo.totalCount-1;
                                    CommonData.orderInfo.totalPrice=(BigDecimal.valueOf(CommonData.orderInfo.totalPrice).subtract(price)).doubleValue();

                                }

                                //重新 设置 界面的显示值
                                //InputGoodsActivity.shopcar_num.setText(CommonData.orderInfo.totalCount);

                                  CommonData.list_adaptor = new CreateAddAdapter(CreateAddAdapter.this.context, list);
                                   listView1.setAdapter(CommonData.list_adaptor);
                                 //   CommonData.list_adaptor.setRefreshPriceInterface(CreateAddAdapter.this);

                                 notifyDataSetChanged();
                               /* pitchOnMap = new HashMap<>();
                                for (int i = 0; i < list.size(); i++) {
                                    pitchOnMap.put(list.get(i).get("id"), 0);
                                }*/
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
