package com.ceshi.helloworld;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InputPayCodeActivity  extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_paycode);
        editText=findViewById(R.id.pay_code);
    }

    String barcode ="";
    @Override
    public  boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction()== KeyEvent.ACTION_DOWN){
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
        }
        if (event.getAction()==KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", barcode);
            if (!TextUtils.isEmpty(barcode)){
                if (barcode.contains("\n")){
                    barcode=barcode.substring(0,barcode.length()-1);
                }
                editText.setText(barcode);
                barcode="";
            }

        }
        return true;

    }

}
