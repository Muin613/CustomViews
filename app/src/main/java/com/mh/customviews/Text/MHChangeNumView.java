package com.mh.customviews.Text;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by mh on 2017/7/7.
 */

public class MHChangeNumView extends TextView {


    private static final int STOPPED = 0;
    private static final int RUNNING = 1;
    private int mPlayingState = STOPPED;
    private float toNumber;
    private float fromNumber;
    private long duration = 1000;
    private int numberType = 2;
    private boolean intOnce = false;
    private boolean floatOnec = false;
    private DecimalFormat fnum = new DecimalFormat("##0.00");

    public MHChangeNumView(Context context) {
        super(context);
    }

    public MHChangeNumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MHChangeNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void runFloat() {
        if (!floatOnec) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, toNumber);
            valueAnimator.setDuration(duration);
            valueAnimator
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            setText(fnum.format(Float.parseFloat(valueAnimator
                                    .getAnimatedValue().toString())));
                            if (valueAnimator.getAnimatedFraction() >= 1) {
                                //大于等于1时认为动画运行结束
                                mPlayingState = STOPPED;
                            }
                        }

                    });
            valueAnimator.start();
            floatOnec = true;
        }
    }

    private void runInt() {
        if (!intOnce) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
                    (int) toNumber);
            valueAnimator.setDuration(duration);
            valueAnimator
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            setText(valueAnimator.getAnimatedValue().toString());
                            if (valueAnimator.getAnimatedFraction() >= 1) {
                                //置标志位
                                mPlayingState = STOPPED;

                            }
                        }
                    });
            valueAnimator.start();
            intOnce = false;
        }
    }


    public MHChangeNumView setFloat(float fromNum, float toNum) {
        toNumber = toNum;
        numberType = 2;
        fromNumber = fromNum;
        return this;
    }

    public MHChangeNumView setInteger(int fromNum, int toNum) {
        toNumber = toNum;
        numberType = 1;
        fromNumber = fromNum;
        return this;
    }

    public MHChangeNumView setDuration(long duration) {
        this.duration = duration;
        return this;
    }


}
