package com.mh.customviews.Chart.PieChart;

/**
 * Created by mh on 2017/8/7.
 */

public class PieData implements IPieData {

    private String color;
    private String value;
    private float angle;
    private float percent;
    private float startAngle;

    public void setColor(String color) {
        this.color = color;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setCurrentStartAngle(float angle) {
        startAngle = angle;
    }

    @Override
    public float getCurrentStartAngle() {
        return startAngle;
    }

    @Override
    public void setPercentage(float percentage) {
        percent = percentage;
    }

    @Override
    public float getPercentage() {
        return percent;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public float getAngle() {
        return angle;
    }
}
