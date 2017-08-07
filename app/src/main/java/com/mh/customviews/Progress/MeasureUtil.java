package com.mh.customviews.Progress;

import android.view.View;

/**
 * Created by Administrator on 2017/6/29.
 */

public class MeasureUtil {

    //一方有值就调用

    public static int getEqualWH(int[] data, View view) {
        int widthMode = View.MeasureSpec.getMode(data[0]);
        int widthSize = View.MeasureSpec.getSize(data[0]);
        int heightMode = View.MeasureSpec.getMode(data[1]);
        int heightSize = View.MeasureSpec.getSize(data[1]);
        int width = 0;
        if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {
            width = Math.min(heightSize, widthSize);
        } else if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (heightMode == View.MeasureSpec.EXACTLY) {
            width = heightSize;
        }
        return width;
    }

    public static int getDefaultSize(int measureSpec, int size) {
        int result = size;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
//        精确返回该数值，不精确返回设定的值
        if (specMode == View.MeasureSpec.EXACTLY)
            result = specSize;
        return result;
    }

}
