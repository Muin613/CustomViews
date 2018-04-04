package com.mh.customviews;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.mh.customviews.Drag.control.MHRemoterView;
import com.mh.customviews.Drag.control.RemoterTools;
import com.mh.customviews.Drag.control.data.IRemoterTag;
import com.mh.customviews.Drag.control.data.RemoterData;
import com.mh.customviews.Drag.control.data.RemoterTag;
import com.mh.customviews.Drag.control.view.MHCombineXYButton;
import com.mh.customviews.Drag.control.view.MHImageXYButton;

/**
 * Created by munin on 2018/3/30.
 */

public class RemoterAct extends AppCompatActivity {
    private MHImageXYButton btn1, btn3;
    private MHCombineXYButton btn2;
    private MHRemoterView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_remoter);
        final RemoterData data = new RemoterData(new RemoterData.Point(1, 1), 0, R.mipmap.svg_new_close);
        data.setTag(new RemoterTag(true, 0));
        final RemoterData data1 = new RemoterData(new RemoterData.Point(2, 2), 0, R.mipmap.offered_cursor);
        IRemoterTag tag = new RemoterTag(true, 1);
        tag.setBottom("11111111");
        tag.setTop("222222222222");
        tag.setCenter("33333333333");
        tag.setLeft("000000000000");
        tag.setRight("------------------");
//
//         app:bottom="@color/blue"
//        app:center="@color/green"
//        app:left="@color/yellow"
//        app:right="@color/red"
//        app:top="@color/colorAccent"
//
        tag.setRightResId(R.color.red);
        tag.setBottomResId(R.color.blue);
        tag.setLeftResId(R.color.yellow);
        tag.setTopResId(R.color.colorAccent);
        tag.setCenterResId(R.color.green);
        data1.setTag(tag);
        final RemoterData data2 = new RemoterData(new RemoterData.Point(2, 2), 0, R.mipmap.offered_cursor);
        data2.setTag(new RemoterTag(true, 0));
        view = (MHRemoterView) findViewById(R.id.remoter);
        btn1 = (MHImageXYButton) findViewById(R.id.view);
        btn2 = (MHCombineXYButton) findViewById(R.id.view1);
        btn3 = (MHImageXYButton) findViewById(R.id.view2);
        btn1.setTag(data);
        btn2.setTag(data1);
        btn3.setTag(data2);
        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RemoterTools.startDrag(v);
                setDragInfo(data);
                return false;
            }
        });
        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RemoterTools.startDrag(v);
                setDragInfo(data1);
                return false;
            }
        });
        btn3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RemoterTools.startDrag(v);
                setDragInfo(data2);
                return false;
            }
        });
        view.setListener(new MHRemoterView.RemoterObjectClickListener() {
            @Override
            public void onDataClick(Object data) {
                Log.e("数据", "onDataClick: " + data);
            }
        });
    }


    public void setDragInfo(RemoterData mButton) {
        Log.i("TAG", "setDragInfo: " + mButton);
        view.setDragInfo(mButton);
    }
}
