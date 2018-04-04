package com.mh.customviews.Drag.control;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;

import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mh.customviews.Drag.control.data.IRemoterData;
import com.mh.customviews.Drag.control.data.IRemoterTag;
import com.mh.customviews.Drag.control.data.RemoterData;
import com.mh.customviews.Drag.control.view.IRemoterView;
import com.mh.customviews.Drag.control.view.MHCombineXYButton;
import com.mh.customviews.Drag.control.view.MHImageXYButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by munin on 2018/3/31.
 * 该遥控器是4*7的所以对控件的要求有4*7的变化可以承受28种长宽不一样的控件
 * MHRemoterView是一个画板，作为shadow,外壳而已
 * 在内部的真正显示控件的是在frameLayout
 */

public class MHRemoterView extends FrameLayout implements View.OnDragListener, View.OnClickListener {

    private int noticeTextResid = 0;
    private static String TAG = "TAG";
    private int baseDp = 10;
    /**
     * 绘制遥控器画笔
     */
    private Paint mPhonePaint;

    /**
     * 遥控器返回按键path
     */
    private Path mBackPath;

    /**
     * 遥控器宽度
     */
    private int mPhoneWidth;

    /**
     * 遥控器内容区域高
     */
    private int mPhoneContentHeight;

    /**
     * 遥控器内容区域宽
     */
    private int mPhoneContentWidth;

    /**
     * 遥控器右上角x轴点
     */
    private int startX;

    /**
     * 存放按钮位置
     */
    private List<Rect> mRectList = new ArrayList<>();

    /**
     * 内部拖拽的View
     */
    private View dragView;

    /**
     * 内部拖拽View的位置
     */
    private Rect dragRect;

    /**
     * 内部拖拽是否出界
     */
    private boolean isOut;

    /**
     * 提示投影的位置
     */
    private Rect shadowRect;

    /**
     * 拖拽按钮的信息
     */
    private IRemoterData info;

    /**
     * 文字Rect
     */
    Rect mTextRect = new Rect();

    /**
     * 投影图片的Bitmap
     */
    private Bitmap shadowBitmap;

    /**
     * 临时Rect
     */
    private Rect mRect = new Rect();

    private FrameLayout frameLayout;
    private TextView mTextView;
    private final static int WIDTH_COUNT = 4;
    private final static int HEIGHT_COUNT = 7;
    private final static String BORDER_COLOR = "#70FFFFFF";
    private final static String SOLID_COLOR = "#30FFFFFF";
    private final static String DASHED_COLOR = "#20FFFFFF";
    private final static String CONTENT_COLOR = "#0E000000";
    private DashPathEffect mDashPathEffect = new DashPathEffect(new float[]{10, 10}, 0);
    RemoterObjectClickListener listener;

    public void setListener(RemoterObjectClickListener listener) {
        this.listener = listener;
    }

    public MHRemoterView(Context context) {
        this(context, null);
    }

