package com.mh.customviews.Drag.control.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.mh.customviews.R;


/**
 * Created by munin on 2018/4/3.
 * 该控件的规则是 5个控件全是等宽等高
 */

public class MHCombineXYButton extends LinearLayout implements IRemoterView {

    private View left, top, right, bottom, center;
    private Paint mPaint;
    private Rect mRect;
    private int leftResId = 0, topResId = 0, rightResId = 0, bottomResId = 0, centerResId = 0;
    private int mhX = -1;
    private int mhY = -1;//mhY与mhX必须都大于0,且成比例
    private boolean edgeFlag = false;
    private View[][] view = new View[3][3];
    private LinearLayout[] parent = new LinearLayout[3];

    public MHCombineXYButton(Context context) {
        this(context, null);
    }

    public MHCombineXYButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHCombineXYButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        ll.weight = 1;
        parent[0] = new LinearLayout(context);
        parent[0].setOrientation(HORIZONTAL);
        this.addView(parent[0], ll);
        LayoutParams ll1 = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        ll1.weight = 1;
        parent[1] = new LinearLayout(context);
        parent[1].setOrientation(HORIZONTAL);
        this.addView(parent[1], ll1);
        LayoutParams ll2 = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        ll2.weight = 1;
        parent[2] = new LinearLayout(context);
        parent[2].setOrientation(HORIZONTAL);
        this.addView(parent[2], ll2);
        LayoutParams ll00 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll00.weight = 1;
        view[0][0] = new View(context);
        parent[0].addView(view[0][0], ll00);
        LayoutParams ll01 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll01.weight = 1;
        top = view[0][1] = new View(context);
        parent[0].addView(view[0][1], ll01);
        LayoutParams ll02 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll02.weight = 1;
        view[0][2] = new View(context);
        parent[0].addView(view[0][2], ll02);
        LayoutParams ll10 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll10.weight = 1;
        left = view[1][0] = new View(context);
        parent[1].addView(view[1][0], ll10);
        LayoutParams ll11 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll11.weight = 1;
        center = view[1][1] = new View(context);
        parent[1].addView(view[1][1], ll11);
        LayoutParams ll12 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll12.weight = 1;
        right = view[1][2] = new View(context);
        parent[1].addView(view[1][2], ll12);
        LayoutParams ll20 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll20.weight = 1;
        view[2][0] = new View(context);
        parent[2].addView(view[2][0], ll20);
        LayoutParams ll21 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll21.weight = 1;
        bottom = view[2][1] = new View(context);
        parent[2].addView(view[2][1], ll21);
        LayoutParams ll22 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        ll22.weight = 1;
        view[2][2] = new View(context);
        parent[2].addView(view[2][2], ll22);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        //1.获得自定义属性值的一个容器
        TypedArray mTd = context.obtainStyledAttributes(attrs, R.styleable.MHCombineXYButton);
        //2.通过属性容器获得属性，如果没有就使用默认值
        leftResId = mTd.getResourceId(R.styleable.MHCombineXYButton_left, 0);
        topResId = mTd.getResourceId(R.styleable.MHCombineXYButton_top, 0);
        rightResId = mTd.getResourceId(R.styleable.MHCombineXYButton_right, 0);
        bottomResId = mTd.getResourceId(R.styleable.MHCombineXYButton_bottom, 0);
        centerResId = mTd.getResourceId(R.styleable.MHCombineXYButton_center, 0);
        edgeFlag = mTd.getBoolean(R.styleable.MHCombineXYButton_edge, false);
        mhX = mTd.getInt(R.styleable.MHCombineXYButton_xy_x, 0);
        mhY = mTd.getInt(R.styleable.MHCombineXYButton_xy_y, 0);
        mTd.recycle();
        build();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width =measure(widthMeasureSpec), height = measure(heightMeasureSpec);
        int size = Math.min(width, height);
        Log.e("TAG", "setControllerViews: 触发了" + size);
        if (mhY <= 0 || mhX <= 0) {
            setMeasuredDimension(size, size);
        } else {
            setMeasuredDimension(width, width * mhY / mhX);
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        int radius = getWidth() / 2;
        if (edgeFlag) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.parseColor("#cdcdcd"));
            canvas.drawCircle(radius, radius, radius - 1, mPaint);
        }
        super.onDraw(canvas);
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


    @Override
    public View leftController() {
        return left;
    }

    @Override
    public void setLeftController(View view) {
        left = view;
    }

    @Override
    public View rightController() {
        return right;
    }

    @Override
    public void setRightController(View view) {
        right = view;
    }

    @Override
    public View topController() {
        return top;
    }

    @Override
    public void setTopController(View view) {
        top = view;
    }

    @Override
    public View bottomController() {
        return bottom;
    }

    @Override
    public void setBottomController(View view) {
        bottom = view;
    }

    @Override
    public View centerController() {
        return center;
    }

    @Override
    public void setCenterController(View view) {
        center = view;
    }

    @Override
    public int viewType() {
        return COMBINATION;
    }

    @Override
    public View[] controlViews() {
        return new View[]{left, top, right, bottom, center};
    }

    @Override
    public void setControllerViews(int left, int top, int right, int bottom, int center) {
        Log.e("TAG", "setControllerViews: " + left);
        leftResId = left;
        topResId = top;
        rightResId = right;
        bottomResId = bottom;
        centerResId = center;
        build();
    }

    @Override
    public IRemoterView build() {
        parent[1].setVisibility(VISIBLE);
        parent[0].setVisibility(VISIBLE);
        parent[2].setVisibility(VISIBLE);
        view[0][0].setVisibility(VISIBLE);
        view[1][0].setVisibility(VISIBLE);
        view[2][0].setVisibility(VISIBLE);
        view[0][2].setVisibility(VISIBLE);
        view[1][2].setVisibility(VISIBLE);
        view[2][2].setVisibility(VISIBLE);

        if (topResId == 0)
            parent[0].setVisibility(GONE);
        if (bottomResId == 0)
            parent[2].setVisibility(GONE);
        if (leftResId == 0) {
            view[0][0].setVisibility(GONE);
            view[1][0].setVisibility(GONE);
            view[2][0].setVisibility(GONE);
        }
        if (rightResId == 0) {
            view[0][2].setVisibility(GONE);
            view[1][2].setVisibility(GONE);
            view[2][2].setVisibility(GONE);
        }
        if (leftResId == 0 && rightResId == 0 && centerResId == 0)
            parent[1].setVisibility(GONE);

        top.setBackgroundResource(topResId);
        left.setBackgroundResource(leftResId);
        center.setBackgroundResource(centerResId);
        right.setBackgroundResource(rightResId);
        bottom.setBackgroundResource(bottomResId);
//        for(int i=0;i<3;i++){
//            for(int j=0;j<3;j++){
//                Log.e("TAG", "Visible "+i+" "+j+" "+view[i][j].getVisibility());
//            }
//        }

        return this;
    }

    public void setmRect(Rect mRect) {
        this.mRect = mRect;
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                // 子容器可以是声明大小内的任意大小
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                // 父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                // 父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
                result = dp2px(300);
                break;
            default:
                break;
        }
        return result;
    }
    private int dp2px(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float density = displayMetrics.scaledDensity;
        return (int) (dp * density + 0.5f);
    }
}
