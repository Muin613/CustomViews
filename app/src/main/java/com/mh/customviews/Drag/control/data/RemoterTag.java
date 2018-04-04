package com.mh.customviews.Drag.control.data;

import java.io.Serializable;

/**
 * Created by munin on 2018/4/3.
 */

public class RemoterTag implements IRemoterTag, Serializable {

    private boolean edge;
    private Object center, top, left, right, bottom;
    private int type = 0;
    private int leftResId = 0, topResId = 0, rightResId = 0, bottomResId = 0, centerResId = 0;

    public RemoterTag(boolean edge, int type) {
        this.edge = edge;
        this.type = type;
    }

    @Override
    public void setEdge(boolean edge) {
        this.edge = edge;
    }

    @Override
    public void setCenter(Object center) {
        this.center = center;
    }

    @Override
    public void setTop(Object top) {
        this.top = top;
    }

    @Override
    public void setRight(Object right) {
        this.right = right;
    }

    @Override
    public void setLeft(Object left) {
        this.left = left;
    }

    @Override
    public void setBottom(Object bottom) {
        this.bottom = bottom;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean isEdge() {
        return edge;
    }

    @Override
    public Object center() {
        return center;
    }

    @Override
    public Object top() {
        return top;
    }

    @Override
    public Object right() {
        return right;
    }

    @Override
    public Object left() {
        return left;
    }

    @Override
    public Object bottom() {
        return bottom;
    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public int getLeftResId() {
        return leftResId;
    }

    @Override
    public void setLeftResId(int leftResId) {
        this.leftResId = leftResId;
    }

    @Override
    public int getTopResId() {
        return topResId;
    }

    @Override
    public void setTopResId(int topResId) {
        this.topResId = topResId;
    }

    @Override
    public int getRightResId() {
        return rightResId;
    }

    @Override
    public void setRightResId(int rightResId) {
        this.rightResId = rightResId;
    }

    @Override
    public int getBottomResId() {
        return bottomResId;
    }

    @Override
    public void setBottomResId(int bottomResId) {
        this.bottomResId = bottomResId;
    }

    @Override
    public int getCenterResId() {
        return centerResId;
    }

    @Override
    public void setCenterResId(int centerResId) {
        this.centerResId = centerResId;
    }

    @Override
    public String toString() {
        return "RemoterTag{" +
                "edge=" + edge +
                ", center=" + center +
                ", top=" + top +
                ", left=" + left +
                ", right=" + right +
                ", bottom=" + bottom +
                ", type=" + type +
                ", leftResId=" + leftResId +
                ", topResId=" + topResId +
                ", rightResId=" + rightResId +
                ", bottomResId=" + bottomResId +
                ", centerResId=" + centerResId +
                '}';
    }
}
