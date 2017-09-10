package com.mh.customviews.ICode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;


import com.mh.customviews.R;

import java.util.Random;

/**
 * Created by Administrator on 2017/9/9.
 */

public class MHICodeView1 extends ImageView {

    private int mWidth;
    private int mHeight;

    private int iCodeWidth;
    private int iCodeHeight;

    private int iCodeX;
    private int iCodeY;
    private Random mRandom;
    private Paint mPaint;


    private Path iCodePath;
    private PorterDuffXfermode Xfermode;

    private boolean isDrawMask;

    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;

    private Bitmap mMaskShadowBitmap;
    private Paint mMaskShadowPaint;

    private int iCodeOffsetX;

    private boolean isMatch;
    private float deviation;

    private ValueAnimator mFailAnim;
    private boolean isSuccess;
    private ValueAnimator mSuccessAnim;
    private Paint mSuccessPaint;
    private int mSuccessAnimOffset;
    private Path mSuccessPath;


    private OnICodeMatchCallback callback;

    public MHICodeView1(Context context) {
        this(context, null);
    }

    public MHICodeView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHICodeView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initAnim();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isMatch){
            if(iCodePath!=null){
                canvas.drawPath(iCodePath,mPaint);
            }
            if(null!=mMaskBitmap&&null!=mMaskShadowBitmap&&isDrawMask){
                canvas.drawBitmap(mMaskShadowBitmap,-iCodeX+iCodeOffsetX,0,mMaskShadowPaint);
                canvas.drawBitmap(mMaskBitmap,-iCodeX+iCodeOffsetX,0,null);
            }
            if(isSuccess){
                canvas.translate(mSuccessAnimOffset,0);
                canvas.drawPath(mSuccessPath,mSuccessPaint);
            }
        }
    }

    public void setCallback(OnICodeMatchCallback callback) {
        this.callback = callback;
    }

    public void  matchICode(){
        if(null!=callback&&isMatch){
            if(Math.abs(iCodeOffsetX-iCodeX)<deviation){
                mSuccessAnim.start();
            }else{
                mFailAnim.start();
            }
        }
    }


    public void resetICode(){
        iCodeOffsetX=0;
        invalidate();
    }

    public int getMaxMoveValue(){
        return mWidth-iCodeWidth;
    }


    public void setCurrentICodeValue(int offset){
        iCodeOffsetX=offset;
        invalidate();
    }
    void initView(Context context, AttributeSet attrs) {
        int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 32, getResources().getDisplayMetrics());
        iCodeHeight = iCodeWidth = defaultSize;
        deviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 6, getResources().getDisplayMetrics());
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ICodeView1);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.ICodeView1_i_code_height:
                    iCodeHeight = (int) ta.getDimension(attr, defaultSize);
                    break;
                case R.styleable.ICodeView1_i_code_width:
                    iCodeWidth = (int) ta.getDimension(attr, defaultSize);
                    break;
                case R.styleable.ICodeView1_i_code_deviation:
                    deviation = ta.getDimension(attr, defaultSize);
                    break;
            }
        }
        ta.recycle();
        mRandom = new Random(System.nanoTime());
