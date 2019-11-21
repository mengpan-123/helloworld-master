package com.ceshi.helloworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;    //引用的类不能搞错，注意的点
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConTrolActivity extends AppCompatActivity {


    public  MediaPlayer player1=new MediaPlayer();        //初始化播放音乐对象


    private Camera ncamera;                               //定义相机对象
    private boolean isPreview = false;                   //定义非预览状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);  //设置页面


        Button tvOne = findViewById(R.id.music);

        player1.reset();
        player1=MediaPlayer.create(this,R.raw.paysuccess);
        player1.start();
        player1.setLooping(false);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //判断手机是否安装SD卡
        if (!Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "请安装SD卡！", Toast.LENGTH_SHORT).show();
        }

        //获取SurfaceView组件，用于显示摄像头预览
        SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder sh = sv.getHolder();
        //获取SurfaceHolder对象
        // 设置该SurfaceHolder自己不维护缓冲
        //sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Button preview = (Button) findViewById(R.id.preview);      //获取"预览"按钮

        Button takePicture = (Button) findViewById(R.id.camera);//获取"拍照"按钮


        Button closemusic = findViewById(R.id.closemusic);

        Button callphone = findViewById(R.id.callphone);





        //设置点击事件，开始音乐播放
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {


                /*MediaPlayer player1 = new MediaPlayer();
                try {
                    String musicPath="D:\\android demo\\helloworld\\helloworld-master\\app\\src\\main\\assets\\wxpay.wav";

                    File file = new File(musicPath);
                    if(file.exists()) {
                        player1.setDataSource(musicPath);
                        player1.prepare();
                    }
                   else{
                        tvOne.setText("音乐文件不存在！");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/

                player1.start();
                player1.setLooping(true);
            }
        });


        closemusic.setOnClickListener(new  View.OnClickListener(){

            @Override
            public  void  onClick(View view) {

                player1.stop();
            }

        });


        //预览
        preview.setOnClickListener(new  View.OnClickListener(){

            @Override
            public  void  onClick(View view) {

                if (!isPreview){
                    ncamera = Camera.open();
                    isPreview = true;  //设置为预览状态
                }
                try {
                    ncamera.setPreviewDisplay(sh);
                    //设置用于显示预览的SurfaceView
                    Camera.Parameters parameters = ncamera.getParameters();  //获取相机参数
                    parameters.setPictureFormat(PixelFormat.JPEG);    //指定图片为JPEG图片

                    parameters.set("jpeg-quality", 80);   //设置图片的质量
                    ncamera.setParameters(parameters);    //重新设置相机参数

                    ncamera.startPreview(); //开始预览
                    ncamera.autoFocus(null);              //设置自动对焦

                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }

        });




        Camera.PictureCallback jpeg = new Camera.PictureCallback()  {
             @Override
             public void onPictureTaken(byte[] data, Camera camera) {

                 //根据拍照所得的数据创建位图
                 Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

                 ncamera.stopPreview();
                 isPreview=false;
                 //获取sd卡根目录 11
                 File appDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/");
                 if (!appDir.exists()) {
                     //如果该目录不存在,创建该目录
                      appDir.mkdir();
                 }

                 //设置照片名称
                 String ﬁleName=System.currentTimeMillis() + ".jpg";
                 File ﬁle = new File(appDir, ﬁleName);    //创建文件对象

                 try{
                     FileOutputStream fos = new FileOutputStream(ﬁle);//创建一个文件输出流对象
                     //将图片内容压缩为JPEG格式输出到输出流对象中
                     bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                     //将缓冲区中的数据全部写出到输出流中
                     fos.flush();
                     fos.close();

                 }
                 catch (FileNotFoundException ex){
                     ex.printStackTrace();
                 }
                 catch (IOException e)
                 {
                     e.printStackTrace();
                 }

                 //将照片插入到系统图库
                 try {
                     MediaStore.Images.Media.insertImage(ConTrolActivity.this.getContentResolver(),
                             ﬁle.getAbsolutePath(), ﬁleName, null);
                 }
                 catch (FileNotFoundException e)
                 {
                     e.printStackTrace();
                 }
                 //最后通知图库更新
                 Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                 Uri uri = Uri.fromFile(ﬁle);
                 intent.setData(uri);
                 ConTrolActivity.this.sendBroadcast(intent);                 //这个广播的目的就是更新图库

                 Toast.makeText(ConTrolActivity.this, "照片保存至：" + ﬁle, Toast.LENGTH_LONG).show();
                 resetCamera();                             //调用重新预览resetCamera()

             }
         };


        //打电话事件
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();            //创建Intent对象

                 switch (v.getId()) {         //根据ImageButton组件的id进行判断
                      case R.id.callphone:                      //如果是电话图片按钮

                          EditText editText=(EditText)findViewById(R.id.inputphone);

                        if (editText.getText().toString()!="") {

                            Toast.makeText(ConTrolActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //以下属于隐士启动 intent
                         intent.setAction(intent.ACTION_DIAL);   //调用拨号面板
                         intent.setData(Uri.parse("tel:"+editText.getText().toString()+"")); //设置要拨打的号码
                          startActivity(intent);     //启动Activity
                          break;
                                               //启动Activity
                 }
            }
        };



            //开始拍照 (但是用户都需要手动在手机里面 打开相关权限)
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ncamera != null) {                      //相机不为空
                    ncamera.takePicture(null, null, jpeg);  //进行拍照
                }
            }
        });


        callphone.setOnClickListener(listener);
    }

    private   void resetCamera(){
        if (!isPreview){
            ncamera.startPreview();
            isPreview=true;
        }
    }


    }

