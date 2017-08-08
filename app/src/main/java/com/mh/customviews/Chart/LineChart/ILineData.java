package com.mh.customviews.Chart.LineChart;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface ILineData {

    float getX();

    float getY();

    float getXValue();

    float getYValue();

    boolean isVisible();

    int getRange();

    ILineData setX(float x);

    ILineData setY(float y);


}
