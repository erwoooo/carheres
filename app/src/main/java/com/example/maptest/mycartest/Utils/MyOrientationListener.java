package com.example.maptest.mycartest.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2017/2/25.
 */

public class MyOrientationListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;


    private float lastX;

    public MyOrientationListener(Context context) {
        this.context = context;
    }

    @SuppressWarnings("deprecation")
    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            //获得方向传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @SuppressWarnings(
            {"deprecation"})
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];

            if (Math.abs(x - lastX) > 1.0) {
                if (myOrientationListener != null) {
                    myOrientationListener.onOrientationChanged(x);
                }
            }
            lastX = x;
        }

    }

    private OnOrientationListener myOrientationListener;

    public void setMyOrientationListener(OnOrientationListener myOrientationListener) {
        this.myOrientationListener = myOrientationListener;
    }

    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
