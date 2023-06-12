package com.cellde.diagnostics;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class LibActivity extends AppCompatActivity {

    ShakeEventListener mGyroscopeSensorListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        registerPhoneShakeListener();
    }

    public void registerPhoneShakeListener() {
        mGyroscopeSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(mGyroscopeSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        mGyroscopeSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {

                Toast.makeText(LibActivity.this,"test pppppppp",Toast.LENGTH_LONG).show();

            }

        });
        // mSensorManager.unregisterListener(mSensorListener);
    }


}