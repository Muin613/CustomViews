package com.mh.customviews.Chart.LineChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MHLineChart extends View {
    private Paint paint;
    // 左上角x
    private int LEFTUPX = 50;
    // 左上角Y
    private int LEFTUPY = 50;


    // 左下角Y
    private int LEFTDOWNY = 400;
    // 右下角x
    private int RIGHTDOWNX = 600;


    // 上下间隔
    private int UPDOWNSPACE = 50;
    // 左右间隔
    private int LEFTRIGHTSPACE = 50;


    private int leftrightlines = (RIGHTDOWNX - LEFTUPX) / LEFTRIGHTSPACE;


    private boolean isOne;


    private ArrayList<ILineData> data = new ArrayList<>();
    private int count = 1;
    private int number = 1; // 最大10

    private boolean isFinish;
    private boolean doOne = false;
    private int finishCount = 1;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (number < 50) {
                number++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, 100);
            } else if (count < data.size() - 1) {
                number = 1;
                count++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, 100);
            } else if (!isFinish || finishCount != 5) {
                finishCount++;
                isFinish = true;
                handler.sendEmptyMessageDelayed(1, 100);
                invalidate();
            }
        }
    };

    public MHLineChart(Context context) {
        super(context);
    }

    public MHLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MHLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        // String layoutWidth =
        // attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
        // "layout_width");
        // String layoutHeight =
        // attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
        // "layout_height");
        // System.out.println("========="+layoutWidth+"===layoutHeight=="+layoutHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getPaint().reset();
        /**
         * 外框线
         */
        // 设置颜色
        getPaint().setColor(Color.parseColor("#A5A5A5"));
        // 设置宽度
        getPaint().setStrokeWidth(2);
        // 线的坐标点 （四个为一条线）
        float[] pts = {LEFTUPX, LEFTUPY - 20, LEFTUPX, LEFTDOWNY, LEFTUPX, LEFTDOWNY, RIGHTDOWNX + 20, LEFTDOWNY};
        // 画线
        canvas.drawLines(pts, getPaint());
        System.out.println("你还在画？");
        /**
         * 箭头
         */
        // 通过路径画三角形
        Path path = new Path();
        getPaint().setStyle(Paint.Style.FILL);// 设置为空心
        path.moveTo(LEFTUPX - 5, LEFTUPY - 20);// 此点为多边形的起点
        path.lineTo(LEFTUPX + 5, LEFTUPY - 20);
        path.lineTo(LEFTUPX, LEFTUPY - 35);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, getPaint());
        // 第二个箭头
        path.moveTo(RIGHTDOWNX + 20, LEFTDOWNY - 5);// 此点为多边形的起点
        path.lineTo(RIGHTDOWNX + 20, LEFTDOWNY + 5);
        path.lineTo(RIGHTDOWNX + 35, LEFTDOWNY);
        canvas.drawPath(path, getPaint());

        /**
         *  中间虚线
         */
        int updownlines = (LEFTDOWNY - LEFTUPY) / UPDOWNSPACE;
        float[] pts2 = new float[(updownlines + leftrightlines) * 4];
        // 计算位置
        for (int i = 0; i < updownlines; i++) {
            float x1 = LEFTUPX;
            float y1 = LEFTDOWNY - (i) * UPDOWNSPACE;
            float x2 = RIGHTDOWNX;
            float y2 = LEFTDOWNY - (i) * UPDOWNSPACE;
            pts2[i * 4 + 0] = x1;
            pts2[i * 4 + 1] = y1;
            pts2[i * 4 + 2] = x2;
            pts2[i * 4 + 3] = y2;
            getPaint().setColor(Color.BLACK);
            getPaint().setTextSize(25);
            canvas.drawText(String.valueOf(i), x1 - 25, y1 + 10, getPaint());
        }
        // 计算位置
        for (int i = 0; i < leftrightlines; i++) {
            float x1 = LEFTUPX + (i) * LEFTRIGHTSPACE;
            float y1 = LEFTUPY;
            float x2 = LEFTUPX + (i) * LEFTRIGHTSPACE;
            float y2 = LEFTDOWNY;
            pts2[(i + updownlines) * 4 + 0] = x1;
            pts2[(i + updownlines) * 4 + 1] = y1;
            pts2[(i + updownlines) * 4 + 2] = x2;
            pts2[(i + updownlines) * 4 + 3] = y2;
            canvas.drawText(String.valueOf(i), x2 - 10, y2 + 30, getPaint());
        }
        getPaint().setColor(Color.parseColor("#E0E0E0"));
        getPaint().setStrokeWidth(1);
        canvas.drawLines(pts2, getPaint());


        if (isOne) {
            // 线的路径
            Path path2 = new Path();
            // 共几个转折点
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    path2.moveTo(data.get(i).getX(), data.get(i).getY());
                } else {
                    path2.lineTo(data.get(i).getX(), data.get(i).getY());
                }
            }
            // 上一个点  减去 下一个点的位置 计算中间点位置
            path2.lineTo(data.get(count - 1).getX() + (data.get(count).getX() - data.get(count - 1).getX()) / 50 * number,
                    data.get(count - 1).getY() + (data.get(count).getY() - data.get(count - 1).getY()) / 50 * number);
            getPaint().setColor(Color.parseColor("#FF7A00"));
            getPaint().setStrokeWidth(3);
            getPaint().setStyle(Paint.Style.STROKE);// 设置为空心
            canvas.drawPath(path2, getPaint());

            path2.lineTo(data.get(count - 1).getX() + (data.get(count).getX() - data.get(count - 1).getX()) / 50 * number, LEFTDOWNY);
            path2.lineTo(data.get(0).getX(), LEFTDOWNY);
            path2.lineTo(data.get(0).getX(), data.get(0).getY());
            getPaint().setStyle(Paint.Style.FILL);// 设置为空心
            canvas.drawPath(path2, getShadeColorPaint());
            getPaint().reset();
            // 画出转折点圆圈
            for (int i = 0; i < count; i++) {
                // 画外圆
                getPaint().setColor(Color.parseColor("#FF7A00"));
                getPaint().setStyle(Paint.Style.FILL);// 设置为空心
                canvas.drawCircle(data.get(i).getX(), data.get(i).getY(), 7, getPaint());
                // 画中心点为白色
                getPaint().setColor(Color.WHITE);
                getPaint().setStyle(Paint.Style.FILL);
                canvas.drawCircle(data.get(i).getX(), data.get(i).getY(), 4, getPaint());
                getPaint().setTextSize(10);
                getPaint().setColor(Color.parseColor("#000000"));
                canvas.drawText("" + data.get(i).getYValue(), data.get(i).getX(), data.get(i).getY() - 20, getPaint());
            }
            if (isFinish) {
                getPaint().setColor(Color.parseColor("#FF7A00"));
                getPaint().setStyle(Paint.Style.FILL);// 设置为空心
                canvas.drawCircle(data.get(count).getX(), data.get(count).getY(), 7, getPaint());
                getPaint().setColor(Color.WHITE);
                getPaint().setStyle(Paint.Style.FILL);
                canvas.drawCircle(data.get(count).getX(), data.get(count).getY(), 4, getPaint());
                getPaint().setTextSize(10);
                getPaint().setColor(Color.parseColor("#000000"));
                canvas.drawText("" + data.get(count).getYValue(), data.get(count).getX(), data.get(count).getY() - 20, getPaint());
            }
            if (!doOne) {
                handler.sendEmptyMessage(1);
                doOne = true;
            }
        }
    }

    public MHLineChart setTotalX(int x) {
        LEFTRIGHTSPACE = RIGHTDOWNX / (x+2);
        return this;
    }

    public MHLineChart setTotalY(int y) {
        UPDOWNSPACE = LEFTDOWNY / (y+2);
        return this;
    }

    public MHLineChart build() {
        leftrightlines = (RIGHTDOWNX - LEFTUPX) / LEFTRIGHTSPACE;
        isOne = true;
        number = 1;
        count = 1;
        isFinish = false;
        invalidate();
        return this;
    }

    public void drawLine(List<ILineData> datas) {
        if (null == datas || datas.size() == 0)
            return;
        data.clear();
        data.addAll(datas);
        for (int i = 0; i < data.size(); i++) {
            ILineData bean = data.get(i);
            float xv = bean.getXValue();
            float yv = bean.getYValue();
            data.get(i).setX(LEFTUPX + (xv * LEFTRIGHTSPACE)).setY((LEFTDOWNY - yv * UPDOWNSPACE));
            System.out.println(data.get(i).getX() + "  " + data.get(i).getY());
        }
        isOne = true;
        number = 1;
        count = 1;
        isFinish = false;
        invalidate();
    }

    // 获取笔
    private Paint getPaint() {
        if (paint == null)
            paint = new Paint();
        return paint;
    }

    // 修改笔的颜色
    private Paint getShadeColorPaint() {
        Shader mShader = new LinearGradient(300, 50, 300, 400,
                new int[]{Color.parseColor("#55FF7A00"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        getPaint().setShader(mShader);
        return getPaint();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub 点击效果没有写了
        System.out.println("==========" + event.getX() + "===" + event.getY());
        return super.onTouchEvent(event);
    }
}
