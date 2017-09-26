package com.mh.customviews.Banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mh.customviews.R;
import com.mh.customviews.Uitls.WeakHandler;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 * <declare-styleable name="MHBanner">
 * <attr name="banner_point_visibility" format="boolean"/>
 * <attr name="banner_point_selected" format="reference"/>
 * <attr name="banner_point_normal"  format="reference"/>
 * </declare-styleable>
 */

public class MHBanner extends RelativeLayout {

    private boolean autoLoopFlag = false;  //是否循环
    private IMHBannerImageLoaderListener loaderListener;//轮播图的数据监听，点击和加载
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWP = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private ViewPager mViewPager;//viewpager
    private List<MHBannerData> mData;//轮播图的数据
    private Drawable mIndicatorNormal;//正常的指示器图片
    private Drawable mIndicatorSelect;//选中的指示器图片
    private int mDataLength = 0;//数据长度
    private int mCurrentPosition = 0;//当前位置初始为0
    private static final int WHAT_AUTO_PLAY = 3000;//默认msg的what为3000
    private static final int DELAY_TIME = 5000;//跳转时间
    private boolean isIndicatorVisible = true;
    private LinearLayout indicatorLL;
    private IMHBannerVH holder;
    private float mMargin = 0;
    private boolean isPlaying = false;
    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                mCurrentPosition = mViewPager.getCurrentItem();
                mCurrentPosition++;
                mViewPager.setCurrentItem(mCurrentPosition % mDataLength == 0 ? mDataLength : mCurrentPosition % mDataLength);

                System.out.println("lai————————le" + mCurrentPosition);
                handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_TIME);
            }
            return true;
        }
    });

    private ViewPager.OnPageChangeListener mOnPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position % (mDataLength);
            switchToPoint(toRealPosition(mCurrentPosition));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int current = mViewPager.getCurrentItem();
                int lastReal = mViewPager.getAdapter().getCount() - 2;
                System.out.println("------" + current + "________" + lastReal);
                if (current == 0) {
                    mViewPager.setCurrentItem(lastReal, false);
                } else if (current == lastReal + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
            }
        }
    };

    public MHBanner(Context context) {
        this(context, null);
    }

    public MHBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        //关闭view的OverScroll
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayout(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MHBanner);
        isIndicatorVisible = a.getBoolean(R.styleable.MHBanner_banner_point_visibility, true);
        mIndicatorNormal = a.getDrawable(R.styleable.MHBanner_banner_point_normal);
        mIndicatorSelect = a.getDrawable(R.styleable.MHBanner_banner_point_selected);
        autoLoopFlag = a.getBoolean(R.styleable.MHBanner_banner_loop, false);
        mMargin = a.getDimension(R.styleable.MHBanner_banner_margin, 0);
        a.recycle();
    }


    private void setLayout(Context context) {
        initIndicatorDrawable(context);
        setClipChildren(false);// 绘制是否在padding内
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initVP(context);
        initViewPagerScroll();
        initIndicatorArea(context);
    }


    //    初始化指示器的drawable
    private void initIndicatorDrawable(Context context) {
        if (mIndicatorSelect == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIndicatorSelect = context.getResources().getDrawable(R.drawable.mh_banner_indcator_select, null);
            } else
                mIndicatorSelect = context.getResources().getDrawable(R.drawable.mh_banner_indcator_select);
        if (mIndicatorNormal == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIndicatorNormal = context.getResources().getDrawable(R.drawable.mh_banner_indcator_normal, null);
            } else
                mIndicatorNormal = context.getResources().getDrawable(R.drawable.mh_banner_indcator_normal);
    }

    private void initVP(Context context) {
        mViewPager = new MHBannerVP(context);
        mViewPager.setOffscreenPageLimit(4);
//        mViewPager.setPageMargin((int) mMargin);
        mViewPager.setClipChildren(true);
        RelativeLayout.LayoutParams lp = new LayoutParams(RMP, RMP);
        lp.leftMargin = (int) mMargin;
        lp.rightMargin = (int) mMargin;
        addView(mViewPager, lp);
    }


    private void initIndicatorArea(Context context) {
        indicatorLL = new LinearLayout(context);
        indicatorLL.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout indicatorArea = new RelativeLayout(context);
        indicatorArea.setPadding(0, 10, 0, 10);
        LayoutParams lp = new LayoutParams(RMP, RWP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(indicatorArea, lp);
        LayoutParams lp1 = new LayoutParams(RWP, RWP);
        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorArea.addView(indicatorLL, lp1);
        if (isIndicatorVisible)
            indicatorArea.setVisibility(VISIBLE);
        else
            indicatorArea.setVisibility(GONE);
    }


    public MHBanner setLoaderListener(IMHBannerImageLoaderListener loaderListener) {
        this.loaderListener = loaderListener;
        return this;
    }

    public MHBanner setBannerItem(IMHBannerVH holder) {
        this.holder = holder;
        return this;
    }

    public MHBanner setData(List<MHBannerData> data) {
        mData = data;
        if (mData == null || mData.size() == 0)
            mDataLength = 0;
        else
            mDataLength = mData.size();
        return this;
    }

    public MHBanner setTransform(boolean flag, ViewPager.PageTransformer transformer) {
        mViewPager.setPageTransformer(flag, transformer);
        return this;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void build() {
        if (mData == null || mData.size() == 0)
            return;
        addIndicators();
        MHBannerPagerAdapter adapter = new MHBannerPagerAdapter(mData, loaderListener, holder, autoLoopFlag, mViewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mOnPageChange);
        mViewPager.setCurrentItem(1, false);
        if (mDataLength > 1)
            resume();
    }

    private void addIndicators() {
        indicatorLL.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
        lp.setMargins(10, 10, 10, 10);
        ImageView imageView;
        for (int i = 0; i < mDataLength; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.setBackground(mIndicatorNormal);
            } else
                imageView.setBackgroundDrawable(mIndicatorNormal);
            indicatorLL.addView(imageView);
        }
        switchToPoint(0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoLoopFlag && mDataLength > 1) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pause();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    resume();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 切换指示器
     *
     * @param currentPoint
     */
    private void switchToPoint(final int currentPoint) {
        for (int i = 0; i < indicatorLL.getChildCount(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                indicatorLL.getChildAt(i).setBackground(mIndicatorNormal);
            } else
                indicatorLL.getChildAt(i).setBackgroundDrawable(mIndicatorNormal);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            indicatorLL.getChildAt(currentPoint).setBackground(mIndicatorSelect);
        } else
            indicatorLL.getChildAt(currentPoint).setBackgroundDrawable(mIndicatorSelect);

    }

    public void resume() {
        if (autoLoopFlag && !isPlaying) {
            isPlaying = true;
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_TIME);
        }
    }


    public void pause() {
        if (autoLoopFlag && isPlaying) {
            isPlaying = false;
            handler.removeMessages(WHAT_AUTO_PLAY);
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


    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller mViewPagerScroller = new ViewPagerScroller(
                    mViewPager.getContext());
            mScroller.set(mViewPager, mViewPagerScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
