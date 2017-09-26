package com.mh.customviews.Banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MHBannerPagerAdapter extends PagerAdapter {
    private List<View> items = new ArrayList<>();
    private int length = 0;
    private List<MHBannerData> mData;
    private IMHBannerImageLoaderListener listener;
    private IMHBannerVH holder;
    private ViewPager mViewPager;
    private boolean loop = false;

    public MHBannerPagerAdapter(List<MHBannerData> mData, IMHBannerImageLoaderListener listener, IMHBannerVH vh, boolean loop, ViewPager viewPager) {
        this.mData = mData;
        this.listener = listener;
        holder = vh;
        length = mData.size();
        mViewPager = viewPager;
        this.loop = loop;
    }

    @Override
    public int getCount() {
        if (mData.size() == 1)
            return 1;
        return length  +2;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View item = holder.getBannerItemView();
        if (listener != null) {
            listener.onLoadImage(mData.get(toRealPosition(position)), holder.getImageView(), item);
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBannerClick(toRealPosition(position), mData.get(toRealPosition(position)), holder.getImageView(), item);
                }
            }
        });
        container.addView(item, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        return item;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (object != null)
            object = null;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (loop) {
            int position = mViewPager.getCurrentItem();
            if (position == getCount() - 1) {
                position = 0;
                mViewPager.setCurrentItem(position,false);
            }
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return
     */
    private int toRealPosition(int position) {
        int realPosition;
        realPosition = (position - 1) % mData.size();
        if (realPosition < 0)
            realPosition += mData.size();
        return realPosition;
    }
}
