package com.ceshi.helloworld.net;


import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.PurchaseBag;
import com.ceshi.helloworld.bean.StoreIdEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.bean.getdeviceinfoEntity;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuke on 2017/10/27.
 * <p
 */

public class RetrofitHelper {

    private static final long DEFAULT_TIMEOUT = 30;
    private static RetrofitHelper mInstance;
    private APIService mAPIService;


    private RetrofitHelper() {
        initRetrofit();
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

     * 多传一个参数，url作为动态的传入
     */


    /**
     * 这里的url必须时域名前的那段baseurl  比如
     * http://dvp.rongxwy.com/base/rest/v3/CartService/getMchIdByStoreId?storeId=S2101
     * 取http://dvp.rongxwy.com/为他的baseurl,暂时给你写死，后面你去优化
     */

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

}
