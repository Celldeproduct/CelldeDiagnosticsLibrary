package com.cellde.diagnostics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.cellde.diagnostics.fragments.GyroscopeFragment;
import com.cellde.diagnostics.fragments.ProximityFragment;
import com.cellde.diagnostics.fragments.StartFragment;

public class LibActivity extends AppCompatActivity {

    ShakeEventListener mGyroscopeSensorListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        loadFragment();
//        registerPhoneShakeListener();
    }
    private void loadFragment() {

        Bundle bundle = new Bundle();
        bundle.putInt("some_int", 0);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, StartFragment.class, bundle)
                .commit();
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
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}