package com.example.keshl.numberpicker;

import android.util.Log;

import java.util.Random;

/**
 * Created by keshl on 22.05.2018.
 */

public class Game {

   
   // protected MyAnimation mAnimation; //класс AsyncTask для анимации
   // boolean chekEnd=true;//для проверки на конец
    //матрица цифр и матрица допустимых ходов
    protected int[][] mMatrix; //цифры для кнопок
    protected volatile boolean[][] mAllowedMoves;//возможен ли ход
    protected int mSize; //размер матрицы

    protected int playerOnePoints = 0, playerTwoPoints = 0;//очки игроков

    protected volatile boolean isRow; //мы играем за строку или за ряд
    protected volatile int currentActiveNumb; //нужно для определения последнего хода
    protected ResultsCallback mResults;//интерфейс, который будет реализовывать MainActivity

    Random rnd; // для заполнения матрицы цифрами и определения первой активной строки

    public Game(ResultsCallback results, int size){
        mResults = results; //передаем сущность интерфейса
        mSize = size;

        rnd = new Random();
        generateMatrix(); //заполняем матрицу случайнами цифрами

        //условный ход, нужен для определения активной строки
        currentActiveNumb = rnd.nextInt(mSize);
        //мы играем за строку или за ряд
        switch (rnd.nextInt(2)){
            case 0:
                isRow=true;
                break;
            case 1:
                isRow=false;
                break;
        }

        for (int yPos = 0; yPos < mSize; yPos++) {
            for (int xPos = 0; xPos < mSize; xPos++) {

                //записываем сгенерированные цифры на кнопки с помощью нашего интерфейса
                mResults.setButtonText(yPos, xPos, mMatrix[yPos][xPos]);
                // закрашиваем активную строку/ряд

            /*    if (isRow && yPos == currentActiveNumb) {
                    mResults.changeButtonBg(yPos, xPos, isRow, true);
                }else if(!isRow&&xPos==currentActiveNumb){
                    mResults.changeButtonBg(yPos, xPos, isRow, true);
                }
            */
            }
        }
        activateRawOrColumn(true);


    }

    //Обработка нажатия пользователя
    protected void onUserTouchDigit(int y, int x, boolean isOne){

        mAllowedMoves[y][x]=false;//два раза в одно место ходить нельзя
        activateRawOrColumn(false);

        if(isOne){
            mResults.onClick(y,x,true);
            playerOnePoints+=mMatrix[y][x];
            mResults.changeLabel(false, playerOnePoints);//изменяем  очки

        }else{
            mResults.onClick(y,x,false);
            playerTwoPoints+=mMatrix[y][x];
            mResults.changeLabel(true, playerTwoPoints);//изменяем  очки
        }
        currentActiveNumb = isRow ? x : y;
        isRow=!isRow;//после хода меняем строку на ряд
        mResults.changePlayer();
        activateRawOrColumn(true);

    }

    //Кликабельность ряда/строки
    protected void activateRawOrColumn(final boolean active){
        //Проверить есть ли ход
        int countMovesAllowed = 0; // для определения, есть ли допустимые ходы
        int y, x;

        for (int i=0;i<mMatrix.length;i++){

            y = isRow ? currentActiveNumb : i;
            x = isRow ? i : currentActiveNumb;
            mResults.changeButtonBg(y,x,active,active);

            if(mAllowedMoves[y][x]){//если ход допустим, то
               // Log.d("mAllowe=true",Integer.toString(y)+" "+Integer.toString(x));
                mResults.changeButtonClickable(y,x,active);//активируем, либо деактивируем его
                countMovesAllowed++;//если переменная останется нулем, то ходов нет
              //  Log.d("countMovesAllowed=",Integer.toString(countMovesAllowed));
            }
        }

        if(countMovesAllowed==0&&active) onResult();//Конец игры

    }


    //заполняем матрицу случайнами цифрами
    protected void generateMatrix() {

        mMatrix = new int[mSize][mSize];
        mAllowedMoves = new boolean[mSize][mSize];

        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {

                mMatrix[i][j] = rnd.nextInt(19) - 9; //от -9 до 9
                mAllowedMoves[i][j] = true; // сперва все ходы доступны
            }
        }
    }

    protected void onResult() {
        //метод интерфеса для отображения результатов
        mResults.onResult(playerOnePoints, playerTwoPoints);
    }
}
