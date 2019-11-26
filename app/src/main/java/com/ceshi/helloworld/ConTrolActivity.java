package com.ceshi.helloworld;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;    //引用的类不能搞错，注意的点
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.log.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

public class ConTrolActivity extends AppCompatActivity {

    private boolean isPrintTextCut = false;
    public  MediaPlayer player1=new MediaPlayer();        //初始化播放音乐对象

    private final static int NO_CONNECT = 10;
    private final static int GETPRINTERVERSION = 101;

    private boolean isCheckConnect=true;
    private Camera ncamera;                               //定义相机对象
    private boolean isPreview = false;                   //定义非预览状态

    private static Runnable runnable;
    public PrinterAPI mPrinter = PrinterAPI.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);  //设置页面


        //调用打印的SDK
        Utils.init(this);
        PrinterAPI.getInstance().setOutput(true);//打印日志文件

        if (!isConnect()) return;
        //progressRun();，设置进度条的
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //mPrinter.setAlignMode(1);
                    mPrinter.setFontStyle(0);
                    if (PrinterAPI.SUCCESS == mPrinter.printString(
                            "哈喽，大家好", "GBK", true)) {
                        if (isPrintTextCut) {
                            try {
                                Thread.sleep(100);
                                mPrinter.cutPaper(66, 0);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();



    }


    public boolean isConnect() {
        if (!isCheckConnect)return true;
        boolean isConnect = true;
        if (!mPrinter.isConnect()) {
            handler.sendEmptyMessage(NO_CONNECT);
            isConnect = false;
        }
        return isConnect;
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new NoLeakHandler(ConTrolActivity.this) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PrinterAPI.ERR_PARAM:

                    Toast.makeText(ConTrolActivity.this, "参数错误",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PrinterAPI.NOSUPPROT:

                    Toast.makeText(ConTrolActivity.this, R.string.no_support,
                            Toast.LENGTH_SHORT).show();
                    break;
                case PrinterAPI.FAIL:

                    break;
                case PrinterAPI.SUCCESS:

                    Toast.makeText(ConTrolActivity.this, R.string.func_success,
                            Toast.LENGTH_SHORT).show();
                    break;
                case PrinterAPI.STRINGLONG:

                    Toast.makeText(ConTrolActivity.this, R.string.string_to_long,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    Toast.makeText(ConTrolActivity.this, R.string.func_failed,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ConTrolActivity.this, "socket connect fail",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 0x100:

                    break;
                case NO_CONNECT:

                    Toast.makeText(ConTrolActivity.this, "当前没有连接",
                            Toast.LENGTH_SHORT).show();

                    break;
                case GETPRINTERVERSION:
                    Toast.makeText(ConTrolActivity.this, "当前没有连接",
                            Toast.LENGTH_SHORT).show();
                    break;

            }


        }
    };


    private static class NoLeakHandler extends Handler {
        WeakReference<ConTrolActivity> wf = null;

        private NoLeakHandler(ConTrolActivity activity) {
            wf = new WeakReference<ConTrolActivity>(activity);
        }
    }

    private void printText() {
        findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isConnect()) return;
                //progressRun();，设置进度条的
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //mPrinter.setAlignMode(1);
                            mPrinter.setFontStyle(0);
                            if (PrinterAPI.SUCCESS == mPrinter.printString(
                                    "哈喽，大家好", "GBK", true)) {
                                if (isPrintTextCut) {
                                    try {
                                        Thread.sleep(100);
                                        mPrinter.cutPaper(66, 0);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                handler.sendEmptyMessage(0);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
    }


    }

