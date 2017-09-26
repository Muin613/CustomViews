package com.mh.customviews.Banner;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MHBannerData<Data> implements IMHBannerData<Data> {

    private Data data;
    private boolean isDrawable;
    private String img;
    private int resId;

    @Override
    public boolean isDrawable() {
        return isDrawable;
    }

    @Override
    public String getImgStr() {
        return img;
    }

    @Override
    public int getResId() {
        return resId;
    }

    @Override
    public Data getRealData() {
        return data;
    }

    public MHBannerData setData(Data data) {
        this.data = data;
        return this;
    }


    public MHBannerData setImg(String img) {
        this.img = img;
        isDrawable = false;
        return this;
    }

    public MHBannerData setResId(int resId) {
        this.resId = resId;
        isDrawable = true;
        return this;
    }
}
