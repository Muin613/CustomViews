package com.mh.customviews.Knob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mh.customviews.R;

/**
 * Created by mh on 2017/8/7.
 * knob
 */

public class MHKnobBtn extends View {

    // 控件的正真高度
    private int realHeight;
    // 控件的真正的宽度
    private int realWidth;
    // 控件宽
    private int width;
    // 控件高
    private int height;
    // 刻度盘半径
    private int dialRadius;
    // 圆弧半径
    private int arcRadius;
    // 刻度高
    private int scaleHeight = dp2px(10);
    // 刻度盘画笔
    private Paint dialPaint;
    // 圆弧画笔
    private Paint arcPaint;
    // 标题画笔
    private Paint titlePaint;
    // 旋钮标识画笔
    private Paint knobFlagPaint;
    // 旋转按钮画笔
    private Paint buttonPaint;
    // 旋钮显示画笔
    private Paint knobShowPaint;
    // 文本提示
    private String title = "旋钮设置";
    // 旋钮的当前度数
    private int knobDegree = 15;
    // 最低度数
    private int minDegree = 15;
    // 最高度数
    private int maxDegree = 30;
    // 四格代表温度1度
    private int angleRate = 4;
    // 每格的角度
    private float angleOne = (float) 270 / (maxDegree - minDegree) / angleRate;
    // 按钮图片
    private Bitmap buttonImage = BitmapFactory.decodeResource(getResources(),
            R.mipmap.mh_knob);
    // 按钮图片阴影
    private Bitmap buttonImageShadow = BitmapFactory.decodeResource(getResources(),
            R.mipmap.mh_knob_shadow);
    // 抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    // 旋钮改变监听
    private OnKnobChangeListener onTempChangeListener;

    // 以下为旋转按钮相关

    // 当前按钮旋转的角度
    private float rotateAngle;
    // 当前的角度
    private float currentAngle;
    //按钮的半径
    private int btnR;


    public MHKnobBtn(Context context) {
        this(context, null);
    }

