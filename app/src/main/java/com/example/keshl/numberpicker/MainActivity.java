package com.example.keshl.numberpicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyButton.MyOnClickListener, ResultsCallback{

    private  int MATRIX_SIZE = 5;// можем ставить от 2 до 16))

    //ui
    private TextView mUpText, mLowText;
    GridLayout mGridLayout;
    private MyButton[][] mButtons;
    private Game game;
    String playerOne="Player 1", playerTwo="Player 2";

    boolean playIsOne=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        MATRIX_SIZE=intent.getIntExtra("MATRIX_SIZE",5);

        mGridLayout = (GridLayout) findViewById(R.id.my_grid);
        mGridLayout.setColumnCount(MATRIX_SIZE);
        mGridLayout.setRowCount(MATRIX_SIZE);
        mButtons = new MyButton[MATRIX_SIZE][MATRIX_SIZE];//5 строк и 5 рядов

        //создаем кнопки для цифр
        for (int yPos = 0; yPos < MATRIX_SIZE; yPos++) {
            for (int xPos = 0; xPos < MATRIX_SIZE; xPos++) {
                MyButton mBut = new MyButton(this, xPos, yPos);

                mBut.setTextSize(30-MATRIX_SIZE);
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);//Возвращает один из объектов шрифта по умолчанию, основанный на указанном стиле
                mBut.setTypeface(boldTypeface);
                mBut.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                mBut.setOnClickListener(this);
                mBut.setPadding(1, 1, 1, 1); //так цифры будут адаптироваться под размер

                mBut.setAlpha(1);//установи прозрачность
                mBut.setClickable(false);

                mBut.setBackgroundResource(R.drawable.bg_grey);

                mButtons[yPos][xPos] = mBut;
                mGridLayout.addView(mBut);
            }
        }

        mUpText = (TextView) findViewById(R.id.upper_scoreboard);
        mLowText = (TextView) findViewById(R.id.lower_scoreboard);

        //расположим кнопки с цифрами равномерно внутри mGridLayout
        mGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setButtonsSize();
                        //нам больше не понадобится OnGlobalLayoutListener
                        mGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        game = new Game(this, MATRIX_SIZE); //создаем класс игры
        //game.startGame(); //и запускаем ее
    }

    private void setButtonsSize() {
        int pLength;
        final int MARGIN = 6;

        int pWidth = mGridLayout.getWidth();
        int pHeight = mGridLayout.getHeight();
        int numOfCol = MATRIX_SIZE;
        int numOfRow = MATRIX_SIZE;

        //сделаем mGridLayout квадратом
        if (pWidth >= pHeight) pLength = pHeight;
        else pLength = pWidth;
        ViewGroup.LayoutParams pParams = mGridLayout.getLayoutParams();
        pParams.width = pLength;
        pParams.height = pLength;
        mGridLayout.setLayoutParams(pParams);

        int w = pLength / numOfCol;
        int h = pLength / numOfRow;

        for (int yPos = 0; yPos < MATRIX_SIZE; yPos++) {
            for (int xPos = 0; xPos < MATRIX_SIZE; xPos++) {
                GridLayout.LayoutParams params = (GridLayout.LayoutParams)
                        mButtons[yPos][xPos].getLayoutParams();
                params.width = w - 2 * MARGIN;
                params.height = h - 2 * MARGIN;
                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                mButtons[yPos][xPos].setLayoutParams(params);
                //Log.w(TAG, "process goes in customizeMatrixSize");
            }
        }
    }

    //Метод для обработки клииков
    @Override
    public void OnTouchDigit(MyButton v) {

        game.onUserTouchDigit(v.getIdY(),v.getIdX(),playIsOne);


    }

    @Override
    public void changeLabel(boolean upLabel, int points) {
        String str = Integer.toString(points);
        if (upLabel) mUpText.setText(playerTwo+ ": "+ str);
        else mLowText.setText(playerOne+ ": "+ str);
    }

    @Override
    public void changePlayer(){
        playIsOne=!playIsOne;
    }

    //Убрать row
    @Override
    public void changeButtonBg(int y, int x, boolean row, boolean active) {

        if(active){
            if(playIsOne)
                mButtons[y][x].setBackgroundResource(R.drawable.bg_blue);
            else
                mButtons[y][x].setBackgroundResource(R.drawable.bg_red);
        }else{
            mButtons[y][x].setBackgroundResource(R.drawable.bg_grey);
        }
    }

    @Override
    public void setButtonText(int y, int x, int text) {
        mButtons[y][x].setText(String.valueOf(text));
    }

    @Override
    public void changeButtonClickable(int y, int x, boolean clickable) {
        mButtons[y][x].setClickable(clickable);
    }

    @Override
    public void onResult(int one, int two) {
        String winner;
        if (one > two) winner = "Winner: "+ playerOne;
        else if (one < two) winner = "Winner: "+ playerTwo;
        else winner = "Draw";
        //  Toast.makeText(this, winner, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(winner);
        builder.setMessage("Play again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recreate();
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(int y, int x, boolean flyDown) {
        final Button currentBut = mButtons[y][x];

        currentBut.setAlpha(0.7f);
        currentBut.setClickable(false);

        AnimationSet sets = new AnimationSet(false);
        int direction = flyDown ? 200 : -200;
        TranslateAnimation animTr = new TranslateAnimation(0, 0, 0, direction);
        animTr.setDuration(405);//установим длинтельность анимации перемещения
        AlphaAnimation animAl = new AlphaAnimation(0.2f, 0f);
        animAl.setDuration(405);
        sets.addAnimation(animTr);
        sets.addAnimation(animAl);
        currentBut.startAnimation(sets);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                currentBut.clearAnimation();
                currentBut.setAlpha(0);
            }
        }, 400);
    }
}
