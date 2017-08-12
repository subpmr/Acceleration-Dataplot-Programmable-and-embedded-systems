package com.example.shubham.accelerometerapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final String TAG = "GraphSensors";
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private GraphView mGraphAccel;
    private LineGraphSeries<DataPoint> mSeriesAccelX, mSeriesAccelY, mSeriesAccelZ;
    private double graphLastAccelXValue = 5d;

    private float X, Y, Z, XYZ;
    private TextView XVal, YVal, ZVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO make this onCreate method not suck and subclass graphs and series
        mGraphAccel = initGraph(R.id.graphAccel, "Sensor.TYPE_ACCELEROMETER");




        // ACCEL
        mSeriesAccelX = initSeries(Color.BLUE, "X");
        mSeriesAccelY = initSeries(Color.RED, "Y");
        mSeriesAccelZ = initSeries(Color.GREEN, "Z");

        mGraphAccel.addSeries(mSeriesAccelX);
        mGraphAccel.addSeries(mSeriesAccelY);
        mGraphAccel.addSeries(mSeriesAccelZ);

        startAccel();

        initializeViews();

        /*
        this.mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        */
    }

    public void initializeViews() {
        XVal = (TextView) findViewById(R.id.XValue);
        YVal = (TextView) findViewById(R.id.YValue);
        ZVal = (TextView) findViewById(R.id.ZValue);

    }

    public GraphView initGraph(int id, String title) {
        GraphView graph = (GraphView) findViewById(id);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);
        graph.setTitle(title);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        return graph;
    }

    public LineGraphSeries<DataPoint> initSeries(int color, String title) {
        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        series.setDrawBackground(false);
        series.setColor(color);
        series.setTitle(title);
        return series;
    }



    public void startAccel(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //event.values[x,y,z]
         if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

             graphLastAccelXValue += 0.15d;
             mSeriesAccelX.appendData(new DataPoint(graphLastAccelXValue, event.values[0]), true, 50);
             mSeriesAccelY.appendData(new DataPoint(graphLastAccelXValue, event.values[1]), true, 50);
             mSeriesAccelZ.appendData(new DataPoint(graphLastAccelXValue, event.values[2]), true, 50);
             X = event.values[0];
             Y = event.values[1];
             Z = event.values[2];
             // Display current Sensor value

             XYZ = X * X + Y * Y + Z * Z;
             XYZ = (float) Math.sqrt(XYZ);


             XVal.setText("" + event.values[0]);
             YVal.setText("" + event.values[1]);
             ZVal.setText("" + event.values[2]);

         }
        else {
             //error
         }
        String dataString = String.valueOf(event.accuracy) + "," + String.valueOf(event.timestamp) + "," + String.valueOf(event.sensor.getType()) + "\n";
        Log.d(TAG, "Data received: " + dataString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged called for the error");
    }


}