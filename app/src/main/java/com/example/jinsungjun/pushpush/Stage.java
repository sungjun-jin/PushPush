package com.example.jinsungjun.pushpush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Stage extends View {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    int gridCount;
    float unit;

    //플레이어
    Player player;

    //맵
    int currentMap[][];

    //그리드
    Paint gridPaint = new Paint();

    public Stage(Context context) {
        super(context);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);

    }

    public void setConfig(int gridCount,float unit) {

        this.gridCount = gridCount;
        this.unit = unit;

    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMap(canvas);
        drawPlayer(canvas);
    }

    // 맵 그리기
    private void drawMap(Canvas canvas) {

        for(int y=0;y<currentMap.length;y++) {

            for(int x=0;x<currentMap[0].length;x++) {

                canvas.drawRect(x*unit,
                        y*unit,
                        x * unit +unit,
                        y* unit +unit,
                        gridPaint);
            }
        }
    }

    //Player 그리기
    private void drawPlayer(Canvas canvas) {

        if(player != null) {
            canvas.drawCircle(
                    player.x * unit + unit/2, //x좌표
                    player.y * unit + unit/2, //y좌표
                    unit/2, //크기
                    player.paint);
        }
    }

    public void move(int direction) {

        switch (direction) {
            case UP :
                if((player.y - 1) >= 0)
                player.up();
                break;
            case DOWN :
                if((player.y + 1) < gridCount)
                player.down();
                break;
            case LEFT :
                if((player.x - 1) >= 0)
                player.left();
                break;
            case RIGHT :
                if((player.x + 1) < gridCount)
                player.right();
                break;
        }
        //OnDraw 호출
        invalidate();
    }


    public void setCurrentMap(int[][] map) {

        currentMap = map;
    }
}
