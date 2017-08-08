package com.mh.customviews.Chart.PieChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import java.util.List;

/**
 * Created by mh on 2017/8/7.
 */

public class MHPieChart extends View {


    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 饼状图宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图起始角度
     */
    private float mStartAngle = 0f;
    /**
     * 用户数据
     */
    private List<IPieData> mData;
    /**
     * 动画时间
     */
    private static final long ANIMATION_DURATION = 1000;
    /**
     * 自定义动画
     */
    private PieChartAnimation mAnimation;
    /**
     * 绘制方式
     */
    private int mDrawWay = PART;
    public static final int PART = 0;//分布绘制
    public static final int COUNT = 1;//连续绘制


    private RectF normalOval;//不分裂的饼图
    private RectF normalInnerOval;//饼图内部白圆
    private RectF selectOval;//选中的饼图
    private RectF selectInnerOval;//选中的内部饼图

    private Paint paint;


    private Context context;


    private int select = -1;

    public MHPieChart(Context context) {
        this(context, null);
    }

    public MHPieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHPieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防止抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔为填充
        //初始化动画
        mAnimation = new PieChartAnimation();
        mAnimation.setDuration(ANIMATION_DURATION);


        normalOval = new RectF();
        normalInnerOval = new RectF();
        selectOval = new RectF();
        selectInnerOval = new RectF();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle
     */
    public void setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();//刷新
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    public void setData(List<IPieData> mData) {
        setmData(mData);
    }

