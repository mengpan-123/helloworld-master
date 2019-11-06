package com.ceshi.helloworld.net;

import com.ceshi.helloworld.bean.TaskDetailEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllInterFace {


    //自助收银机登陆/注册
    public  static  String  getdeviceinfobyselfhelpdeviceid(String inputstoreid,String deviceid){

        /*String result="";
        String  Url="http://dvp.rongxwy.com/base/rest/v3/CheckService/getdeviceinfobyselfhelpdeviceid";
        HttpSendRequest  httpsend=new   HttpSendRequest(Url,
                "POST",
                "inputId="+inputstoreid+"&deviceId="+deviceid+"");

        result= httpsend.NewSendRequest();

        return result;*/

        String result="";
        String  Url="http://dvp.rongxwy.com/base/rest/v3/CheckService/getdeviceinfobyselfhelpdeviceid";

        Call<TaskDetailEntity> taskDetail= RetrofitHelper.getInstance().getTaskDetail(inputstoreid, deviceid);
        taskDetail.enqueue(new Callback<TaskDetailEntity>() {
            @Override
            public void onResponse(Call<TaskDetailEntity> call, Response<TaskDetailEntity> response) {
                //网络请求成功
                if (response.isSuccessful()) {
                    TaskDetailEntity body = response.body();
                    if (body != null) {
                        //就拿到后台返回的数据了, tasktitle  设置给我们自己的页面
                        String Info= body.getTasktitle();

                    }
                }
            }

            @Override
            public void onFailure(Call<TaskDetailEntity> call, Throwable t) {

            }
        });

        return result;

    }

    //获取门店的商户号
    public  static  String  getMchIdByStoreId(String storeid){

        String result="";
        String url="http://dvp.rongxwy.com/base/rest/v3/CartService/getMchIdByStoreId";
        HttpSendRequest  httpsend=new   HttpSendRequest(url,
                "POST",
                "storeId="+storeid+"");

        result= httpsend.SendRequest();

        return result;
    }

}
