package com.mh.customviews.Chart;

/**
 * Created by Administrator on 2017/8/7.
 */

public interface IPieData {

    String getColor();

    String getValue();

    void setCurrentStartAngle(float angle);

    float getCurrentStartAngle();

    void setPercentage(float percentage);

    float getPercentage();

    void setAngle(float angle);

    float getAngle();
}
