package com.example.jinsungjun.pushpush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class Stage extends View {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    int gridCount;
    float unit;
    EndStage endStage;
    //플레이어
    Player player;
    //맵
    int currentMap[][];
    //그리드
    Paint gridPaint = new Paint();
    Paint boxPaint = new Paint();
    Paint goalPaint = new Paint();
    Paint successPaint = new Paint();

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
        invalidate();
        completionProcess();

    }

    public interface EndStage {
        public void makeText();
    }

    // 이동이 완료된 후에는 종료검사를 실행해준다
    private void completionProcess() {

        int goalCount = getCounts();

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

                // 플레이어의 위에 박스가 있을 때와 플레이어의 위에 골안에 들어간 박스가 있으면
                if (currentMap[player.y - 1][player.x] == 1 || currentMap[player.y - 1][player.x] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 위에 또다른 박스가 있으면, 박스 위에 골에 들어간 박스가 있는 경우
                    if (player.y - 2 >= gridCount || currentMap[player.y - 2][player.x] == 1 || currentMap[player.y - 2][player.x] == 7)
                        // 움직임 불가
                        return false;

                    //박스의 위에 골이 있는 경우
                    if (currentMap[player.y - 2][player.x] == 9) {

                        //플레이어의 위에 있는 박스가 골안에 이미 들어간 경우
                        if (currentMap[player.y - 1][player.x] == 7) {

                            //플레이어의 다음위치는 골안에, 박스는 여전히 파란박스
                            currentMap[player.y - 1][player.x] = 9;
                            currentMap[player.y - 2][player.x] = 7;
                        } else {
                            //박스의 골 초기 진입
                            currentMap[player.y - 1][player.x] = 0;
                            currentMap[player.y - 2][player.x] = 7;
                        }
                    } else {
                        //박스의 위에 아무것도 없을 경우

                        //플레이어의 위에 골안에 들어간 박스가 있으면
                        if (currentMap[player.y - 1][player.x] == 7) {
                            //플레이어의 다음 위치는 골, 골을 벗어난 박스는 원래의 검정 박스로
                            currentMap[player.y - 1][player.x] = 9;
                            currentMap[player.y - 2][player.x] = 1;
                        } else {
                            //박스를 가진 채 정상이동
                            currentMap[player.y - 1][player.x] = 0;
                            currentMap[player.y - 2][player.x] = 1;
                        }
                    }
                }
                break;

            case Stage.DOWN:

                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.y + 1) >= gridCount)
                    return false;

                // 플레이어의 아래쪽에 박스가 있을 때와 플레이어의 아래쪽에 골안에 들어간 박스가 있으면
                if (currentMap[player.y + 1][player.x] == 1 || currentMap[player.y + 1][player.x] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 아래쪽에 또다른 박스가 있으면, 박스 아래쪽에 골에 들어간 박스가 있는 경우
                    if (player.y + 2 >= gridCount || currentMap[player.y + 2][player.x] == 1 || currentMap[player.y + 2][player.x] == 7)
                        // 움직임 불가
                        return false;

                    //박스의 아래쪽에 골이 있는 경우
                    if (currentMap[player.y + 2][player.x] == 9) {

                        //플레이어의 아래쪽에 있는 박스가 골안에 들어간 경우
                        if (currentMap[player.y + 1][player.x] == 7) {

                            //플레이어의 다음위치는 골안에, 박스는 여전히 파란박스
                            currentMap[player.y + 1][player.x] = 9;
                            currentMap[player.y + 2][player.x] = 7;
                        } else {
                            //박스의 골 초기 진입
                            currentMap[player.y + 1][player.x] = 0;
                            currentMap[player.y + 2][player.x] = 7;
                        }
                    } else {
                        //박스의 아래쪽에 아무것도 없을 경우

                        //플레이어의 아래쪽에 골안에 들어간 박스가 있으면
                        if (currentMap[player.y + 1][player.x] == 7) {
                            //플레이어의 다음위치는 골안에, 골을 벗어난 박스는 원래의 검정 박스로
                            currentMap[player.y + 1][player.x] = 9;
                            currentMap[player.y + 2][player.x] = 1;
                        } else {
                            //박스를 가진 채 정상이동
                            currentMap[player.y + 1][player.x] = 0;
                            currentMap[player.y + 2][player.x] = 1;
                        }
                    }
                }
                break;

            case Stage.LEFT:

                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.x - 1) < 0)
                    return false;

                // 플레이어의 왼쪽에 박스가 있을 때와 플레이어의 왼쪽에 골안에 들어간 박스가 있으면
                if (currentMap[player.y][player.x - 1] == 1 || currentMap[player.y][player.x - 1] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 왼쪽에 또다른 박스가 있으면, 박스 왼쪽에 골에 들어간 박스가 있는 경우
                    if (player.x - 2 >= gridCount || currentMap[player.y][player.x - 2] == 1 || currentMap[player.y][player.x - 2] == 7)
                        // 움직임 불가
                        return false;

                    //박스의 왼쪽에 골이 있는 경우
                    if (currentMap[player.y][player.x - 2] == 9) {

                        //플레이어의 왼쪽에 있는 박스가 골안에 들어간 경우
                        if (currentMap[player.y][player.x - 1] == 7) {

                            //플레이어의 다음위치는 골안에, 박스는 여전히 파란박스
                            currentMap[player.y][player.x - 1] = 9;
                            currentMap[player.y][player.x - 2] = 7;
                        } else {
                            //박스의 골 초기 진입
                            currentMap[player.y][player.x - 1] = 0;
                            currentMap[player.y][player.x - 2] = 7;
                        }
                    } else {
                        //박스의 왼쪽에 아무것도 없을 경우

                        //플레이어의 왼쪽에 골안에 들어간 박스가 있으면
                        if (currentMap[player.y][player.x - 1] == 7) {
                            //플레이어의 다음위치는 골안에, 골을 벗어난 박스는 원래의 검정 박스로
                            currentMap[player.y][player.x - 1] = 9;
                            currentMap[player.y][player.x - 2] = 1;
                        } else {
                            //박스를 가진 채 정상이동
                            currentMap[player.y][player.x - 1] = 0;
                            currentMap[player.y][player.x - 2] = 1;
                        }
                    }
                }
                break;

            case Stage.RIGHT:
                //플레이어가 스테이지 밖을 벗어나는지 체크
                if ((player.x + 1) >= gridCount)
                    return false;

                // 플레이어의 오른쪽에 박스가 있을 때와 플레이어의 오른쪽에 골안에 들어간 박스가 있으면
                if (currentMap[player.y][player.x + 1] == 1 || currentMap[player.y][player.x + 1] == 7) {

                    //박스가 스테이지 밖을 벗어나거나, 플레이어의 오른편에 또다른 박스가 있으면, 박스 오른편에 골에 들어간 박스가 있는 경우
                    if (player.x + 2 >= gridCount || currentMap[player.y][player.x + 2] == 1 || currentMap[player.y][player.x + 2] == 7)
                        // 움직임 불가
                        return false;

                    //박스의 오른편에 골이 있는 경우
                    if (currentMap[player.y][player.x + 2] == 9) {

                        //플레이어의 오른편에 있는 박스가 골안에 들어간 경우
                        if (currentMap[player.y][player.x + 1] == 7) {

                            //플레이어의 다음위치는 골안에, 박스는 여전히 파란박스
                            currentMap[player.y][player.x + 1] = 9;
                            currentMap[player.y][player.x + 2] = 7;
                        } else {
                            //박스의 골 초기 진입
                            currentMap[player.y][player.x + 1] = 0;
                            currentMap[player.y][player.x + 2] = 7;
                        }
                    } else {
                        //박스의 오른편에 아무것도 없을 경우

                        //플레이어의 오른편에 골안에 들어간 박스가 있으면
                        if (currentMap[player.y][player.x + 1] == 7) {
                            //플레이어의 다음위치는 골안에, 골을 벗어난 박스는 원래의 검정 박스로
                            currentMap[player.y][player.x + 1] = 9;
                            currentMap[player.y][player.x + 2] = 1;
                        } else {
                            //박스를 가진 채 정상이동
                            currentMap[player.y][player.x + 1] = 0;
                            currentMap[player.y][player.x + 2] = 1;
                        }
                    }
                }
                break;
        }
        return true;

    }


    public void setCurrentMap(int[][] map) {

        currentMap = map;
    }
}

