package com.mh.customviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mh.customviews.Uitls.TextAutoUtils;


public class TextAutoAct extends AppCompatActivity {
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_auto_txt);
        txt = (TextView) findViewById(R.id.txt);
        TextAutoUtils.adjustTVTextSize(txt, "Hello World!111111111", 15);

    }
}
