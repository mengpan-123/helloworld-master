package com.ceshi.helloworld.net;

import com.ceshi.helloworld.bean.Addgoods;
import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.ResponseSignBean;
import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.bean.UpdateVersionEntity;
import com.ceshi.helloworld.bean.createPrepayIdEntity;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;
import com.ceshi.helloworld.bean.getpaysignnewEntity;
import com.ceshi.helloworld.bean.upCardCacheEntity;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    @POST("base/rest/v3/CheckService/getdeviceinfobyselfhelpdeviceid")
    Call<getdeviceinfoEntity> getdeviceinfobyselfhelpdeviceid(
            @Field("inputId") String inputId,
            @Field("deviceId") String deviceId
    );


    /**
     * 注解 FormUrlEncoded表示 application/x-www-form-urlencoded 以key-value请求
     * POST  表示post请求
     * <p>
     * storeId就是请求的参数  刚才写错了
     */
    @FormUrlEncoded
    @POST("base/rest/v3/CartService/getMchIdByStoreId")
    Call<StoreIdEntity> getStoreId(@Field("storeId") String getStoreId);


    /**
     * 注解 FormUrlEncoded表示 application/x-www-form-urlencoded 以key-value请求
     * POST  表示post请求
     * <p>
     * storeId就是请求的参数  刚才写错了
     */
    @FormUrlEncoded
    @POST("base/rest/v3/cartService/cleanCartOfDevice")
    Call<ClearCarEntity> ClearCar(
            @Field("storeId") String storeId,
            @Field("deviceId") String deviceId);


    /**
     * 获取购物袋信息
     * @param storeId
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("base/rest/v3/goodsService/getBagInfo")
    Call<PurchaseBag> getBagInfo(
            @Field("userId")  String storeId,
            @Field("storeId")  String deviceId);


    /**
     * 获取会员登录信息
     */
    @FormUrlEncoded
    @POST("base/rest/v3/MemberService/testingMemberCard")
    Call<GetHyInfoEntity> getHyInfoEntityCall(
            //String sBindMobile,String sCorpId,CommonData.lCorpId,String sUserId
            @Field("sBindMobile") String sBindMobile,
            @Field("sCorpId") String sCorpId,
            @Field("lCorpId") String lCorpId,
            @Field("sUserId") String sUserId
    );


    /**
     * 获取录入的产品信息
     */
    @FormUrlEncoded
    @POST("base/rest/v3/GoodsService/getgoodsinfo")
    Call<Addgoods> getgoodsinfo(
            @Field("barcode") String barcode,
            @Field("storeId") String storeId,
            @Field("userId") String userId,
            @Field("goodsPrice") String goodsPrice
    );


    /**
     * 更新版本
     */
    @FormUrlEncoded
    @POST("base/rest/v3/SystemService/updateversion")
    Call<UpdateVersionEntity> UpdateVersion(
            @Field("appName") String appName,
            @Field("appId") String appId,
            @Field("storeId") String storeId
    );

    /**
     * 订单流水号
     */
    @FormUrlEncoded
    @POST("base/rest/v3.1.0/cartService/createPrepayId")
    Call<createPrepayIdEntity> createPrepayId(
            @Field("storeId") String storeId
    );

    /**
     * 更新版本
     */
    @FormUrlEncoded
    @POST("base/rest/v3/MemberService/upCardCache")
    Call<upCardCacheEntity> upCardCache(
            @Field("sMemberId") String sMemberId,
            @Field("prepayId") String prepayId
    );


    /**
     * 下单支付接口
     */
    @FormUrlEncoded
    @POST("base/rest/v3/CartService/getpaysignnew")
    Call<getpaysignnewEntity> getpaysignnew(
            @Field("sMemberId") String sMemberId,
            @Field("prepayId") String prepayId
    );

    @POST("base/rest/v3/CartService/getpaysignnew")
    Call<ResponseSignBean> getSgin(@Body RequestBody requestBody);
}