package com.example.jinsungjun.pushpush;

import android.graphics.Color;
import android.graphics.Paint;

public class Player {

    Paint paint;
    int x,y;

    public Player() {

        //player의 색 입력
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        //기본좌표 입력
        x = 0;
        y = 0;
    }

    public void up() {

        y = y - 1;

    }

    public void down() {

        y  = y + 1;

    }

    public void left() {

        x = x - 1;

    }

    public void right() {

        x = x + 1;

    }

}
