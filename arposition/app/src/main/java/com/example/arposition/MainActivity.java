package com.example.arposition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;
    private SensorDataRenderer renderer;
    private float[] accelerometerData = new float[3];
    private float[] gyroscopeData = new float[3];
    private boolean isRunning = false;
    private long lastUpdateTime = 0;
    private float[] currentPosition = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        renderer = new SensorDataRenderer(surfaceView);

        EditText intervalInput = findViewById(R.id.intervalInput);
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intervalText = intervalInput.getText().toString();
                if (isPositiveInteger(intervalText)) {
                    long interval = Long.parseLong(intervalText);
                    isRunning = true;
                    lastUpdateTime = System.currentTimeMillis();
                    sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                    renderer.startRendering(interval);
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isRunning) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerData, 0, accelerometerData.length);
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                System.arraycopy(event.values, 0, gyroscopeData, 0, gyroscopeData.length);
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= renderer.getInterval()) {
                float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
                lastUpdateTime = currentTime;

                // 根据传感器数据计算位置变化
                currentPosition[0] += gyroscopeData[0] * deltaTime;
                currentPosition[1] += gyroscopeData[1] * deltaTime;
                currentPosition[2] += gyroscopeData[2] * deltaTime;

                renderer.addPosition(currentPosition);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 处理精度变化
    }

    private boolean isPositiveInteger(String s) {
        try {
            return Integer.parseInt(s) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRunning) {
            sensorManager.unregisterListener(this);
            renderer.stopRendering();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunning) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
