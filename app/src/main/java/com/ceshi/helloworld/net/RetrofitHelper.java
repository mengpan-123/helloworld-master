package com.ceshi.helloworld.net;


import com.alibaba.fastjson.JSON;
import com.ceshi.helloworld.bean.Addgoods;
import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.ResponseDeleteGoods;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.RequestSignBean;
import com.ceshi.helloworld.bean.RequestDeleteGoods;
import com.ceshi.helloworld.bean.ResponseSignBean;
import com.ceshi.helloworld.bean.ReturnXMLParser;
import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.bean.UpdateVersionEntity;
import com.ceshi.helloworld.bean.XMLParseEntity;
import com.ceshi.helloworld.bean.createPrepayIdEntity;
import com.ceshi.helloworld.bean.getCartItemsEntity;
import com.ceshi.helloworld.bean.getWXFacepayAuthInfo;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;
import com.ceshi.helloworld.bean.PaysuccessofdeviceEntity;
import com.ceshi.helloworld.bean.upCardCacheEntity;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhoupan on 2019/11/04.
 * <p
 */

public class RetrofitHelper {

    private static final long DEFAULT_TIMEOUT = 30;
    private static RetrofitHelper mInstance;
    private APIService mAPIService;


    private RetrofitHelper(String Url) {
        initRetrofit(Url);
    }


    private RetrofitHelper() {
        initRetrofit();
    }