//        常规
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0x99000000);
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
//蒙盖
        Xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//阴影
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        iCodePath = new Path();


    }

    void initAnim() {
        mFailAnim = ValueAnimator.ofFloat(0, 1);
        mFailAnim.setDuration(100)
                .setRepeatCount(4);
        mFailAnim.setRepeatMode(ValueAnimator.REVERSE);
        //失败的时候先闪一闪动画 斗鱼是 隐藏-显示 -隐藏 -显示
        mFailAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != callback)
                    callback.onMatchFail(MHICodeView1.this);
            }
        });
        mFailAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (animatedValue < 0.5f) {
                    isDrawMask = false;
                } else {
                    isDrawMask = true;
                }
                invalidate();
            }
        });

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        mSuccessAnim = ValueAnimator.ofInt(mWidth + width, 0);
        mSuccessAnim.setDuration(500);
        mSuccessAnim.setInterpolator(new FastOutLinearInInterpolator());
        mSuccessAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSuccessAnimOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mSuccessAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSuccess = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != callback)
                    callback.onMatchSuccess(MHICodeView1.this);
                isSuccess = false;
                isMatch = false;
            }
        });
        mSuccessPaint = new Paint();
        mSuccessPaint.setShader(new LinearGradient(0, 0, width / 2 * 3, mHeight, new int[]{
                0x00ffffff, 0x88ffffff}, new float[]{0, 0.5f},
                Shader.TileMode.MIRROR));
        //模仿斗鱼 是一个平行四边形滚动过去
        mSuccessPath = new Path();
        mSuccessPath.moveTo(0, 0);
        mSuccessPath.rLineTo(width, 0);
        mSuccessPath.rLineTo(width / 2, mHeight);
        mSuccessPath.rLineTo(-width, 0);
        mSuccessPath.close();
    }


    public void createICode() {
        if (getDrawable() != null) {
            isMatch = true;
            createICodePath();
            createMask();
            invalidate();
        }
    }


    void createICodePath() {
        int gap = iCodeWidth / 3;
        iCodeX = mRandom.nextInt(mWidth - iCodeWidth - gap);
        iCodeY = mRandom.nextInt(mHeight - iCodeHeight - gap);
        iCodePath.reset();
        iCodePath.lineTo(0, 0);
        iCodePath.moveTo(iCodeX, iCodeY);
        iCodePath.lineTo(iCodeX + gap, iCodeY);
        //draw凹凸
        drawPartCircle(new PointF(iCodeX + gap, iCodeY), new PointF(iCodeX + gap * 2, iCodeY), iCodePath, mRandom.nextBoolean());
        iCodePath.lineTo(iCodeX + iCodeWidth, iCodeY);
        iCodePath.lineTo(iCodeX + iCodeWidth, iCodeY + gap);
        //draw凹凸
        drawPartCircle(new PointF(iCodeX + iCodeWidth, iCodeY + gap),
                new PointF(iCodeX + iCodeWidth, iCodeY + gap * 2),
                iCodePath, mRandom.nextBoolean());
        iCodePath.lineTo(iCodeX + iCodeWidth, iCodeY + iCodeHeight);
        iCodePath.lineTo(iCodeX + iCodeWidth - gap, iCodeY + iCodeHeight);
        //draw凹凸
        drawPartCircle(new PointF(iCodeX + iCodeWidth - gap, iCodeY + iCodeHeight),
                new PointF(iCodeX + iCodeWidth - gap * 2, iCodeY + iCodeHeight),
                iCodePath, mRandom.nextBoolean());
        iCodePath.lineTo(iCodeX, iCodeY + iCodeHeight);
        iCodePath.lineTo(iCodeX, iCodeY + iCodeHeight - gap);
        //draw凹凸
        drawPartCircle(new PointF(iCodeX, iCodeY + iCodeHeight - gap),
                new PointF(iCodeX, iCodeY + iCodeHeight - gap * 2),
                iCodePath, mRandom.nextBoolean());
        iCodePath.close();
    }

    void createMask() {
        mMaskBitmap = getMaskBitmap(((BitmapDrawable) getDrawable()).getBitmap(), iCodePath);
        mMaskShadowBitmap = mMaskBitmap.extractAlpha();
        iCodeOffsetX = 0;
        isDrawMask = true;
    }

    //抠图
    private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {
        //以控件宽高 create一块bitmap
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        //把创建的bitmap作为画板
        Canvas mCanvas = new Canvas(tempBitmap);
        //有锯齿 且无法解决,所以换成XFermode的方法做
        //mCanvas.clipPath(mask);
        // 抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //绘制用于遮罩的圆形
        mCanvas.drawPath(mask, mMaskPaint);
        //设置遮罩模式(图像混合模式)
        mMaskPaint.setXfermode(Xfermode);
        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap, getImageMatrix(), mMaskPaint);
        mMaskPaint.setXfermode(null);
        return tempBitmap;
    }

    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }


        }
    }

    public interface OnICodeMatchCallback {
        void onMatchSuccess(MHICodeView1 view);

        void onMatchFail(MHICodeView1 view);
    }
}
