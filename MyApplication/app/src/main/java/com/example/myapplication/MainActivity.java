package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAccelerometer = findViewById(R.id.btn_accelerometer);
        Button btnGyroscope = findViewById(R.id.btn_gyroscope);
        Button btnMagnetometer = findViewById(R.id.btn_magnetometer);
        Button btnLight = findViewById(R.id.btn_light);

        btnAccelerometer.setOnClickListener(v -> openSensorActivity("accelerometer"));
        btnGyroscope.setOnClickListener(v -> openSensorActivity("gyroscope"));
        btnMagnetometer.setOnClickListener(v -> openSensorActivity("magnetometer"));
        btnLight.setOnClickListener(v -> openSensorActivity("light"));
    }

    private void openSensorActivity(String sensorType) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra("sensor_type", sensorType);
        startActivity(intent);
    }
}
