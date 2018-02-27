package com.mh.customviews.Tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by munin on 2018/2/27.
 */

public class MHTabItem extends TextView {

    private int colorResidChoose = android.R.color.black;
    private int colorResidUnChoose = android.R.color.darker_gray;
    private boolean choose = false;

    public MHTabItem(Context context) {
        super(context);
    }

    public MHTabItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MHTabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MHTabItem setColorResidChoose(int colorResidChoose) {
        this.colorResidChoose = colorResidChoose;
        return this;
    }

    public MHTabItem setColorResidUnChoose(int colorResidUnChoose) {
        this.colorResidUnChoose = colorResidUnChoose;
        return this;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
        if (choose)
            this.setTextColor(getResources().getColor(colorResidChoose));
        else
            this.setTextColor(getResources().getColor(colorResidUnChoose));
    }

    public boolean isChoose() {
        return choose;
    }
}
