
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;

public class CombinedChartActivity extends DemoBase {

    private static final int X_AXIS_SHOW_COUNT = 7;
    private static final int itemCount = 16;

    private String[] date = new String[] {
            "8.11", "8.12", "8.13", "8.14", "8.15", "8.16", "8.17","8.18", "8.19", "8.20", "8.21", "8.22", "8.23", "8.24"
    };
    private CombinedChart mChart;
    private static final String TAG = "CombinedChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_combined);

        mChart = (CombinedChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.setScaleEnabled(false); //不允许放大缩小
        mChart.setDrawBorders(false); //不绘制表格边框
        mChart.setExtraBottomOffset(12f); //设置chartView距离底部边距 ps:padding属性无效,只能设置这个
        mChart.setHighlightPerDragEnabled(false); //不允许拖动高亮
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof BarEntry) {
                    BarData barData = mChart.getBarData();
                    int index = barData.getDataSetForEntry(e).getEntryIndex((BarEntry) e);
                    barData.setSelectedIndex(index);
                    Log.d(TAG, "onValueSelected: ");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setLabelCount(9, true); //固定左右Y轴显示数量为9个
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //y轴标签样式
        rightAxis.setTextColor(Color.parseColor("#666666"));
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setXOffset(0f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setLabelCount(9, true); //固定左右Y轴显示数量为9个
        leftAxis.setGridColor(Color.parseColor("#DDDFE2"));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //y轴标签样式
        leftAxis.setTextColor(Color.parseColor("#666666"));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setXOffset(0f);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //设置x轴标签样式和内容
        xAxis.setTextColor(Color.parseColor("#666666"));
        xAxis.setYOffset(10f); //距离x轴10dp
        xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
        xAxis.setCenterAxisLabels(true);

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        data.setValueTypeface(mTfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.5f);

        mChart.setData(data);

        mChart.invalidate();
        // 为了使 柱状图成为可滑动的,将水平方向 放大 2倍 (可滑动关键代码)
        Matrix mMatrix = new Matrix();
        mMatrix.postScale(itemCount / (float)X_AXIS_SHOW_COUNT, 1f);
        mChart.getViewPortHandler().refresh(mMatrix, mChart, false);
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemCount; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.parseColor("#FFBC1C"));
        set.setLineWidth(1.5f);
        set.setCircleColor(Color.WHITE);
        set.setCircleRadius(4.5f);
        set.setCircleColorHole(Color.parseColor("#FFBC1C"));
        set.setCircleHoleRadius(3.5f);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighlightEnabled(false);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int index = 0; index < itemCount; index++) {
            entries1.add(new BarEntry(0, getRandom(25, 25)));

            entries2.add(new BarEntry(0, getRandom(13, 12)));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.parseColor("#28C1AD"));
        set1.setHighLightColor(Color.parseColor("#28C1AD"));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setColor(Color.parseColor("#8190FF"));
        set2.setHighLightColor(Color.parseColor("#8190FF"));
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        //106
        float groupSpace = 0.26f; //34
        float barSpace = 0.13f; // 12
        float barWidth = 0.24f; // 24
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setDrawValues(false);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleLineValues: {
                for (IDataSet set : mChart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : mChart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {

                int rnd = (int) getRandom(mChart.getData().getDataSetCount(), 0);
                mChart.getData().removeDataSet(mChart.getData().getDataSetByIndex(rnd));
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                break;
            }
        }
        return true;
    }
}
