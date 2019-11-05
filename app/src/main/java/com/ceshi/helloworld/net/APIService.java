package com.ceshi.helloworld.net;

import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    /**
     * 任务详情
     */
    @FormUrlEncoded
    @POST("renwu/taskinfo")
    Call<TaskDetailEntity> getTaskDetail(
            @Field("u_id") String u_id,
            @Field("t_id") String t_id
    );


    /**
     * 门店信息登陆或注册
     */
    @FormUrlEncoded
    @POST("rest/v3/CheckService/getdeviceinfobyselfhelpdeviceid")
    Call<getdeviceinfoEntity> getdeviceinfobyselfhelpdeviceid(
            @Field("inputId") String inputId,
            @Field("deviceId") String deviceId
    );


    /**
     *注解 FormUrlEncoded表示 application/x-www-form-urlencoded 以key-value请求
     * POST  表示post请求
     *
     * storeId就是请求的参数  刚才写错了
     */
    @FormUrlEncoded
    @POST("base/rest/v3/CartService/getMchIdByStoreId")
    Call<StoreIdEntity> getStoreId(@Field("storeId")  String getStoreId);



    /**
     *注解 FormUrlEncoded表示 application/x-www-form-urlencoded 以key-value请求
     * POST  表示post请求
     *
     * storeId就是请求的参数  刚才写错了
     */
    @FormUrlEncoded
    @POST("base/rest/v3/cartService/cleanCartOfDevice")
    Call<ClearCarEntity> ClearCar(
            @Field("storeId")  String storeId,
            @Field("deviceId")  String deviceId);


    @FormUrlEncoded
    @POST("base/rest/v3/cartService/cleanCartOfDevice")
    Call<PurchaseBag> getBagInfo(
            @Field("userId")  String storeId,
            @Field("storeId")  String deviceId);
}