    public static RetrofitHelper getInstance(String Url) {
        if (mInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitHelper(Url);
                }
            }
        }
        return mInstance;
    }

    public static RetrofitHelper getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitHelper();
                }
            }
        }
        return mInstance;
    }
    /**
     * 这里的url必须时域名前的那段baseurl  比如
     * http://dvp.rongxwy.com/base/rest/v3/CartService/getMchIdByStoreId?storeId=S2101
     * 取http://dvp.rongxwy.com/为他的baseurl
     */

    private void initRetrofit(String Url) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getBuilder().build())
                .build();
        mAPIService = retrofit.create(APIService.class);
    }

    //没有参数的时候默认使用此地址，重写一个有参数的
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(CommonData.CommonUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getBuilder().build())
                .build();
        mAPIService = retrofit.create(APIService.class);
    }

    private OkHttpClient.Builder getBuilder() {

        Interceptor controlInterceptor = chain -> {
            Request request = chain.request();
            RequestBody oldBody = request.body();
            Buffer buffer = new Buffer();
            assert oldBody != null;
            oldBody.writeTo(buffer);
            String data = buffer.readUtf8();
            return chain.proceed(request);
        };


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(controlInterceptor)
                .retryOnConnectionFailure(true);
        return builder;
    }


    /**
     * 获取任务详情
     *
     * @param u_id 用户id
     * @param t_id 任务id
     */
    public Call<TaskDetailEntity> getTaskDetail(String u_id, String t_id) {
        return mAPIService.getTaskDetail(u_id, t_id);
    }


    /**
     * 输入门店号和设备号之后 进行登陆/注册
     *
     * @param inputstoreid 用户id
     * @param deviceid 任务id
     */
    public Call<getdeviceinfoEntity> getdeviceinfobyselfhelpdeviceid(String inputstoreid, String deviceid) {
        return mAPIService.getdeviceinfobyselfhelpdeviceid(inputstoreid, inputstoreid);
    }

    /**
     * StoreIdEntity就是返回的json，框架会帮你自动解析google.code.gson 就这个玩意
     * storeId就是你要传的参数
     */
    public Call<StoreIdEntity>  getStoreId(String storeId){
        return mAPIService.getStoreId(storeId);
    }

    /**
     * StoreIdEntity就是返回的json，框架会帮你自动解析google.code.gson 就这个玩意
     * storeId就是你要传的参数
     */
    public Call<ClearCarEntity>  ClearCar(String storeId,String deviceid){
        return mAPIService.ClearCar(storeId,deviceid);
    }


    /**
     * StoreIdEntity就是返回的json，框架会帮你自动解析google.code.gson 就这个玩意
     * streId就是你要传的参数
     */
    public Call<PurchaseBag>  getBagInfo(String userId, String storeId){
        return mAPIService.getBagInfo(userId,storeId);
    }


    /***
     * GetHyInfoEntity是返回的结果
     *sBindMobile 手机号
     * sCorpId    商户号
     * lCorpId    商户编号
     * sUserId    用户编号
     * */

    public Call<GetHyInfoEntity> getHyInfoEntityCall(String sBindMobile,String sCorpId,String lCorpId,String sUserId){
        lCorpId=CommonData.lCorpId;
        return mAPIService.getHyInfoEntityCall(sBindMobile,sCorpId,CommonData.lCorpId,sUserId);
    }


    /***
     * getgoodsinfo 是返回的结果
     * @param barcode  商品条形码
     * @param storeId   门店编号
     * @param userId    设备编号
     * @param goodsPrice    商品价格
     * @return
     */
    public  Call<Addgoods> getgoodsinfo(String barcode, String storeId, String userId, String goodsPrice){
        return  mAPIService.getgoodsinfo(barcode, storeId, userId,  goodsPrice);
    }

    /***
     * 调用接口告诉对方程序升级了
     *sBindMobile 手机号
     * sCorpId    商户号
     * lCorpId    商户编号n
     * sUserId    用户编号
     * */
    public Call<UpdateVersionEntity> UpdateVersion(String appName, String appId, String storeId){
        return mAPIService.UpdateVersion(appName,appId,storeId);
    }

    /***
     * 预支付订单流水号
     * storeId    门店编号
     * */
    public Call<createPrepayIdEntity> createPrepayId(String storeId){
        return mAPIService.createPrepayId(storeId);
    }


    /***
     * 会员卡预支付设置
     *sMemberId 会员编号
     * prepayId    预支付流水号

     * */
    public Call<upCardCacheEntity> upCardCache(String sMemberId,String prepayId){
        return mAPIService.upCardCache(sMemberId,prepayId);
    }


    /***
     * 获取支付结果
     * */
    public Call<PaysuccessofdeviceEntity> paysuccessofdevice(String storeId, String prepayId){
        return mAPIService.paysuccessofdevice(storeId,prepayId);
    }


    /***
     * 订单支付接口
     *请求方式 POST，数据格式application/json
     * */
    public Call<ResponseSignBean> getSign(String payWay,
                                          String AuthCode,
                                          String sPayTypeExt,
                                          String  openid,
                                          List<RequestSignBean.PluMapBean> pluMap,
                                          List<RequestSignBean.PayMapBean> payMap){

        RequestSignBean requestSignBean = new RequestSignBean();
        requestSignBean.setStoreId(CommonData.khid);

        requestSignBean.setDeviceId(CommonData.userId);
        requestSignBean.setUserId(CommonData.userId);  //登陆/注册接口返回的 userId

        requestSignBean.setPayWay(payWay);
        if (payWay.equals("WXFacePay")){
            requestSignBean.setFaceCode(AuthCode);
            requestSignBean.setAuthCode("");
        }
        else
        {
            requestSignBean.setAuthCode(AuthCode);
        }

        requestSignBean.setSPayTypeExt(sPayTypeExt);//新增加的，支付来源

        requestSignBean.setOpenid(openid);//用户的 微信用户号

        requestSignBean.setBizType(1);



        String Pays="{'prepayId':'"+CommonData.orderInfo.prepayId+"','mydata':'testinfo'}";




        requestSignBean.setExtra(Pays);
        requestSignBean.setPluMap(pluMap);
        requestSignBean.setPayMap(payMap);



        //String s = new Gson().toJson(requestSignBean); //将bean转为json

        String s= JSON.toJSONString(requestSignBean);



        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.getSgin(requestBody);

    }

    public Call<ResponseDeleteGoods> responseDeleteGoods(List<RequestDeleteGoods.ItemMapBean> itemMap ){
        RequestDeleteGoods requestDeleteGoods=new RequestDeleteGoods();
        requestDeleteGoods.setStoreId(CommonData.khid);
        requestDeleteGoods.setUserId(CommonData.userId);
        requestDeleteGoods.setItemMap(itemMap);
        String s=new Gson().toJson(requestDeleteGoods);
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return  mAPIService.deleteGoods(requestBody);
    }

    /***
     * 获取 购物车列表
     * */
    public Call<getCartItemsEntity> getCartItems(String userId, String storeId){
        return mAPIService.getCartItems(userId,storeId,"1");
    }



    /***
     * 订单支付接口
     *请求方式 POST，数据格式XML
     * */
    public Call<String> PostWxPay(String  XML){

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), XML);
        return mAPIService.PostWxPay(requestBody);

    }



    //获取微信刷脸SDK调用凭证getWXFacepayAuthInfo
    public Call<getWXFacepayAuthInfo> getWXFacepayAuthInfo(String storeId, String deviceId, String rawdata){

        return mAPIService.getWXFacepayAuthInfo(storeId,deviceId,rawdata);

    }

}
