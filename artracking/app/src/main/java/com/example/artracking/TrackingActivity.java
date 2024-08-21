package com.example.artracking;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private List<float[]> points = new ArrayList<>();
    private float[] currentPosition = new float[3];
    private float[] velocity = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private long lastTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastTimestamp == 0) {
            lastTimestamp = event.timestamp;
            return;
        }

        float dt = (event.timestamp - lastTimestamp) * 1.0f / 1000000000.0f;
        lastTimestamp = event.timestamp;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // 处理加速度计数据
            for (int i = 0; i < 3; i++) {
                velocity[i] += event.values[i] * dt;
                currentPosition[i] += velocity[i] * dt;
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // 处理陀螺仪数据
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, event.values);
            SensorManager.getOrientation(deltaRotationMatrix, orientation);
            SensorManager.getRotationMatrix(rotationMatrix, null, event.values, orientation);
        }

        points.add(currentPosition.clone());
        drawPoints();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 不处理精度变化
    }

    private void drawPoints() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK); // 清空画布
            for (float[] point : points) {
                float[] rotatedPoint = new float[3];
                SensorManager.getOrientation(rotationMatrix, rotatedPoint);
                canvas.drawPoint(rotatedPoint[0] + surfaceView.getWidth() / 2, rotatedPoint[1] + surfaceView.getHeight() / 2, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
