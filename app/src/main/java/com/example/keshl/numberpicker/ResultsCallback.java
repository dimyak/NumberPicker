package com.example.keshl.numberpicker;

/**
 * Created by keshl on 22.05.2018.
 */
//Интерфейс для MainActivity, который будет изменять ui элементы
//********************************************************************
public interface ResultsCallback {
    //для изменения ваших очков и очков соперника
    void changeLabel(boolean upLabel, int points);

    //для изменения цвета кнопок
    void changeButtonBg(int y, int x, boolean row, boolean active);

    //для заполнения кнопок цифрами
    void setButtonText(int y, int x, int text);

    //для блокировки/разблокировки кнопок
    void changeButtonClickable(int y, int x, boolean clickable);

    //по окончанию партии
    void onResult(int one, int two);

    //по нажатию на кнопку
    void onClick(int y, int x, boolean flyDown);

    void changePlayer();
}
