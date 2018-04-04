package com.mh.customviews.Drag.control.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.mh.customviews.R;

/**
 * Created by munin on 2018/3/31.
 * 有文字的imageView
 * 有圆形边框
 */

public class MHImageXYButton extends AppCompatImageView implements IRemoterView{

    private Paint mPaint;
    private Rect mRect = new Rect();
    private String text;
    private boolean edgeFlag = false;
    private int mhX = -1;
    private int mhY = -1;//mhY与mhX必须都大于0,且成比例

    public MHImageXYButton(Context context) {
        this(context, null);
    }

    public MHImageXYButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHImageXYButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        //1.获得自定义属性值的一个容器
        TypedArray mTd = context.obtainStyledAttributes(attrs, R.styleable.MHImageXYButton);
        //2.通过属性容器获得属性，如果没有就使用默认值
        edgeFlag = mTd.getBoolean(R.styleable.MHImageXYButton_edge_flag, false);
        mhX = mTd.getInt(R.styleable.MHImageXYButton_x, 0);
        mhY = mTd.getInt(R.styleable.MHImageXYButton_y, 0);
        mTd.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        int size = Math.min(width, height);
        if (mhY <=0 || mhX <=0)
            setMeasuredDimension(size, size);
        else
            setMeasuredDimension(width, width * mhY / mhX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = getWidth() / 2;
        if (edgeFlag) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.parseColor("#cdcdcd"));
            canvas.drawCircle(radius, radius, radius-1, mPaint);
        }
        // 按下时有背景变化
        if (isPressed()) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor("#20000000"));
            canvas.drawCircle(radius, radius, radius - 4, mPaint);
        }

        if (!TextUtils.isEmpty(text)) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(radius / 2);
            mPaint.getTextBounds(text, 0, text.length(), mRect);
            int textHeight = mRect.bottom - mRect.top;
            int textWidth = mRect.right - mRect.left;
            canvas.drawText(text, radius - textWidth / 2, radius + textHeight / 2, mPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        // View的状态有发生改变的触发
        invalidate();
    }

    public void setEdgeFlag(boolean edgeFlag) {
        this.edgeFlag = edgeFlag;
    }

    public int getMhX() {
        return mhX;
    }

    public void setMhX(int mhX) {
        this.mhX = mhX;
    }

    public void setMhY(int mhY) {
        this.mhY = mhY;
    }

    public int getMhY() {
        return mhY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public View leftController() {
        return null;
    }

    @Override
    public void setLeftController(View view) {

    }

    @Override
    public View rightController() {
        return null;
    }

    @Override
    public void setRightController(View view) {

    }

    @Override
    public View topController() {
        return null;
    }

    @Override
    public void setTopController(View view) {

    }

    @Override
    public View bottomController() {
        return null;
    }

    @Override
    public void setBottomController(View view) {

    }

    @Override
    public View centerController() {
        return null;
    }

    @Override
    public void setCenterController(View view) {

    }

    @Override
    public int viewType() {
        return SINGLE;
    }

    @Override
    public View[] controlViews() {
        return new View[0];
    }

    @Override
    public void setControllerViews(int left, int top, int right, int bottom, int center) {

    }

    @Override
    public IRemoterView build() {
        return null;
    }


}