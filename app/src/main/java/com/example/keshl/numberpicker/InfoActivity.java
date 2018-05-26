package com.example.keshl.numberpicker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;



/**
 * Created by keshl on 21.05.2018.
 */

public class InfoActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView tx = new TextView(this);

        tx.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        String str = getResources().getString(R.string.info);
        //  String str = "STRING";
        tx.setText(str);
        tx.setTextColor(getResources().getColor(R.color.bgGrey));
        tx.setPadding(48,48,48,48);
        tx.setTextSize(18);
        tx.setGravity(100);
        setContentView(tx);


    }
}
