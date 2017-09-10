package com.mh.customviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.mh.customviews.ICode.MHICodeView1;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/9/10.
 */

public class ICodeView1TestAct extends AppCompatActivity {
    MHICodeView1 code;
    SeekBar seek;
    private long time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icodeview1_test);
        code = (MHICodeView1) findViewById(R.id.codeview);
        seek = (SeekBar) findViewById(R.id.seek);
        code.setCallback(new MHICodeView1.OnICodeMatchCallback() {
            @Override
            public void onMatchSuccess(MHICodeView1 view) {
                seek.setEnabled(false);
                Toast.makeText(ICodeView1TestAct.this, "您花"+(System.currentTimeMillis()-time)+"ms解锁成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMatchFail(MHICodeView1 view) {
                view.resetICode();
                seek.setProgress(0);
                time=System.currentTimeMillis();
                Toast.makeText(ICodeView1TestAct.this, "解锁失败", Toast.LENGTH_SHORT).show();
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                code.setCurrentICodeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seek.setMax(code.getMaxMoveValue());
                time=System.currentTimeMillis();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                code.matchICode();
            }
        });

        code.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.sm1));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                code.createICode();
            }
        },1000);

    }

    public void change(View view) {
        code.createICode();
        seek.setEnabled(true);
        seek.setProgress(0);
    }
}
