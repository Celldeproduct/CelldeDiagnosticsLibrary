package com.cellde.diagnostics.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView img;

    public ChargingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargingFragment newInstance(String param1, String param2) {
        ChargingFragment fragment = new ChargingFragment();
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
        View view= inflater.inflate(R.layout.fragment_charging, container, false);
        img=view.findViewById(R.id.imgViewChargingPort);
        ((LibActivity) getActivity())
                .setActionBarTitle("Charging");
        registerChargingRecevier();
        return view;
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        boolean stop = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == BatteryManager.BATTERY_PLUGGED_AC
                        || intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == BatteryManager.BATTERY_PLUGGED_USB) {
                    img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green));

                    Toast.makeText(getActivity(), "Test Passed", Toast.LENGTH_SHORT).show();

                    unRegisterCharging();
                    changeScreen();


                }

            } catch (Exception e) {
            }

        }
    };

    public void registerChargingRecevier() {
        if(batteryInfoReceiver!=null)
        {
            unRegisterCharging();
        }

            getActivity().registerReceiver(batteryInfoReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            //  receiverArrayList.add(batteryInfoReceiver);


    }
    public void unRegisterCharging() {
        try {
            getActivity().unregisterReceiver(batteryInfoReceiver);
        } catch (Exception e) {
            e.printStackTrace();
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
                        .replace(R.id.fragment_container_view, DiagnosticCompleteFragment.class, bundle)
                        .commit();
            }
        },1500);


    }

}