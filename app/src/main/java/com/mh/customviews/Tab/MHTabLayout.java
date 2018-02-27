package com.mh.customviews.Tab;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by munin on 2018/2/27.
 */

public class MHTabLayout extends LinearLayout implements View.OnClickListener {

    private List<MHTabItem> views = new LinkedList<>();
    private List<View> indicatorViews = new LinkedList<>();
    private int defaultPos = 0;
    private int from = 0;
    private int to = 0;
    private ObjectAnimator objAnimator;
    private LinearLayout topView;
    private LinearLayout bottomView;
    private int topBottomRatio = 24;
    OnTabSelectedListener selectedListener;

    public MHTabLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MHTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public MHTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    public MHTabLayout addTab(MHTabItem view) {
        view.setTag(views.size());
        view.setOnClickListener(this);
        if (views.size() == 0) {
            view.setChoose(true);
            View view1 = new View(getContext());
            view1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            indicatorViews.add(view1);
        } else {
            view.setChoose(false);
            View view1 = new View(getContext());
            indicatorViews.add(view1);
        }
        views.add(view);
        return this;
    }

    public MHTabLayout setTopBottomRatio(int topBottomRatio) {
        this.topBottomRatio = topBottomRatio;
        return this;
    }

    public MHTabLayout setSelectedListener(OnTabSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
        return this;
    }

    public void build() {
        if (views.size() == 0)
            return;
        topView = new LinearLayout(getContext());
        LayoutParams LL = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        LL.weight = topBottomRatio;
        LayoutParams LL1 = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        LL1.weight = 1;
        bottomView = new LinearLayout(getContext());
        this.setWeightSum(topBottomRatio + 1);
        topView.setOrientation(HORIZONTAL);
        topView.setGravity(TEXT_ALIGNMENT_CENTER);
        this.addView(topView, LL);
        this.addView(bottomView, LL1);
        topView.setWeightSum(views.size());
        for (MHTabItem item : views) {
            LayoutParams txtL = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            txtL.weight = 1;
            txtL.gravity = Gravity.CENTER;
            topView.addView(item, txtL);
        }
        for (View view : indicatorViews) {
            LayoutParams txtL1 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            txtL1.weight = 1;
            txtL1.gravity = Gravity.CENTER;
            txtL1.rightMargin = 5;
            txtL1.leftMargin = 5;
            bottomView.addView(view, txtL1);
        }
    }


    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        from = defaultPos;
        to = tag;
        objAnimator = ObjectAnimator.ofFloat(bottomView, "TranslationX",  tag * getWidth() / views.size());
        objAnimator.setDuration(200);
        objAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                defaultPos = to;
                views.get(from).setChoose(false);
                views.get(to).setChoose(true);
                if (null != selectedListener)
                    selectedListener.onTabSelect(defaultPos);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objAnimator.start();
    }

    public interface OnTabSelectedListener {
        void onTabSelect(int tab);
    }
}
