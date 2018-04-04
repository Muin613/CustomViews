package com.mh.customviews.Drag.control.data;


import java.io.Serializable;

/**
 * Created by munin on 2018/3/31.
 */

public class RemoterData implements IRemoterData, Serializable {
    private Point type;
    private int textResId = 0;
    private int picResId = 0;
    private IRemoterTag tag;
    public RemoterData() {
    }

    public RemoterData(Point type, int textResId, int picResId, IRemoterTag tag) {
        this.type = type;
        this.textResId = textResId;
        this.picResId = picResId;
        this.tag = tag;
    }

    public RemoterData(Point type, int textResId, int picResId) {
        this.type = type;
        this.textResId = textResId;
        this.picResId = picResId;
    }



    public void setType(Point type) {
        this.type = type;
    }

    public void setTextResId(int textResId) {
        this.textResId = textResId;
    }

    public void setPicResId(int picResId) {
        this.picResId = picResId;
    }

    public void setTag(IRemoterTag tag) {
        this.tag = tag;
    }

    @Override
    public Point getType() {
        return type;
    }

    @Override
    public int getTextResId() {
        return textResId;
    }


    @Override
    public int getPicResId() {
        return picResId;
    }

    @Override
    public IRemoterTag getTag() {
        return tag;
    }


    public static class Point implements Serializable {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RemoterData{" +
                "type=" + type +
                ", textResId=" + textResId +
                ", picResId=" + picResId +
                ", tag=" + tag +
                '}';
    }
}
