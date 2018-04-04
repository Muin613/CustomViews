package com.mh.customviews.Drag.control.data;

/**
 * Created by munin on 2018/4/3.
 */

public interface IRemoterTag {

    void setEdge(boolean edge);

    void setCenter(Object center);

    void setTop(Object top);

    void setRight(Object right);

    void setLeft(Object left);

    void setBottom(Object bottom);

    void setType(int type);

    boolean isEdge();


    Object center();

    Object top();

    Object right();

    Object left();

    Object bottom();

    int type();


    public int getLeftResId();

    public void setLeftResId(int leftResId);

    public int getTopResId();

    public void setTopResId(int topResId);

    public int getRightResId() ;

    public void setRightResId(int rightResId);

    public int getBottomResId() ;

    public void setBottomResId(int bottomResId);

    public int getCenterResId();

    public void setCenterResId(int centerResId);
}
