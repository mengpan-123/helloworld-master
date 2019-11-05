package com.ceshi.helloworld;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.net.RetrofitHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 2.学习简单的网络请求
 * <p>
 * 举个例子: https://xiaobai.jikewangluo.cn/renwu/taskinfo?u_id=71&t_id=22832
 */
public class NetWorkActivity extends AppCompatActivity {

    private Call<TaskDetailEntity> taskDetail;
    private TextView tvTaskName;
    private TextView tvAlldata;
    private Call<StoreIdEntity> storeIdEntityCall;
    private Call<ClearCarEntity> ClearCarEntityCall;

    private Call<PurchaseBag> PurchaseBagCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTaskName = findViewById(R.id.tv_taskname);
        tvAlldata = findViewById(R.id.tv_alldata);

        //参数就可以动态传过去
        /*storeIdEntityCall = RetrofitHelper.getInstance().getStoreId("S2101");
        storeIdEntityCall.enqueue(new Callback<StoreIdEntity>() {
            @Override
            public void onResponse(Call<StoreIdEntity> call, Response<StoreIdEntity> response) {
                if (response != null) {
                    StoreIdEntity body = response.body();
                    //这里就直接拿到后台返回的对象StoreIdEntity  比如我要取return下的code
                    StoreIdEntity.ReturnBean returnX = body.getReturnX();

                    int nCode = returnX.getNCode();
                    Log.e("xuke", "code=" + nCode);

                    //code=0 就表示成功了

                    StoreIdEntity.ResponseBean response1 = body.getResponse();

                    Log.e("xuke","getMch_id = "+response1.getMch_id());
                    Log.e("xuke","getSub_mch_id = "+response1.getSub_mch_id());

                }
            }

            @Override
            public void onFailure(Call<StoreIdEntity> call, Throwable t) {

            }
        });*/
       /* ClearCarEntityCall  = RetrofitHelper.getInstance().ClearCar("S2101","111111");

        ClearCarEntityCall.enqueue(new Callback<ClearCarEntity>() {
            @Override
            public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {
                if (response != null) {

                    ClearCarEntity body = response.body();

                    int nCode= body.getReturnX().getNCode();

                    Log.e("zhoupan","nCode = "+nCode);

                    ClearCarEntity.ResponseBean response1 = body.getResponse();

                    Log.e("zhoupan","getSub_mch_id = "+response1.getServer_timestamp());
                }
            }

            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {

            }
        });*/

        PurchaseBagCall= RetrofitHelper.getInstance().getBagInfo("526374","S101");

        PurchaseBagCall.enqueue(new Callback<PurchaseBag>() {
            @Override
            public void onResponse(Call<PurchaseBag> call, Response<PurchaseBag> response) {

                PurchaseBag body = response.body();

                int nCode= body.getReturnX().getNCode();

                PurchaseBag.ResponseBean  res=body.getResponse();
                List<PurchaseBag.ResponseBean.BagMapBean>  maps= res.getBagMap();
                

            }

            @Override
            public void onFailure(Call<PurchaseBag> call, Throwable t) {

            }
        });
    }
}
