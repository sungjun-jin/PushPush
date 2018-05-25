package com.example.jinsungjun.pushpush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class Stage extends View {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    int gridCount;
    float unit;
    EndStage endStage;

    int tempDirection = 8;


    //플레이어
    Player player;

    //맵
    int currentMap[][];

    //박스가 goal 구간 안에 있는지 없는지를 체크
    boolean goalFlag = false;

    //그리드
    Paint gridPaint = new Paint();
    Paint boxPaint = new Paint();
    Paint goalPaint = new Paint();
    Paint successPaint = new Paint();

//    EndStage endStage;


    public Stage(Context context) {
        super(context);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        boxPaint.setColor(Color.BLACK);
        goalPaint.setColor(Color.GREEN);
        successPaint.setColor(Color.BLUE);
        endStage = (MainActivity) context;

    }

    public void setConfig(int gridCount, float unit) {

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

    Paint tempPaint;

    // 맵 그리기
    private void drawMap(Canvas canvas) {

        for (int y = 0; y < currentMap.length; y++) {

            for (int x = 0; x < currentMap[0].length; x++) {

                if (currentMap[y][x] == 0) {
                    tempPaint = gridPaint;
                } else if (currentMap[y][x] == 1) {
                    tempPaint = boxPaint;

                } else if (currentMap[y][x] == 9) {
                    tempPaint = goalPaint;
                } else if (currentMap[y][x] == 7) {
                    tempPaint = successPaint;
                }
                canvas.drawRect(x * unit,
                        y * unit,
                        x * unit + unit,
                        y * unit + unit,
                        tempPaint);
            }
        }
    }

    //Player 그리기
    private void drawPlayer(Canvas canvas) {

        if (player != null) {
            canvas.drawCircle(
                    player.x * unit + unit / 2, //x좌표
                    player.y * unit + unit / 2, //y좌표
                    unit / 2, //크기
                    player.paint);
        }

    }

    public void move(int direction) {

        switch (direction) {
            case UP:
                if (collisionProcess(UP))
                    player.up();
                break;
            case DOWN:
                if (collisionProcess(DOWN))
                    player.down();
                break;
            case LEFT:
                if (collisionProcess(LEFT))
                    player.left();
                break;
            case RIGHT:
                if (collisionProcess(RIGHT))
                    player.right();
                break;
        }
        //OnDraw 호출
        completionProcess();
        invalidate();


    }

    public interface EndStage {
        public void makeText();
    }

    // 이동이 완료된 후에는 종료검사를 실행해준다
    private void completionProcess() {

        int goalCount = getCounts();

        Log.d("goalCount", "goalCount=========" + goalCount);

        if (goalCount == 0) {

            Log.d("success", "success=========");
            endStage.makeText();
        }
    }

    private int getCounts() {

        int goalCount = 0;

        for (int y = 0; y < currentMap.length; y++) {

            for (int x = 0; x < currentMap[0].length; x++) {

                if (currentMap[y][x] == 9)
                    goalCount = goalCount + 1;
            }
        }

        return goalCount;
    }


    //이동이 일어나기 전에는 충돌검사를 실행해준다
    public boolean collisionProcess(int direction) {

        switch (direction) {

            case Stage.UP:

                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.y - 1) < 0)
                    return false;

                if (goalFlag) {

                    Log.d("false", "wefwefwfwef");

                    if (currentMap[player.y - 2][player.x] == 0) {

                        currentMap[player.y - 1][player.x] = 9;
                        currentMap[player.y - 2][player.x] = 1;
                        goalFlag = false;
                    }
                }

                //플레이어의 위에 박스가 있으면
                if (currentMap[player.y - 1][player.x] == 1 || currentMap[player.y - 1][player.x] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 윗편에 또다른 박스가 있으면
                    if (player.y - 2 < 0 || currentMap[player.y - 2][player.x] == 1) {
                        goalFlag = false;
                        return false;
                    }

                    if (currentMap[player.y - 2][player.x] == 9) {

                        if (currentMap[player.y - 1][player.x] == 7) {

                            //앞 자리에 goal 넣어주고 박스이동
                            currentMap[player.y - 1][player.x] = 9;
                            currentMap[player.y - 2][player.x] = 7;
                        } else {
                            //박스가 처음으로 goal구간에 진입한 상황
                            currentMap[player.y - 1][player.x] = 0;
                            currentMap[player.y - 2][player.x] = 7;
                            goalFlag = true;
                        }
                    } else {
                        // 정상적으로 박스와 함께 움직인다.
                        currentMap[player.y - 1][player.x] = 0;
                        currentMap[player.y - 2][player.x] = 1;
                        goalFlag = false;
                    }
                }

                break;

            case Stage.DOWN:

                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.y + 1) >= gridCount)
                    return false;

                if (goalFlag) {


                    if (currentMap[player.y + 2][player.x] == 0) {

                        currentMap[player.y + 1][player.x] = 9;
                        currentMap[player.y + 2][player.x] = 1;
                        goalFlag = false;
                    }
                }

                //플레이어의 밑에 박스가 있다면
                if ((currentMap[player.y + 1][player.x]) == 1 || currentMap[player.y + 1][player.x] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 밑에 또다른 박스가 있으면
                    if (player.y + 2 >= gridCount || currentMap[player.y + 2][player.x] == 1) {

                        goalFlag = false;
                        //움직임 불가
                        return false;
                    }
                    if (currentMap[player.y + 2][player.x] == 9) {

                        if (currentMap[player.y + 1][player.x] == 7) {

                            //앞 자리에 goal 넣어주고 박스이동
                            currentMap[player.y + 1][player.x] = 9;
                            currentMap[player.y + 2][player.x] = 7;
                        } else {
                            //박스가 처음으로 goal구간에 진입한 상황
                            currentMap[player.y + 1][player.x] = 0;
                            currentMap[player.y + 2][player.x] = 7;
                            goalFlag = true;
                        }
                    } else {
                        // 정상적으로 박스와 함께 움직인다.
                        currentMap[player.y + 1][player.x] = 0;
                        currentMap[player.y + 2][player.x] = 1;
                        goalFlag = false;
                    }
                }

                break;

            case Stage.LEFT:


                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.x - 1) < 0)
                    return false;

                if (goalFlag) {

                    if (currentMap[player.y][player.x - 2] == 0) {

                        currentMap[player.y][player.x - 1] = 9;
                        currentMap[player.y][player.x - 2] = 1;
                        goalFlag = false;
                    }
                }

                //플레이어의 왼쪽에 박스가 있다면
                if ((currentMap[player.y][player.x - 1] == 1) || currentMap[player.y][player.x - 1] == 7) {
                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 왼편에 또다른 박스가 있으면
                    if (player.x - 2 < 0 || currentMap[player.y][player.x - 2] == 1) {
                        goalFlag = false;
                        //이동 불가
                        return false;
                    }

                    // 박스의 오른편에 goal이 있다면
                    if (currentMap[player.y][player.x - 2] == 9) {
                        //처음에는 goalFlag 구간 생략

                        //박스가 goal 구간 안으로 진입한 상황
                        if (currentMap[player.y][player.x - 1] == 7) {

                            //앞 자리에 goal 넣어주고 박스이동
                            currentMap[player.y][player.x - 1] = 9;
                            currentMap[player.y][player.x - 2] = 7;

                        } else {
                            //박스가 처음으로 goal구간에 진입한 상황
                            currentMap[player.y][player.x - 1] = 0;
                            currentMap[player.y][player.x - 2] = 7;
                            goalFlag = true;
                        }
                        // 박스의 오른편에 goal이 없다면
                    } else {
                        // 정상적으로 박스와 함께 움직인다.
                        currentMap[player.y][player.x - 1] = 0;
                        currentMap[player.y][player.x - 2] = 1;
                        goalFlag = false;
                    }
                }

                break;

            case Stage.RIGHT:


                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.x + 1) >= gridCount)
                    return false;

                if (goalFlag) {

                    if (currentMap[player.y][player.x + 2] == 0) {

                        currentMap[player.y][player.x + 1] = 9;
                        currentMap[player.y][player.x + 2] = 1;
                        goalFlag = false;
                    }
                }

                // 플레이어의 오른쪽에 박스가 있으면
                if (currentMap[player.y][player.x + 1] == 1 || currentMap[player.y][player.x + 1] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 오른편에 또다른 박스가 있으면
                    if (player.x + 2 >= gridCount || currentMap[player.y][player.x + 2] == 1) {
                        //goalFlag == false
                        goalFlag = false;
                        // 움직임 불가
                        return false;
                    }

                    // 박스의 오른편에 goal이 있다면
                    if (currentMap[player.y][player.x + 2] == 9) {
                        //처음에는 goalFlag 구간 생략

                        //박스가 goal 구간 안으로 진입한 상황
                        if (currentMap[player.y][player.x + 1] == 7) {

                            //앞 자리에 goal 넣어주고 박스이동
                            currentMap[player.y][player.x + 1] = 9;
                            currentMap[player.y][player.x + 2] = 7;

                        } else {
                            //박스가 처음으로 goal구간에 진입한 상황
                            currentMap[player.y][player.x + 1] = 0;
                            currentMap[player.y][player.x + 2] = 7;
                            goalFlag = true;
                        }
                        // 박스의 오른편에 goal이 없다면
                    } else {
                        // 정상적으로 박스와 함께 움직인다.
                        currentMap[player.y][player.x + 1] = 0;
                        currentMap[player.y][player.x + 2] = 1;
                        goalFlag = false;
                    }
                }
                break;
        }
        Log.d("goalFlag", goalFlag + "");
        return true;
    }


    public void setCurrentMap(int[][] map) {

        currentMap = map;
    }
}

