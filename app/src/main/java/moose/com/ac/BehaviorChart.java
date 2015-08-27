package moose.com.ac;

import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;

import moose.com.ac.util.CommonUtil;

/**
 * Created by dell on 2015/8/27.
 * BehaviorChart
 */
public class BehaviorChart extends AppCompatActivity {
    private static final String TAG = "BehaviorChart";

    /**
     * Third chart
     */
    private LineChartView mChart;
    private ImageButton mPlayThree;
    private boolean mUpdate;
    private  String[] mLabelsThree;
    private final float[][] mValuesThree = {{4.5f, 5.7f, 4f, 8f, 2.5f, 3f, 6.5f},
            {1.5f, 2.5f, 1.5f, 5f, 5.5f, 5.5f, 3f},
            {8f, 7.5f, 7.8f, 1.5f, 8f, 8f, .5f}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        // Init  chart
        mLabelsThree = CommonUtil.getDays();
        mUpdate = true;
        mChart = (LineChartView) findViewById(R.id.linechart3);
        mPlayThree = (ImageButton) findViewById(R.id.play3);
        mPlayThree.setOnClickListener(v -> {
            if (mUpdate)
                updateChart(mChart, mPlayThree);
            else
                dismissChart(mChart, mPlayThree);
            mUpdate = !mUpdate;
        });
        showChart(mChart, mPlayThree);
    }

    /**
     * Show a CardView chart
     *
     * @param chart Chart view
     * @param btn   Play button
     */
    private void showChart(final LineChartView chart, final ImageButton btn) {
        dismissPlay(btn);
        Runnable action = () -> new Handler().postDelayed(() -> showPlay(btn), 500);


        produceThree(chart, action);
    }

    /**
     * Update the values of a CardView chart
     *
     * @param chart Chart view
     * @param btn   Play button
     */
    private void updateChart(final LineChartView chart, ImageButton btn) {

        dismissPlay(btn);

        updateThree(chart);
    }


    /**
     * Dismiss a CardView chart
     *
     * @param chart Chart view
     * @param btn   Play button
     */
    private void dismissChart(final LineChartView chart, final ImageButton btn) {

        dismissPlay(btn);

        Runnable action = () -> new Handler().postDelayed(() -> {
            showPlay(btn);
            showChart(chart, btn);
        }, 500);


        dismissThree(chart, action);
    }

    /**
     * Show CardView play button
     *
     * @param btn Play button
     */
    private void showPlay(ImageButton btn) {
        btn.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(1).scaleX(1).scaleY(1);
        else
            btn.setVisibility(View.VISIBLE);
    }


    /**
     * Dismiss CardView play button
     *
     * @param btn Play button
     */
    private void dismissPlay(ImageButton btn) {
        btn.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(0).scaleX(0).scaleY(0);
        else
            btn.setVisibility(View.INVISIBLE);
    }


    /**
     * Chart 3
     */

    public void produceThree(LineChartView chart, Runnable action) {

        Tooltip tip = new Tooltip(this, R.layout.linechart_three_tooltip, R.id.value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));

            tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f));
        }

        chart.setTooltips(tip);

        LineSet dataset = new LineSet(mLabelsThree, mValuesThree[0]);
        dataset.setColor(Color.parseColor("#FF58C674"))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF58C674"))
                .setDotsColor(Color.parseColor("#eef1f6"));
        chart.addData(dataset);

        dataset = new LineSet(mLabelsThree, mValuesThree[1]);
        dataset.setColor(Color.parseColor("#FFA03436"))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FFA03436"))
                .setDotsColor(Color.parseColor("#eef1f6"));
        chart.addData(dataset);

        dataset = new LineSet(mLabelsThree, mValuesThree[2]);
        dataset.setColor(Color.parseColor("#FF365EAF"))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF365EAF"))
                .setDotsColor(Color.parseColor("#eef1f6"));
        chart.addData(dataset);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#308E9196"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));

        chart.setBorderSpacing(1)
                .setAxisBorderValues(0, 10, 1)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#FF8E9196"))
                .setXAxis(false)
                .setYAxis(false)
                .setStep(2)
                .setBorderSpacing(Tools.fromDpToPx(5))
                .setGrid(ChartView.GridType.VERTICAL, gridPaint);

        Animation anim = new Animation().setEndAction(action);

        chart.show(anim);
    }

    public void updateThree(LineChartView chart) {
        chart.dismissAllTooltips();
        chart.updateValues(0, mValuesThree[2]);
        chart.updateValues(1, mValuesThree[0]);
        chart.updateValues(2, mValuesThree[1]);
        chart.notifyDataUpdate();
    }

    public static void dismissThree(LineChartView chart, Runnable action) {
        chart.dismissAllTooltips();
        chart.dismiss(new Animation().setEndAction(action));
    }
}
