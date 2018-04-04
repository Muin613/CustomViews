package com.mh.customviews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by munin on 2018/3/30.
 */

public class DragShadowAct extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_drag);

        ImageView v = (ImageView) findViewById(R.id.image1);
        v.setOnLongClickListener(this);
        ImageView v1 = (ImageView) findViewById(R.id.image2);
        v1.setOnLongClickListener(this);
        ImageView v2 = (ImageView) findViewById(R.id.image3);
        v2.setOnLongClickListener(this);
        v.setOnDragListener(this);
        v1.setOnDragListener(this);
        v2.setOnDragListener(this);

    }


    @SuppressLint("NewApi")
    @Override
    public boolean onLongClick(View v) {
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(null, shadowBuilder, ((ImageView) v).getDrawable(), 0);
        return true;
    }
    float x=0, y=0;
    @Override
    public boolean onDrag(View v, DragEvent event) {
        x+=event.getX();
        y+=event.getY();
        Log.i("onDrag", "onDrag: v " + "" + v.getX() + "  " + v.getY());
        Log.i("onDrag", "onDrag: event " + "" + event.getX() + " " + event.getY());
        Log.i("onDrag", "onDrag: x y " + "" + x + " " + y);
        return true;
    }
}
