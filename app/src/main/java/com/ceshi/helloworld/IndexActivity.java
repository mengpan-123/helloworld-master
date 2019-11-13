package com.ceshi.helloworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.bean.TaskDetailEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.HyMessage;
import com.ceshi.helloworld.net.OrderInfo;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.ToastUtil;

import java.util.HashMap;

public class IndexActivity extends Activity {
    String result=null;
    View layout=null;

    private  Call<ClearCarEntity>  ClearCarEntityCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        /*需要在这里，第一。每次进入时，清空会员信息，清空订单信息,调用接口清空购物车*/
        ClearCarEntityCall=RetrofitHelper.getInstance().ClearCar(CommonData.khid,CommonData.userId);
        ClearCarEntityCall.enqueue(new Callback<ClearCarEntity>() {
            @Override
            public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {
                if (response!=null){
                    ClearCarEntity body = response.body();

                    if (body.getReturnX().getNCode()==0){
                        //购物车清空成功。那如果万一没网 ，清空失败 这种情况呢？
                        CommonData.hyMessage=null;
                        CommonData.orderInfo=null;
                    }
                }
            }

            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {
                Toast.makeText(IndexActivity.this, "请检查网络配置...", Toast.LENGTH_LONG).show();
            }
        });






        //绑定 开始购物
        Button button_shape=findViewById(R.id.shopping);
        button_shape.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                //Toast.makeText(IndexActivity.this,"正在跳转，请等待",Toast.LENGTH_SHORT).show();
                //跳转到商品录入界面
                Intent intent = new Intent(IndexActivity.this, InputGoodsActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     *
     * MembersLogin 会员登录
     * */

    public  void  MembersLogin(View view){
        // 创建一个Dialog
        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);
        // 自定义对话框布局
        layout = View.inflate(this, R.layout.activity_memberlogin,
                null);
        dialog.setContentView(layout);
        //确定
        TextView title = (TextView) layout.findViewById(R.id.closeHy);

        //closemember 关闭页面
        TextView closemem=layout.findViewById(R.id.closemem);

        dialog.show();
        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText hyedit=(EditText)layout.findViewById(R.id.phoneorhyNum);

                String hynum=hyedit.getText().toString();

                String sCorpId=CommonData.corpId;
                sCorpId="C1501";
                String sUserId="";

                if (!TextUtils.isEmpty(hynum)){

                    Call<GetHyInfoEntity> hyInfoEntityCall= RetrofitHelper.getInstance().getHyInfoEntityCall(hynum,sCorpId,"16",sUserId);
                     hyInfoEntityCall.enqueue(new Callback<GetHyInfoEntity>() {
                         @Override
                         public void onResponse(Call<GetHyInfoEntity> call, Response<GetHyInfoEntity> response) {
                             if (response.isSuccessful()) {
                                 GetHyInfoEntity body = response.body();
                                 if (body != null) {
                                    if (body.getReturnX().getNCode()==0){
                                        GetHyInfoEntity.ResponseBean response1 = body.getResponse();
                                        HyMessage   hyinfo=new  HyMessage();
                                        hyinfo.cardnumber=response1.getCdoData().getSMemberId();
                                        hyinfo.hySname=response1.getCdoData().getStrName();
                                        hyinfo.hytelphone=response1.getCdoData().getSBindMobile();

                                        //给会员信息赋值
                                        CommonData.hyMessage=hyinfo;

                                        //然后跳转到 购物界面
                                        Intent intent = new Intent(IndexActivity.this, InputGoodsActivity  .class);
                                        startActivity(intent);

                                    }else {
                                        ToastUtil.showToast(IndexActivity.this,"登录提示","该会员不存在，请重新输入");
                                    return;
                                    }
                                 }
                             }
                         }
                         @Override
                         public void onFailure(Call<GetHyInfoEntity> call, Throwable t) {
                                try{
                                    Log.e("zhoupan","getSub_mch_id = "+ call.execute().message());
                                }catch (Exception e){

                                }
                         }
                     });
                }
                dialog.dismiss();

            }
        });
        //设置关闭按钮的时间
        closemem.setOnClickListener(new View.OnClickListener() {

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
        EditText editText1=layout.findViewById(R.id.phoneorhyNum);
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

        EditText editText=layout.findViewById(R.id.phoneorhyNum);
        if (null!=editText.getText().toString()&&editText.getText().toString().length()>0){
            String old_text=editText.getText().toString();
            editText.setText(old_text.substring(0,old_text.length()-1));
            editText.setSelection(editText.getText().toString().length());
        }
    }

    /**
     * 确认登录按钮
     */

    public  void Require_login(){



    }

    public  void gw_click(View view){

        ToastUtil.showToast(IndexActivity.this, "这是一个toast", "我就是那个toast");
    }
}
