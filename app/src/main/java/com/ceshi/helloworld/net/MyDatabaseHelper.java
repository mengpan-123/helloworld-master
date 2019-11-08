package com.ceshi.helloworld.net;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {



    private Context mContext;

    //声明一个建表的语句，用于存储这个门店 和机器编号,和录入日期，app版本，微信支付商户号,门店商户id-》lCorpId,个人id-》userId
    public static final String CREATE_SQL = "create table  "+CommonData.tablename+" ("

            + "khid text  primary key, "

            + "lCorpId  text , "

            + "khsname  text , "

            + "machine_number text, "

            + "app_version  text, "

            + "userId  text, "

            + "date_lr date, "

            + "sub_mch_id date, "

            + "mch_id text )";


    //构造函数
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);

        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SQL);

        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();

    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
