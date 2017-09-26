package com.mh.customviews.Banner;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface IMHBannerImageLoaderListener {

    void onLoadImage(MHBannerData data ,ImageView imgView,View parentView);

    void onBannerClick(int position,MHBannerData data ,ImageView imgView,View parentView);
}