    public MHRemoterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MHRemoterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setWillNotDraw(false);
        mPhonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPath = new Path();
        // 不使用硬件加速，否则虚线显示不出
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        // 拖拽有效区域
        frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Color.parseColor(CONTENT_COLOR));
        frameLayout.setOnDragListener(this);
        addView(frameLayout);
        // 提示文字
        mTextView = new TextView(context);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setText("长按并拖拽下方按钮到这里");
        LayoutParams fl = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        fl.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(fl);
        mTextView.measure(0, 0);
        addView(mTextView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
        // 遥控器高度为View高度减去上下间隔24dp
        int phoneHeight = getMeasuredHeight() - dp2px(24);
        // 遥控器内容区域高 ：手机高度 - 手机头尾（48dp）- 手机屏幕间距（5dp） * 2）
        mPhoneContentHeight = phoneHeight - dp2px(58);
        // 遥控器内容区域宽 ：手机内容区域高/ 7 * 4（手机内容区域为4：7）
        mPhoneContentWidth = mPhoneContentHeight / HEIGHT_COUNT * WIDTH_COUNT;
        // 遥控器宽度为手机内容区域宽 + 手机屏幕间距 * 2
        mPhoneWidth = mPhoneContentWidth + dp2px(10);
        // 绘制起始点
        startX = (getMeasuredWidth() - mPhoneWidth) / 2;
        Log.e(TAG, "onMeasure: " + mPhoneContentWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        frameLayout.layout(startX, dp2px(36), getMeasuredWidth() - startX, getMeasuredHeight() - dp2px(36));
        if (frameLayout.getChildCount() > 0) {
            for (int i = 0; i < frameLayout.getChildCount(); i++) {
                Rect rect = mRectList.get(i);
                frameLayout.getChildAt(i).layout(rect.left, rect.top, rect.right, rect.bottom);
            }
        }
    }


    private RectF mRectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShell(canvas);
        drawGrid(canvas);
        drawShadow(canvas);

    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // 判断是否是需要接收的数据
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {
                } else {
                    return false;
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                进入隐藏提示（未放开手）
                mTextView.setVisibility(GONE);
                isOut = false;
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                出去在没有控件时候显示提示（未放开手）
                if (frameLayout.getChildCount() == 0) {
                    mTextView.setVisibility(VISIBLE);
                }
                isOut = true;
                shadowRect = null;
                invalidate();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
//                停止拖拽的时候判断控件是否在外面(放开手)
                if (dragView != null && isOut) {
                    mRectList.remove(dragRect);
                    frameLayout.removeView(dragView);
                }
                if (frameLayout.getChildCount() == 0) {
                    mTextView.setVisibility(VISIBLE);
                }
                dragView = null;
                dragRect = null;
                stopDrag();
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                // 停留
                if (dragView != null) {
                    info = (IRemoterData) dragView.getTag();
                }
                if (info == null) {
                    break;
                }
                compute(info.getType(), mRect, event);
                adjust(info.getType(), mRect, event);
                if (isEffectiveArea(mRect) && !isOverlap(mRect)) {
                    shadowRect = mRect;
                } else {
                    shadowRect = null;
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                invalidate();
                break;
            case DragEvent.ACTION_DROP:
//                释放拖动
                if (dragView == null) {
                    final IRemoterData data = (IRemoterData) event.getClipData().getItemAt(0).getIntent().getSerializableExtra("data");
                    if (data != null) {
                        IRemoterTag tag = data.getTag();

                        int type = tag.type();
                        if (type == IRemoterView.SINGLE) {
                            Log.e(TAG, "onDrag: 1");
                            final MHImageXYButton imageView;
                            imageView = new MHImageXYButton(getContext());
                            if (data.getPicResId() != 0) {
                                imageView.setBackgroundResource(data.getPicResId());
                                if (tag.isEdge()) {
                                    imageView.setEdgeFlag(true);
                                }
                            } else if (data.getTextResId() != 0) {
                                imageView.setText(getResources().getString(data.getTextResId()));
                                if (tag.isEdge()) {
                                    imageView.setEdgeFlag(true);
                                }
                            }
                            imageView.setTag(data);
                            final Rect rect = new Rect();
                            imageView.setOnClickListener(this);
                            imageView.setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    RemoterTools.startDrag(imageView);
                                    dragView = imageView;
                                    dragRect = rect;
                                    setDragInfo(data);
                                    imageView.setVisibility(GONE);
                                    return false;
                                }
                            });

                            compute(data.getType(), rect, event);
                            adjust(data.getType(), rect, event);

                            if (isEffectiveArea(rect) && !isOverlap(rect)) {
                                mRectList.add(rect);
                                ViewGroup.LayoutParams lp = new LayoutParams(rect.right - rect.left, rect.bottom - rect.top);
                                frameLayout.addView(imageView, lp);

                            }
                        } else if (type == IRemoterView.COMBINATION) {
                            Log.e(TAG, "onDrag: 2" + tag);
                            final MHCombineXYButton imageView;
                            imageView = new MHCombineXYButton(getContext());
                            Log.e(TAG, "onDrag: 2" + tag.getLeftResId() + tag.getTopResId() + tag.getRightResId() + tag.getBottomResId() + tag.getCenterResId());
                            imageView.setControllerViews(tag.getLeftResId(), tag.getTopResId(), tag.getRightResId(), tag.getBottomResId(), tag.getCenterResId());
                            if (tag.isEdge())
                                imageView.setEdgeFlag(true);
                            else
                                imageView.setEdgeFlag(false);
                            imageView.setMhX(data.getType().x);
                            imageView.setMhX(data.getType().y);
                            imageView.setTag(data);
                            final Rect rect = new Rect();
                            imageView.setOnClickListener(this);
                            imageView.centerController().setTag(tag.center());
                            imageView.topController().setTag(tag.top());
                            imageView.leftController().setTag(tag.left());
                            imageView.rightController().setTag(tag.right());
                            imageView.bottomController().setTag(tag.bottom());
                            imageView.centerController().setOnClickListener(this);
                            imageView.topController().setOnClickListener(this);
                            imageView.leftController().setOnClickListener(this);
                            imageView.rightController().setOnClickListener(this);
                            imageView.bottomController().setOnClickListener(this);
                            imageView.setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    RemoterTools.startDrag(imageView);
                                    dragView = imageView;
                                    dragRect = rect;
                                    setDragInfo(data);
                                    imageView.setVisibility(GONE);
                                    return false;
                                }
                            });

                            compute(data.getType(), rect, event);
                            adjust(data.getType(), rect, event);

                            if (isEffectiveArea(rect) && !isOverlap(rect)) {
                                mRectList.add(rect);
                                ViewGroup.LayoutParams lp = new LayoutParams(rect.right - rect.left, rect.bottom - rect.top);
                                frameLayout.addView(imageView, lp);
                            }

                        }
                    }
                } else {
                    IRemoterData data = (IRemoterData) dragView.getTag();
                    Rect rect = dragRect;
                    compute(data.getType(), rect, event);
                    adjust(data.getType(), rect, event);

                    if (isEffectiveArea(rect) && !isOverlap(rect)) {
                        dragView.setVisibility(VISIBLE);
                    } else {
                        mRectList.remove(dragRect);
                        frameLayout.removeView(dragView);
                    }
                    dragView = null;
                    dragRect = null;
                }
                stopDrag();
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: " + v.getTag());
        if(null!=listener)
            listener.onDataClick(v.getTag());
    }

    private void stopDrag() {
        shadowRect = null;
        info = null;
        invalidate();
    }

    //    画投影
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/

    private void drawShadow(Canvas canvas) {
        if (shadowRect != null) {
            int textResId = info.getTextResId();
            int picResId = info.getPicResId();
            if (textResId == 0 && picResId == 0)
                return;
            mPhonePaint.setStyle(Paint.Style.FILL);
            mPhonePaint.setColor(Color.WHITE);
            shadowRect.left = shadowRect.left + startX;
            shadowRect.right = shadowRect.right + startX;
            shadowRect.top = shadowRect.top + dp2px(36);
            shadowRect.bottom = shadowRect.bottom + dp2px(36);
            drawBitmapOrText(canvas, info);
        }
    }


    private void drawBitmapOrText(Canvas canvas, IRemoterData data) {
        RemoterData.Point p = data.getType();
        int textResId = data.getTextResId();
        int picResId = data.getPicResId();
        int x = p.x;
        int y = p.y;
        if (textResId != 0) {
            int width = shadowRect.right - shadowRect.left;
            String txt = getResources().getString(textResId);
            mPhonePaint.setTextSize(width / 4);
            mPhonePaint.getTextBounds(txt, 0, txt.length(), mTextRect);
            int textHeight = mTextRect.bottom - mTextRect.top;
            int textWidth = mTextRect.right - mTextRect.left;
            canvas.drawText(txt, shadowRect.left + width / 2 - textWidth / 2, shadowRect.top + width / 2 + textHeight / 2, mPhonePaint);
        } else if (picResId != 0) {
            int padding = 0;
            shadowRect.left = shadowRect.left + padding;
            shadowRect.right = shadowRect.right - padding;
            shadowRect.top = shadowRect.top + padding;
            shadowRect.bottom = shadowRect.bottom - padding;
            canvas.drawBitmap(shadowBitmap, null, shadowRect, mPhonePaint);
        }
    }

