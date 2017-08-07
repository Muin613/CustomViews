package com.mh.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mh.customviews.Chart.IPieData;
import com.mh.customviews.Chart.MHPieChart;
import com.mh.customviews.Chart.PieData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MHPieChart btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (MHPieChart) findViewById(R.id.text);
        List<IPieData> numList = new ArrayList<IPieData>();
        Random random = new Random(9);
        for (int i = 0; i < 5; i++) {
            PieData data = new PieData();
            if (i % 2 == 0)
                data.setColor("#ff"+i+"0"+i/2+"f");
            else
                data.setColor("#0ff0"+i+""+i/3);
            data.setValue("" + random.nextFloat());
            numList.add(data);
        }
//设置数据和默认动画
        btn.setData(numList, MHPieChart.COUNT);


    }
}
