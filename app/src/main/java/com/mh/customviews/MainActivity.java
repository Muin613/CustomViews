package com.mh.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mh.customviews.Chart.LineChart.ILineData;
import com.mh.customviews.Chart.LineChart.LineData;
import com.mh.customviews.Chart.LineChart.MHLineChart;
import com.mh.customviews.Chart.PieChart.IPieData;
import com.mh.customviews.Chart.PieChart.MHPieChart;
import com.mh.customviews.Chart.PieChart.PieData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MHPieChart btn;
    MHLineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (MHPieChart) findViewById(R.id.text);
        lineChart = (MHLineChart) findViewById(R.id.lineChart);

       showPieChart();
//showLineChart();

    }

    void showLineChart(){
        lineChart.setVisibility(View.VISIBLE);
        Random random = new Random();
        List<ILineData> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LineData data = new LineData();
            data.setxValue(i);
            data.setyValue(random.nextInt(6));
            datas.add(data);
        }

        lineChart.setTotalX(5).setTotalY(6).build().drawLine(datas);
    }
    void showPieChart(){
        btn.setVisibility(View.VISIBLE);
        Random random = new Random();

        List<IPieData> numList = new ArrayList<IPieData>();

        for (int i = 0; i < 5; i++) {
            PieData data = new PieData();
            if (i % 2 == 0)
                data.setColor("#ff" + i + "0" + i / 2 + "f");
            else
                data.setColor("#0ff0" + i + "" + i / 3);
            data.setValue("" + random.nextFloat());
            numList.add(data);
        }
//设置数据和默认动画
        btn.setData(numList, MHPieChart.COUNT);
    }
}
