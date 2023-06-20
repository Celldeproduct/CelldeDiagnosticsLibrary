package com.cellde.diagnostics.fragments;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cellde.diagnostics.LibActivity;
import com.cellde.diagnostics.R;
import com.cellde.diagnostics.ShakeEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GyroscopeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GyroscopeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ShakeEventListener mGyroscopeSensorListener;
    SensorManager mSensorManager;
    ImageView img;
    public GyroscopeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GyroscopeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GyroscopeFragment newInstance(String param1, String param2) {
        GyroscopeFragment fragment = new GyroscopeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gyroscope, container, false);
        img=view.findViewById(R.id.imgViewPhoneShake);
        ((LibActivity) getActivity())
                .setActionBarTitle("Gyroscope");
        registerPhoneShakeListener();
        return view;
    }


    public void registerPhoneShakeListener() {
        mGyroscopeSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager)getActivity().getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(mGyroscopeSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        mGyroscopeSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green));
                Toast.makeText(getActivity(),"Test Passed",Toast.LENGTH_SHORT).show();
                mSensorManager.unregisterListener(mGyroscopeSensorListener);
              changeScreen();
            }

        });
        // mSensorManager.unregisterListener(mSensorListener);
    }

    private void changeScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putInt("some_int", 0);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, TouchScreenCanvasManualFragment.class, bundle)
                        .commit();
            }
        },1500);


    }

}