package com.mh.customviews.Banner.Transform;

import android.graphics.Camera;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2017/9/26.
 */

public class X3DTransformer implements ViewPager.PageTransformer {

//viewpager 在加上transformer之后把view重新绘制了改变了view的大小

    @Override
    public void transformPage(View page, float position) {
        if (position <-1) {
           page.setAlpha(0);
        } else if (position <1) {
            if(position<0){
                page.setAlpha(1);
//                绕着右边的边线的中点旋转
                page.setPivotX(page.getMeasuredWidth());
                page.setPivotY(page.getMeasuredHeight()/2);
                page.setRotationY(90*position);
            }else{
                page.setAlpha(1);
//                绕着左边的边线的中点旋转
                page.setPivotX(0);
                page.setPivotY(page.getMeasuredHeight()/2);
                page.setRotationY(90*position);
            }
        } else {
            page.setAlpha(0);
        }
    }
}
