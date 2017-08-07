package com.mh.customviews.Progress;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mh.customviews.R;


/**
 * Created by mh on 2017/7/7.
 */

public class MHCView extends View {
    private Paint arcPaint, arcPaint1;
    //长度
    private float length;
    //圆弧的颜色
    private int roundColor;
    //圆弧的弧线值
    private float mSweepValue = 0;
    private float textSize;
    //展示的文字
    private String mShowText = "0";

    private Shader shader;

    private RectF mRectF;

    private boolean animOnce = false;
    private boolean once = false;

    public MHCView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1.获得自定义属性值的一个容器
        TypedArray mTd = context.obtainStyledAttributes(attrs, R.styleable.Circle);
        //2.通过属性容器获得属性，如果没有就使用默认值
        roundColor = mTd.getColor(R.styleable.Circle_circle_color, Color.rgb(255, 222, 0));
        textSize = mTd.getDimension(R.styleable.Circle_circle_text_size, 25f);
        //3.释放重复利用
        mTd.recycle();
        initView();
        this.setRotation(-90);

    }


    private void initView() {
        arcPaint = new Paint();
        arcPaint.setStrokeWidth(10);
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(roundColor);
        arcPaint.setStyle(Paint.Style.STROKE);

        arcPaint1 = new Paint();
        arcPaint1.setStrokeWidth(10);//宽度
        arcPaint1.setAntiAlias(true);
        arcPaint1.setColor(Color.rgb(208, 208, 208));
        arcPaint1.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureUtil.getEqualWH(new int[]{widthMeasureSpec, heightMeasureSpec}, MHCView.this);
        if (!once) {
            length = w;
            mRectF = new RectF((float) (length * 0.1), (float) (length * 0.1),
                    (float) (length * 0.9), (float) (length * 0.9));
            once = true;
        }
        setMeasuredDimension(w, w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画弧线
        shader = new SweepGradient(length / 2, length / 2, new int[]{Color.parseColor("#fad961"), Color.parseColor("#f9b148"), Color.parseColor("#f89d3b")}, new float[]{-0.25f, mSweepValue / 720, mSweepValue / 360});
        arcPaint.setShader(shader);
        canvas.drawArc(mRectF, 0, 360, false, arcPaint1);
        canvas.drawArc(mRectF, 0, mSweepValue, false, arcPaint);


    }


    public void setProgress(float mSweepValue) {
        float a = (float) mSweepValue;
        if (a != 0) {
            this.mSweepValue = (float) (360.0 * (a / 100.0));
            mShowText = mSweepValue + "";
            Log.e("this.mSweepValue:", this.mSweepValue + "");
        } else {
            this.mSweepValue = 0;
            mShowText = 0 + "";
        }
    }

    //这是可以动画的
    public void setAnimation(float last, final float current, int length) {
//        if (!animOnce) {
            ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
            progressAnimator.setDuration(length);
            progressAnimator.setTarget(mSweepValue);
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSweepValue = (float) (360.0 * ((float) animation.getAnimatedValue() / 100.0));
                    if (listener != null)
                        listener.onDegreeChange((Float) animation.getAnimatedValue(), current);
                    invalidate();
                }

            });
            progressAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null)
                        listener.onDegreeEnd();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            progressAnimator.start();
//            animOnce=true;
//        }
    }

    private CVDegreeChangeListener listener;

    public void setDegreeListener(CVDegreeChangeListener listener) {
        this.listener = listener;
    }


    public interface CVDegreeChangeListener {
        public void onDegreeChange(float change, float destination);

        void onDegreeEnd();
    }

}