    public MHKnobBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHKnobBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setStrokeWidth(dp2px(2));
        dialPaint.setStyle(Paint.Style.STROKE);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.parseColor("#FFFF00"));
        arcPaint.setStrokeWidth(dp2px(2));
        arcPaint.setStyle(Paint.Style.STROKE);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(sp2px(15));
        titlePaint.setColor(Color.parseColor("#000000"));
        titlePaint.setStyle(Paint.Style.STROKE);

        knobFlagPaint = new Paint();
        knobFlagPaint.setAntiAlias(true);
        knobFlagPaint.setTextSize(sp2px(25));
        knobFlagPaint.setColor(Color.parseColor("#00FF00"));
        knobFlagPaint.setStyle(Paint.Style.STROKE);

        buttonPaint = new Paint();
        knobFlagPaint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        knobShowPaint = new Paint();
        knobShowPaint.setAntiAlias(true);
        knobShowPaint.setTextSize(sp2px(60));
        knobShowPaint.setColor(Color.parseColor("#FF0000"));
        knobShowPaint.setStyle(Paint.Style.STROKE);

        btnR = Math.min(buttonImage.getWidth() / 2, buttonImage.getHeight() / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        width = height = Math.min(h, w);
        realHeight = h;
        // 刻度盘半径
        dialRadius = width / 2 - dp2px(20);
        // 圆弧半径
        arcRadius = dialRadius - dp2px(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas);
        drawArc(canvas);
        drawText(canvas);
        drawButton(canvas);
        drawTemp(canvas);
    }

    /**
     * 绘制刻度盘
     *
     * @param canvas 画布
     */
    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 逆时针旋转135-2度
        canvas.rotate(-133);
        dialPaint.setColor(Color.parseColor("#3CB7EA"));
        for (int i = 0; i < angleRate * (maxDegree - minDegree); i++) {
            canvas.drawLine(0, -dialRadius, 0, -dialRadius + scaleHeight, dialPaint);
            canvas.rotate(angleOne);
        }

        canvas.rotate(90);
        dialPaint.setColor(Color.parseColor("#E37364"));
        for (int i = 0; i < (knobDegree - minDegree) * angleRate; i++) {
            canvas.drawLine(0, -dialRadius, 0, -dialRadius + scaleHeight, dialPaint);
            canvas.rotate(angleOne);
        }
        canvas.restore();
    }

    /**
     * 绘制刻度盘下的圆弧
     *
     * @param canvas 画布
     */
    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(135 + 2);
        RectF rectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        canvas.drawArc(rectF, 0, 265, false, arcPaint);
        canvas.restore();
    }

    /**
     * 绘制标题与温度标识
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        canvas.save();

        // 绘制标题
        float titleWidth = titlePaint.measureText(title);
        canvas.drawText(title, (width - titleWidth) / 2, dialRadius * 2 + dp2px(15), titlePaint);

        // 绘制最小温度标识
        // 最小温度如果小于10，显示为0x
        String minTempFlag = minDegree < 10 ? "0" + minDegree : minDegree + "";
        float tempFlagWidth = titlePaint.measureText(maxDegree + "");
        canvas.rotate(55, width / 2, height / 2);
        canvas.drawText(minTempFlag, (width - tempFlagWidth) / 2, height + dp2px(5), knobFlagPaint);

        // 绘制最大温度标识
        canvas.rotate(-105, width / 2, height / 2);
        canvas.drawText(maxDegree + "", (width - tempFlagWidth) / 2, height + dp2px(5), knobFlagPaint);
        canvas.restore();
    }

    /**
     * 绘制旋转按钮
     *
     * @param canvas 画布
     */
    private void drawButton(Canvas canvas) {
        // 按钮宽高
        int buttonWidth = buttonImage.getWidth();
        int buttonHeight = buttonImage.getHeight();
        // 按钮阴影宽高
        int buttonShadowWidth = buttonImageShadow.getWidth();
        int buttonShadowHeight = buttonImageShadow.getHeight();

        // 绘制按钮阴影
        canvas.drawBitmap(buttonImageShadow, (width - buttonShadowWidth) / 2,
                (realHeight - buttonShadowHeight) / 2, buttonPaint);

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        // 设置按钮位置
        matrix.setTranslate(buttonWidth / 2, buttonHeight / 2);
        // 设置旋转角度
        matrix.preRotate(45 + rotateAngle);
        // 按钮位置还原，此时按钮位置在左上角
        matrix.preTranslate(-buttonWidth / 2, -buttonHeight / 2);
        // 将按钮移到中心位置
        matrix.postTranslate((width - buttonWidth) / 2, (realHeight - buttonHeight) / 2);

        //设置抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawBitmap(buttonImage, matrix, buttonPaint);
    }

    /**
     * 绘制温度
     *
     * @param canvas 画布
     */
    private void drawTemp(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);

        float tempWidth = knobShowPaint.measureText(knobDegree + "");
        float tempHeight = (knobShowPaint.ascent() + knobShowPaint.descent()) / 2;
        canvas.drawText(knobDegree + "°", -tempWidth / 2 - dp2px(5), -tempHeight, knobShowPaint);
        canvas.restore();
    }

    private boolean isDown;
    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:

                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();

                float angle = calcAngle(targetX, targetY);

                // 滑过的角度增量
                float angleIncreased = angle - currentAngle;

                // 防止越界
                if (angleIncreased < -270) {
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > 270) {
                    angleIncreased = angleIncreased - 360;
                }

                IncreaseAngle(angleIncreased);
                currentAngle = angle;
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDown && isMove) {
                    // 纠正指针位置
                    rotateAngle = (float) ((knobDegree - minDegree) * angleRate * angleOne);
                    invalidate();
                    // 回调温度改变监听
                    if (null != onTempChangeListener)
                        onTempChangeListener.onKnobChange(knobDegree);
                    isDown = false;
                    isMove = false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - width / 2;
        float y = targetY - height / 2;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }




    /**
     * 增加旋转角度
     *
     * @param angle 增加的角度
     */
    private void IncreaseAngle(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > 270) {
            rotateAngle = 270;
        }
        // 加上0.5是为了取整时四舍五入
        knobDegree = (int) ((rotateAngle / angleOne) / angleRate + 0.5) + minDegree;
    }

    /**
     * 设置几格代表1度，默认4格
     *
     * @param angleRate 几格代表1度
     */
    public void setAngleRate(int angleRate) {
        this.angleRate = angleRate;
    }

    /**
     * 设置温度
     *
     * @param temp 设置的温度
     */
    public void setTemp(int temp) {
        setTemp(minDegree, maxDegree, temp);
    }

    /**
     * 设置温度
     *
     * @param minTemp 最小温度
     * @param maxTemp 最大温度
     * @param temp    设置的温度
     */
    public void setTemp(int minTemp, int maxTemp, int temp) {
        this.minDegree = minTemp;
        this.maxDegree = maxTemp;
        if (temp < minTemp) {
            this.knobDegree = minTemp;
        } else {
            this.knobDegree = temp;
        }

        // 计算旋转角度
        rotateAngle = (float) ((temp - minTemp) * angleRate * angleOne);
        // 计算每格的角度
        angleOne = (float) 270 / (maxTemp - minTemp) / angleRate;
        invalidate();
    }

    public void setOnTempChangeListener(OnKnobChangeListener onTempChangeListener) {
        this.onTempChangeListener = onTempChangeListener;
    }

    /**
     * 旋钮改变监听
     */
    public interface OnKnobChangeListener {
        //     旋钮回调
        void onKnobChange(int change);
    }


    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
