package com.example.myapplication;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        TextView sensorNameTextView = findViewById(R.id.sensor_name);
        sensorDataTextView = findViewById(R.id.sensor_data);

        String sensorType = getIntent().getStringExtra("sensor_type");
        sensorNameTextView.setText(sensorType);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        switch (sensorType) {
            case "accelerometer":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                break;
            case "gyroscope":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                break;
            case "magnetometer":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                break;
            case "light":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                break;
        }

        if (sensor == null) {
            sensorDataTextView.setText("Sensor not available");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder data = new StringBuilder();
        for (float value : event.values) {
            data.append(value).append("\n");
        }
        sensorDataTextView.setText(data.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for now
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensor != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
