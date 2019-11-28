package com.ceshi.helloworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ceshi.helloworld.bean.ClearCarEntity;
import com.ceshi.helloworld.bean.GetHyInfoEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.HyMessage;
import com.ceshi.helloworld.net.MyDatabaseHelper;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewIndexActivity extends Activity {


    View layout=null;
    private  VideoView  video;

    private  Call<ClearCarEntity>  ClearCarEntityCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newindex);


        video = (VideoView) findViewById(R.id.video);

        String path = Environment.getExternalStorageDirectory().getPath()+"/"+"index.mp4";//获取视频路径

        Uri uri = Uri.parse("http://www.ikengee.com.cn/test1/index.mp4");//将路径转换成uri
        video.setVideoURI(uri);//为视频播放器设置视频路径
        video.setMediaController(new MediaController(NewIndexActivity.this));//显示控制栏


        video.start();


        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

        //设置底部的显示信息
        TextView storename=findViewById(R.id.storename);
        storename.setText("门店编号:"+ CommonData.khid);

        TextView appmachine=findViewById(R.id.appmachine);
        appmachine.setText("设备编号:"+ CommonData.machine_number);


        TextView appversion=findViewById(R.id.appversion);
        appversion.setText("Version : "+ CommonData.app_version);


        //从数据库取数据，如果万一数据有是从1 开始的话，说明可能 程序中断了，机器重启了，从数据库查找本地保存的已知 的流水号
        if (CommonData.number==1) {
            try {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BaseInfo2.db", null, 1);
                SQLiteDatabase querydb = dbHelper.getWritableDatabase();

                //获取今天
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(date);

                Cursor cursor = querydb.query(CommonData.tablename, null,null,null, null, null, null);

                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        String  get_days = cursor.getString(cursor.getColumnIndex("date_lr"));
                        if (get_days.equals(today)){
                            //如果是同一天
                            CommonData.number=cursor.getInt(cursor.getColumnIndex("number"));
                        }
                        else
                        {
                            CommonData.number=1;
                        }
                    }
                }


            }
            catch (Exception ex) {
                //如果创建异常,不能影响其他进度

            }
        }




        /*需要在这里，第一。每次进入时，清空会员信息，清空订单信息,调用接口清空购物车*/
        ClearCarEntityCall=RetrofitHelper.getInstance().ClearCar(CommonData.khid,CommonData.userId);
        ClearCarEntityCall.enqueue(new Callback<ClearCarEntity>() {
            @Override
            public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {
                if (response!=null){
                    ClearCarEntity body = response.body();
                    if (null!=body) {
                        if (body.getReturnX().getNCode() == 0) {
                            //购物车清空成功。，清空单据
                            CommonData.hyMessage = null;
                            CommonData.orderInfo = null;
                        }
                    }
                    else{
                        ToastUtil.showToast(NewIndexActivity.this, "异常通知", "接口请求异常，请耐心等待回复");
                    }
                }
            }

            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {
                ToastUtil.showToast(NewIndexActivity.this, "异常通知", "请检查网络配置");
            }
        });


        //避免万一断网情况下，数据未正常清空。清空失败 这种情况呢？
        CommonData.hyMessage=null;
        CommonData.orderInfo=null;


        CommonData.player.reset();
        CommonData.player= MediaPlayer.create(this,R.raw.main);
        CommonData.player.start();
        CommonData.player.setLooping(false);



        //绑定 开始购物
        Button button_shape=findViewById(R.id.shopping);
        button_shape.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                //Toast.makeText(IndexActivity.this,"正在跳转，请等待",Toast.LENGTH_SHORT).show();
                //跳转到商品录入界面
                Intent intent = new Intent(NewIndexActivity.this, CarItemsActicity.class);
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
        setDialogSize(layout);
        dialog.show();
        EditText hyedit=(EditText)layout.findViewById(R.id.phoneorhyNum);
        hyedit.setInputType(InputType.TYPE_NULL);

        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText hyedit=(EditText)layout.findViewById(R.id.phoneorhyNum);

                String hynum=hyedit.getText().toString();

                String sCorpId=CommonData.corpId;

                String sUserId="";

                if (!TextUtils.isEmpty(hynum)){

                    Call<GetHyInfoEntity> hyInfoEntityCall= RetrofitHelper.getInstance().getHyInfoEntityCall(hynum,sCorpId,CommonData.lCorpId,sUserId);
                    hyInfoEntityCall.enqueue(new Callback<GetHyInfoEntity>() {
                        @Override
                        public void onResponse(Call<GetHyInfoEntity> call, Response<GetHyInfoEntity> response) {
                            if (null!=response && response.isSuccessful()) {
                                GetHyInfoEntity body = response.body();
                                if ( null!=body) {
                                    if (body.getReturnX().getNCode()==0){
                                        GetHyInfoEntity.ResponseBean response1 = body.getResponse();
                                        HyMessage hyinfo=new  HyMessage();
                                        hyinfo.cardnumber=response1.getCdoData().getSMemberId();
                                        hyinfo.hySname=response1.getCdoData().getStrName();
                                        hyinfo.hytelphone=response1.getCdoData().getSBindMobile();

                                        //给会员信息赋值
                                        CommonData.hyMessage=hyinfo;

                                        //然后跳转到 购物界面
                                        Intent intent = new Intent(NewIndexActivity.this,   CarItemsActicity.class);
                                        startActivity(intent);

                                    }else
                                    {
                                        ToastUtil.showToast(NewIndexActivity.this, "登录提示", body.getReturnX().getStrText());
                                        return;
                                    }
                                }
                                else
                                {
                                    ToastUtil.showToast(NewIndexActivity.this, "登录提示", "接口访问失败，请重试");
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<GetHyInfoEntity> call, Throwable t) {
                            ToastUtil.showToast(NewIndexActivity.this, "登录提示", "请求失败，请检查网络连接");
                            return;
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
    public  void  hynumClick(View view){
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
     * 动态控制弹出框的大小
     */
    private void setDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                mView.setLayoutParams(new FrameLayout.LayoutParams(780,
                        1100));
            }
        });
    }

    /**
     *
     * 删除按钮事件
     *
     * */
    public  void  hydeleteClick(View view){

        EditText editText=layout.findViewById(R.id.phoneorhyNum);
        if (null!=editText.getText().toString()&&editText.getText().toString().length()>0){
            String old_text=editText.getText().toString();
            editText.setText(old_text.substring(0,old_text.length()-1));
            editText.setSelection(editText.getText().toString().length());
        }
    }

}
