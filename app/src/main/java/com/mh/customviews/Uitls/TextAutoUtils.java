package com.mh.customviews.Uitls;

import android.os.Build;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by munin on 2017/12/13.
 */

public class TextAutoUtils {


    public static void adjustTVTextSize(final TextView tv, final String text, final float minSize) {
        tv.setText(text);
        tv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            tv.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            tv.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        int availWidth = tv.getWidth();
                        if (availWidth <= 0) {
                            return;
                        }

                        TextPaint textPaintClone = new TextPaint(tv.getPaint());
                        float trySize = textPaintClone.getTextSize();
                        while (textPaintClone.measureText(text) > availWidth) {
                            trySize--;
                            textPaintClone.setTextSize(trySize);
                            if (trySize < 0)
                                break;
                        }
                        if (trySize < minSize)
                            trySize = minSize;
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
                    }
                });

    }
}
