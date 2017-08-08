package com.mh.customviews.Chart.LineChart;

/**
 * Created by Administrator on 2017/8/8.
 */

public class LineData implements ILineData {
    private float xValue;
    private float yValue;
    private float x;
    private float y;
    private boolean isVisible;
    private int range;

    public LineData setxValue(float xValue) {
        this.xValue = xValue;
        return this;
    }

    public LineData setyValue(float yValue) {
        this.yValue = yValue;
        return this;
    }

    public LineData setRange(int range) {
        this.range = range;
        return this;
    }

    public LineData setX(float x) {
        this.x = x;
        return this;
    }

    public LineData setY(float y) {
        this.y = y;
        return this;
    }

    public LineData setVisible(boolean visible) {
        isVisible = visible;
        return this;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getXValue() {
        return xValue;
    }

    @Override
    public float getYValue() {
        return yValue;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public int getRange() {
        return range;
    }
}
