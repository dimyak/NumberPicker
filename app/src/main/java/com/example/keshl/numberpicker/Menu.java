package com.example.keshl.numberpicker;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity implements View.OnClickListener{
    int matrixSize = 5;
    TextView tvMatrixSize;
    Button btnMinus, btnPlus, btnPlay, btnInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        tvMatrixSize = findViewById(R.id.tvMatrixSize);
        tvMatrixSize.setText(Integer.toString(matrixSize));

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnPlay = findViewById(R.id.btnPlay);

        btnInfo = findViewById(R.id.btnInfo);

        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnMinus:
                if (matrixSize > 5)
                    --matrixSize;
                break;
            case R.id.btnPlus:
                if (matrixSize < 9)
                    ++matrixSize;
                break;
            case R.id.btnPlay:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("MATRIX_SIZE",matrixSize);
                // intent.putExtra("MATRIX_SIZE",2);
                startActivity(intent);
                break;
            case R.id.btnInfo:
                Intent intentt = new Intent(this,InfoActivity.class);
                startActivity(intentt);
                break;
        }

        tvMatrixSize.setText(Integer.toString(matrixSize));
    }
}
