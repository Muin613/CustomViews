package com.mh.customviews.Drag.control.view;

import android.view.View;

/**
 * Created by munin on 2018/4/3.
 * 5个方向的view
 * 左 上 右 下 中间
 */

public interface IRemoterView {

    public static final int COMBINATION = 1;
    public static final int SINGLE = 0;

    View leftController();

    void setLeftController(View view);

    View rightController();

    void setRightController(View view);

    View topController();

    void setTopController(View view);

    View bottomController();

    void setBottomController(View view);

    View centerController();

    void setCenterController(View view);

    int viewType();

    View[] controlViews();

    void setControllerViews(int left, int top, int right, int bottom, int center);

    IRemoterView build();
}
