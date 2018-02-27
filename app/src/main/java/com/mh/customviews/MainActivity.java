package com.mh.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.mh.customviews.Chart.LineChart.ILineData;
import com.mh.customviews.Chart.LineChart.LineData;
import com.mh.customviews.Chart.LineChart.MHLineChart;
import com.mh.customviews.Chart.PieChart.IPieData;
import com.mh.customviews.Chart.PieChart.MHPieChart;
import com.mh.customviews.Chart.PieChart.PieData;
import com.mh.customviews.Tab.MHTabItem;
import com.mh.customviews.Tab.MHTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    MHPieChart btn;
    MHLineChart lineChart;
    MHTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        btn = (MHPieChart) findViewById(R.id.text);
//        lineChart = (MHLineChart) findViewById(R.id.lineChart);
//
//       showPieChart();
//showLineChart();

        setContentView(R.layout.act_tablayout);
        tablayout();

    }

    void showLineChart() {
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

    void showPieChart() {
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

    void tablayout() {
        tabLayout = (MHTabLayout) findViewById(R.id.mh_tab_layout);
        MHTabItem item = new MHTabItem(this);
        item.setText("123");
        item.setGravity(Gravity.CENTER);
        item.setBackgroundResource(R.color.colorAccent);
        MHTabItem item1 = new MHTabItem(this);
        item1.setText("456");
        item1.setGravity(Gravity.CENTER);
        item1.setBackgroundResource(R.color.colorAccent);
//        MHTabItem item2 = new MHTabItem(this);
//        item2.setText("789");
//        item2.setGravity(Gravity.CENTER);
//        item2.setBackgroundResource(R.color.colorAccent);
        tabLayout.addTab(item).addTab(item1).setSelectedListener(new MHTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelect(int tab) {
                System.out.println("xuan zhong "+tab);
            }
        });
        tabLayout.build();
    }
}
