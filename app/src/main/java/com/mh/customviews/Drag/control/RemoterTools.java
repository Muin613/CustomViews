package com.mh.customviews.Drag.control;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.mh.customviews.Drag.control.data.RemoterData;

/**
 * Created by munin on 2018/3/31.
 */

public class RemoterTools {

    public static void startDrag(View view){
        RemoterData data = (RemoterData) view.getTag();
        if (data == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("data", data);
        ClipData dragData = ClipData.newIntent("value", intent);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
        // 震动反馈，不需要震动权限
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(dragData, myShadow, null, 0);
        }else{
            view.startDrag(dragData, myShadow, null, 0);
        }
    }
}