    /**
     * 设置数据和绘制方式
     *
     * @param mData
     */
    public void setData(List<IPieData> mData, int mDrawWay) {
        setmData(mData);
        this.mDrawWay = mDrawWay;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    private void setmData(List<IPieData> mData) {
        sumValue = 0;
        this.mData = mData;
        initData(mData);
        startAnimation(mAnimation);
        invalidate();
    }

    float sumValue = 0;//数据值的总和

    /**
     * 初始化数据
     *
     * @param mData
     */
    private void initData(List<IPieData> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        /**
         * 计算数据总和确定颜色
         */
        for (int i = 0; i < mData.size(); i++) {
            IPieData data = mData.get(i);
            sumValue += Float.valueOf(data.getValue());
        }
        /**
         * 计算百分比和角度
         */
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            IPieData data = mData.get(i);
            data.setCurrentStartAngle(currentStartAngle);
            //通过总和来计算百分比
            float percentage = Float.valueOf(data.getValue()) / sumValue;
            //通过百分比来计算对应的角度
            float angle = percentage * 360;
            //设置用户数据
            data.setPercentage(percentage);
            data.setAngle(angle);
            currentStartAngle += angle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        normalOval.left = (float) (getMeasuredWidth() * 0.1);
        normalOval.top = (float) (getMeasuredHeight() * 0.1);
        normalOval.right = (float) (getMeasuredWidth() * 0.9);
        normalOval.bottom = (float) (getMeasuredHeight() * 0.9);

        normalInnerOval.left = (float) (normalOval.left + getMeasuredHeight() / 4);
        normalInnerOval.top = (float) (normalOval.top + getMeasuredHeight() / 4);
        normalInnerOval.right = (float) (normalOval.right - getMeasuredHeight() / 4);
        normalInnerOval.bottom = (float) (normalOval.bottom - getMeasuredHeight() / 4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData == null) {
            return;
        }
        //1.移动画布到中心点
//        canvas.translate(mWidth / 2, mHeight / 2);
        //2.设置当前起始角度
        float currentStartAngle = mStartAngle;

        for (int i = 0; i < mData.size(); i++) {
            IPieData data = mData.get(i);
//            绘制饼图
            if (select < 0) {
                if (mDrawWay == PART) {
//                    canvas.drawArc(rectF, data.getCurrentStartAngle(), data.getAngle(), true, mPaint);
                    drawCom(canvas,data,data.getCurrentStartAngle());
                } else if (mDrawWay == COUNT) {
//                    canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
                    drawCom(canvas,data,currentStartAngle);
                    //7.绘制下一块扇形时先将角度加上当前扇形的角度
                    currentStartAngle += data.getAngle();
                }
            } else if (select >= 0 && i == select){
                drawSelect(canvas,data);
            }else
                drawCom(canvas,data,data.getCurrentStartAngle());
        }
    }


    /**
     * 自定义动画
     */
    public class PieChartAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < mData.size(); i++) {
                    IPieData data = mData.get(i);
                    //通过总和来计算百分比
                    float percentage = Float.valueOf(data.getValue()) / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    //根据插入时间来计算角度
                    angle = angle * interpolatedTime;
                    data.setAngle(angle);
                }
            } else {//默认显示效果
                for (int i = 0; i < mData.size(); i++) {
                    //通过总和来计算百分比
                    IPieData data = mData.get(i);
                    float percentage = Float.valueOf(data.getValue()) / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    data.setPercentage(percentage);
                    data.setAngle(angle);
                }
            }
            invalidate();
        }
    }


    void drawSelect(Canvas canvas, IPieData data) {

        selectOval.left = (float) (getMeasuredWidth() * 0.1);
        selectOval.top = (float) (getMeasuredHeight() * 0.1);
        selectOval.right = (float) (getMeasuredWidth() * 0.9);
        selectOval.bottom = (float) (getMeasuredHeight() * 0.9);

        selectInnerOval.left = (float) (selectOval.left + getMeasuredHeight() / 4);
        selectInnerOval.top = (float) (selectOval.top + getMeasuredHeight() / 4);
        selectInnerOval.right = (float) (selectOval.right - getMeasuredHeight() / 4);
        selectInnerOval.bottom = (float) (selectOval.bottom - getMeasuredHeight() / 4);


        int middle = (int) (data.getCurrentStartAngle() * 2 + data.getAngle()) / 2;
        if (middle <= 90) {
            int top = (int) (Math.sin(Math.toRadians(middle)) * 15);
            int left = (int) (Math.cos(Math.toRadians(middle)) * 15);
            selectOval.left += left;
            selectOval.right += left;
            selectOval.top += top;
            selectOval.bottom += top;

            selectInnerOval.left += left - 1;
            selectInnerOval.right += left - 1;
            selectInnerOval.top += top - 1;
            selectInnerOval.bottom += top - 1;
        }
        if (middle > 90 && middle <= 180) {
            middle = 180 - middle;
            int top = (int) (Math.sin(Math.toRadians(middle)) * 15);
            int left = (int) (Math.cos(Math.toRadians(middle)) * 15);
            selectOval.left -= left;
            selectOval.right -= left;
            selectOval.top += top;
            selectOval.bottom += top;


            selectInnerOval.left -= left +2;
            selectInnerOval.right -= left - 2;
            selectInnerOval.top += top - 2;
            selectInnerOval.bottom += top - 2;
        }
        if (middle > 180 && middle <= 270) {
            middle = 270 - middle;
            int left = (int) (Math.sin(Math.toRadians(middle)) * 15);
            int top = (int) (Math.cos(Math.toRadians(middle)) * 15);
            selectOval.left -= left;
            selectOval.right -= left;
            selectOval.top -= top;
            selectOval.bottom -= top;


            selectInnerOval.left -= left - 1;
            selectInnerOval.right -= left - 1;
            selectInnerOval.top -= top - 1;
            selectInnerOval.bottom -= top - 1;
        }
        if (middle > 270 && middle <= 360) {
            middle = 360 - middle;
            int top = (int) (Math.sin(Math.toRadians(middle)) * 15);
            int left = (int) (Math.cos(Math.toRadians(middle)) * 15);
            selectOval.left += left;
            selectOval.right += left;
            selectOval.top -= top;
            selectOval.bottom -= top;

            selectInnerOval.left += left -1;
            selectInnerOval.right += left -1;
            selectInnerOval.top -= top - 1;
            selectInnerOval.bottom -= top - 1;
        }
        paint.setColor(Color.parseColor(data.getColor()));
        canvas.drawArc(selectOval, data.getCurrentStartAngle(), data.getAngle(), true,
                paint);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawArc(selectInnerOval, data.getCurrentStartAngle(), data.getAngle(), true, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawOval(normalInnerOval, paint);
        canvas.drawArc(normalInnerOval, data.getCurrentStartAngle(),  data.getAngle(), true, paint);
    }




    void drawCom(Canvas canvas,IPieData data,float currentStartAngle){
        paint.setColor(Color.parseColor(data.getColor()));
        canvas.drawArc(normalOval, currentStartAngle, data.getAngle(), true,
                paint);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawArc(normalInnerOval,currentStartAngle,data.getAngle(), true, paint);
        canvas.drawOval(normalInnerOval, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            int radius = 0;
            //第四象限
            if (x >= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
                radius = (int) (Math.atan((y - getMeasuredHeight() / 2) * 1.0f
                        / (x - getMeasuredWidth() / 2)) * 180 / Math.PI);
            }
            //第三象限
            if (x <= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
                radius = (int) (Math.atan((getMeasuredWidth() / 2 - x)
                        / (y - getMeasuredHeight() / 2))
                        * 180 / Math.PI + 90);
            }
            //第二象限
            if (x <= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
                radius = (int) (Math.atan((getMeasuredHeight() / 2 - y)
                        / (getMeasuredWidth() / 2 - x))
                        * 180 / Math.PI + 180);
            }
            //第一象限
            if (x >= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
                radius = (int) (Math.atan((x - getMeasuredWidth() / 2)
                        / (getMeasuredHeight() / 2 - y))
                        * 180 / Math.PI + 270);
            }
            for (int i = 0; i < mData.size(); i++) {
                IPieData data = mData.get(i);
                if (data.getCurrentStartAngle() <= radius && data.getCurrentStartAngle() + data.getAngle() >= radius) {
                    select = i;
                    Toast.makeText(context, "点击" + i,
                            Toast.LENGTH_SHORT)
                            .show();
                    invalidate();
                    return true;
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
