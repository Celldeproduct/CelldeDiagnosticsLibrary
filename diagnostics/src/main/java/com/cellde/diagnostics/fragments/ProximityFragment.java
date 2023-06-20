package com.cellde.diagnostics.fragments;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cellde.diagnostics.LibActivity;
import com.cellde.diagnostics.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProximityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProximityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PowerManager.WakeLock wakeLock;
    Handler proximityHandler;
    Runnable proximityRunnable;
    PowerManager powerManager;
    DisplayManager dm;
    SensorEventListener proximitySensorListner;
    SensorManager proximitySensorManager;
    private Sensor proximitySensor;
    public static boolean  proximityresult=false;
    ImageView img;


    public ProximityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProximityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProximityFragment newInstance(String param1, String param2) {
        ProximityFragment fragment = new ProximityFragment();
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

        View view=inflater.inflate(R.layout.fragment_proximity, container, false);
        img=view.findViewById(R.id.imgViewProximity);
        ((LibActivity) getActivity())
                .setActionBarTitle("Proximity");
        registerProximityRecevier();
        return view;
    }


    public void registerProximityRecevier(){
        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        dm = (DisplayManager) getActivity().getSystemService(Context.DISPLAY_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
                "lock:proximity_screen_off");
        acquire(getActivity());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(proximitySensorManager!=null)
                        proximitySensorManager.unregisterListener(proximitySensorListner);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                registerProximityRecevier(getActivity());
            }
        },600);
    }

    public void registerProximityRecevier(Context mContext){
        proximitySensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);

        proximitySensor = proximitySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        final int SENSOR_SENSITIVITY = 4;

        if(proximitySensor == null){
        }else {
            proximitySensorManager.registerListener(proximitySensorListner=new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            try {

                                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                                    if (event.values[0] < proximitySensor.getMaximumRange()) {
                                        unregistrProximity();
                                        if (wakeLock != null && wakeLock.isHeld()) {
                                            wakeLock.release();
                                        }
                                        if (!proximityresult ) {
                                            proximityresult = true;
                                            img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green));

                                            Toast.makeText(getActivity(), "Test Passed", Toast.LENGTH_SHORT).show();
                                            unregistrProximity();
                                          changeScreen();
                                        }
                                    }
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onAccuracyChanged(Sensor sensor, int i) {
                        }
                    },proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    public void unregistrProximity() {
        try {
            try {
                proximitySensorManager.unregisterListener(proximitySensorListner);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(proximityHandler!=null) {
                proximityHandler.removeCallbacksAndMessages(null);
            }
            if (wakeLock!=null && wakeLock.isHeld()) {
                wakeLock.release();
            }
            wakeLock=null;
            powerManager=null;
            dm=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    public void acquire(Context context) {
        if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            if (wakeLock!=null && wakeLock.isHeld()) {
                wakeLock.release();
            }
            wakeLock.acquire();
            proximityHandler=  new Handler();
            proximityRunnable=   new Runnable() {
                @Override
                public void run() {
                    proximityHandler.removeCallbacksAndMessages(null);
                    proximityHandler.removeCallbacks(proximityRunnable);
                    if(!displayState()){
                        if(!proximityresult) {
                            proximityresult = true;
                            img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green));

                            Toast.makeText(getActivity(), "Test Passed", Toast.LENGTH_SHORT).show();
                            unregistrProximity();
                          changeScreen();
                        }
                        if (wakeLock!=null && wakeLock.isHeld()) {
                            wakeLock.release();
                        }
                    }else{
                        proximityHandler.postDelayed(proximityRunnable,500);
                    }
                }
            };
            proximityHandler.postDelayed(proximityRunnable,500);
        }
    }
    public Boolean  displayState() {
        try {
            for (int i = 0; i < dm.getDisplays().length; i++) {
                Log.e("displays", String.valueOf(dm.getDisplay(i).getState()));
                if (dm.getDisplay(i).getState() != Display.STATE_OFF) {
                    return true;
                }
            }
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return  true;
        }
    }
    private void changeScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putInt("some_int", 0);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, ChargingFragment.class, bundle)
                        .commit();
            }
        },1500);


    }
}