//      画遥控器外壳
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    private void drawShell(Canvas canvas) {
        mPhonePaint.setColor(Color.parseColor(BORDER_COLOR));
        mPhonePaint.setStyle(Paint.Style.STROKE);
        mPhonePaint.setStrokeWidth(2);
        int i = dp2px(12);
        int i1 = dp2px(18);
        // 绘制遥控器外壳
        mRectF.left = startX;
        mRectF.right = getMeasuredWidth() - startX;
        mRectF.top = i;
        mRectF.bottom = getMeasuredHeight() - i;
        canvas.drawRoundRect(mRectF, i1, i1, mPhonePaint);
        // 绘制遥控器内部上下两条线
        canvas.drawLine(startX, i * 3, getMeasuredWidth() - startX, i * 3, mPhonePaint);
        canvas.drawLine(startX, getMeasuredHeight() - i * 3, getMeasuredWidth() - startX, getMeasuredHeight() - i * 3, mPhonePaint);

    }

    //    画格子
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    private void drawGrid(Canvas canvas) {
        // 绘制网格（4 * 7的田字格）田字格外框为实线，内侧为虚线
        // 遥控器屏幕间距5pd
        int j = dp2px(5);
        // 格子的宽高
        int size = mPhoneContentHeight / HEIGHT_COUNT;

        // 横线
        for (int z = 0; z <= HEIGHT_COUNT; z++) {
            mPhonePaint.setPathEffect(null);
            mPhonePaint.setColor(Color.parseColor(SOLID_COLOR));
            mPhonePaint.setStrokeWidth(1);
            // 实线
            canvas.drawLine(startX + j, dp2px(41) + z * size,
                    getMeasuredWidth() - startX - j, dp2px(41) + z * size, mPhonePaint);
            // 虚线
            if (z != HEIGHT_COUNT) {
                mPhonePaint.setPathEffect(mDashPathEffect);
                mPhonePaint.setColor(Color.parseColor(DASHED_COLOR));
                canvas.drawLine(startX + j, dp2px(41) + z * size + size / 2,
                        getMeasuredWidth() - startX - j, dp2px(41) + z * size + size / 2, mPhonePaint);
            }
        }
        // 竖线
        for (int z = 0; z <= WIDTH_COUNT; z++) {
            mPhonePaint.setPathEffect(null);
            mPhonePaint.setColor(Color.parseColor(SOLID_COLOR));
            mPhonePaint.setStrokeWidth(1);
            // 实线
            canvas.drawLine(startX + j + z * size, dp2px(41),
                    startX + j + z * size, getMeasuredHeight() - dp2px(41), mPhonePaint);
            // 虚线
            if (z != WIDTH_COUNT) {
                mPhonePaint.setPathEffect(mDashPathEffect);
                mPhonePaint.setColor(Color.parseColor(DASHED_COLOR));
                canvas.drawLine(startX + j + z * size + size / 2, dp2px(41),
                        startX + j + z * size + size / 2, getMeasuredHeight() - dp2px(41), mPhonePaint);
            }
        }
    }

    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    /**
     * 计算控件位置
     */
    private void compute(RemoterData.Point type, Rect rect, DragEvent event) {
        int size = mPhoneContentWidth / WIDTH_COUNT;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int px = type.x;
        int py = type.y;
        rect.left = (int) (x - size * (px / 2f));
        rect.top = (int) (y - size * (py / 2f));
        rect.right = (int) (x + size * (px / 2f));
        rect.bottom = (int) (y + size * (py / 2f));
        Log.e(TAG, "compute: " + rect);
    }
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/

    /**
     * 调整控件位置 这里是关键
     * 修正
     */
    private void adjust(RemoterData.Point type, Rect rect, DragEvent event) {
        // 最小单元格宽高
        int size = mPhoneContentWidth / WIDTH_COUNT / 2;

        // 手机屏幕间距
        int padding = 0;
        // 1 * 1方格宽高
        int width = size * 2 - dp2px(10);

        int offsetX = (rect.left - padding) % size;
        if (offsetX < size / 2) {
            rect.left = rect.left + padding - offsetX;
        } else {
            rect.left = rect.left + padding - offsetX + size;
        }

        int offsetY = (rect.top - padding) % size;
        if (offsetY < size / 2) {
            rect.top = rect.top + padding - offsetY;
        } else {
            rect.top = rect.top + padding - offsetY + size;
        }
        rect.left += dp2px(5);
        rect.top += dp2px(5);
        Log.e(TAG, "ji suan1: " + dp2px(10));
        rect.right = (int) (rect.left + size * 2 * (type.x));
        rect.bottom = (int) (rect.top + size * 2 * (type.y));

        Log.e(TAG, "ji suan: " + rect);

        //超出部分修正(超出部分)
        if (rect.right > frameLayout.getWidth() || rect.bottom > frameLayout.getHeight()) {

            int currentX = (int) event.getX();
            int currentY = (int) event.getY();

            int centerX = frameLayout.getWidth() / 2;
            int centerY = frameLayout.getHeight() / 2;

            if (currentX <= centerX && currentY <= centerY) {
                //左上角区域

            } else if (currentX >= centerX && currentY <= centerY) {
                //右上角区域
                rect.left = rect.left - size;
                rect.right = rect.right - size;
            } else if (currentX <= centerX && currentY >= centerY) {
                //左下角区域
                rect.top = rect.top - size;
                rect.bottom = rect.bottom - size;
            } else if (currentX >= centerX && currentY >= centerY) {
                //右下角区域
                if (rect.right > frameLayout.getWidth()) {
                    rect.left = rect.left - size;
                    rect.right = rect.right - size;
                }
                if (rect.bottom > frameLayout.getHeight()) {
                    rect.top = rect.top - size;
                    rect.bottom = rect.bottom - size;
                }
            }
        }

        Log.e(TAG, "adjust: " + rect);
    }
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    /**
     * 判断是否重叠
     */
    private boolean isOverlap(Rect rect) {
        for (Rect mRect : mRectList) {
            if (!isEqual(mRect)) {
                if (isRectOverlap(mRect, rect)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断两Rect是否重叠
     */
    private boolean isRectOverlap(Rect oldRect, Rect newRect) {
        return (oldRect.right > newRect.left &&
                newRect.right > oldRect.left &&
                oldRect.bottom > newRect.top &&
                newRect.bottom > oldRect.top);
    }

    /**
     * 判断与拖拽的Rect是否相等
     */
    private boolean isEqual(Rect rect) {
        if (dragRect == null) {
            return false;
        }

        return (rect.left == dragRect.left &&
                rect.right == dragRect.right &&
                rect.top == dragRect.top &&
                rect.bottom == dragRect.bottom);
    }

    /**
     * 是否在有效区域
     */
    private boolean isEffectiveArea(Rect rect) {
        return rect.left >= 0 && rect.top >= 0 && rect.right >= 0 && rect.bottom >= 0 &&
                rect.right <= frameLayout.getWidth() && rect.bottom <= frameLayout.getHeight();
    }
    //   测量
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                // 子容器可以是声明大小内的任意大小
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                // 父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                // 父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
                result = dp2px(300);
                break;
            default:
                break;
        }
        return result;
    }
    //    dp转px
    /*******************************************************************************************************************************************************/
    /*******************************************************************************************************************************************************/
    private int dp2px(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float density = displayMetrics.scaledDensity;
        return (int) (dp * density + 0.5f);
    }

    /*******************************************************************************************************************************************************/
    /**
     * 设置图片
     * <p>
     * /
     *******************************************************************************************************************************************************/
    public void setDragInfo(IRemoterData info) {
        this.info = info;
        if (info.getPicResId() != 0) {
            if (shadowBitmap != null) {
                shadowBitmap.recycle();
            }
            shadowBitmap = BitmapFactory.decodeResource(getResources(), info.getPicResId());
        }
    }

    public interface RemoterObjectClickListener {
        void onDataClick(Object data);
    }
}
