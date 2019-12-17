package com.ceshi.helloworld.net;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ceshi.helloworld.CarItemsActicity;
import com.ceshi.helloworld.R;

public class SettingsActivity extends PopupWindow {


    private View mMenuView;
    public SettingsActivity(final Activity context, View.OnClickListener itemsOnClick) {
         super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_settings, null);


        TextView text1=  mMenuView.findViewById(R.id.txt1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context, "商品查询通知", "11111");
                return;
            }
        });

    }


}
