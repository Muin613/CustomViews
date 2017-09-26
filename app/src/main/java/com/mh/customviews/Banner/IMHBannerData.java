package com.mh.customviews.Banner;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface IMHBannerData<T> {
    boolean isDrawable();

    String getImgStr();

    int getResId();

    T getRealData();
}
