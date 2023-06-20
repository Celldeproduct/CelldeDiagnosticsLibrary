package com.cellde.diagnostics.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.cellde.diagnostics.LibActivity;
import com.cellde.diagnostics.R;
import com.google.android.material.snackbar.Snackbar;


/**
 * Created by Girish on 8/9/2016.
 */

@SuppressLint("ValidFragment")
public class DisplayManualFragment extends Fragment  {
    public static boolean isTestPerform = false;
    View view;
    Context ctx;
    TextView txtManualDeadPixel;
    Button txtManualDeadPixelStart;
    RelativeLayout txtDeadPixelScreen, txtDeadPixelMain;
    Handler handler = null;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;
    boolean isTestRunning = false;
    //DialogInterface dialog;
    boolean isWhitePixel, isBlackPixel;
    LinearLayout layoutField;
    boolean isTestPerforming = false;
    boolean isTestResumedAfter = false;
    CountDownTimer cTimer = null;
    boolean isDialogShown = false;
    boolean isBackOnTestStart = false;
    boolean isTestDone = false;
    boolean isDialogShownOnce = false;
    Button displayPass, displayFail;
    Snackbar snackbar;
    Integer[] colorArray = {R.color.RedColor, R.color.green, R.color.brown,
            R.color.white, R.color.Black};
    int i;
    int count = 0;
    //Handler nextButtonHandler = null;
    private Runnable runnable;
    private Handler handlerColor = new Handler();
    private LinearLayout mLinearLayout;
    private boolean alreadyShow;
    ImageView displayImage;


    public DisplayManualFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            if (view == null) {
                view = inflater.inflate(R.layout.fragment_manual_display, null);
                ((LibActivity) getActivity())
                        .setActionBarTitle("Display");
                ctx = getActivity();
                handler = new Handler();
                // Crashlytics.getInstance().log(FragmentTag.DISPLAY_FRAGMENT.name());

                initViews();
            }
            return view;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Initialize view
     */

    private void initViews() {
        try {
            layoutField = view.findViewById(R.id.ll_display);
            displayFail = view.findViewById(R.id.btnDisplayFail);
            displayPass = view.findViewById(R.id.btnDisplayPass);
            txtManualDeadPixel = (TextView) view.findViewById(R.id.txtManualDeadPixel);
            txtManualDeadPixelStart = (Button) view.findViewById(R.id.txtDeadPixelStart);
            txtDeadPixelScreen = (RelativeLayout) view.findViewById(R.id.txtDeadPixelScreen);
            txtDeadPixelMain = (RelativeLayout) view.findViewById(R.id.relativeLayoutMain);
            displayImage=view.findViewById(R.id.imgd);



            txtManualDeadPixelStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                     fullScreen();
                    count = 0;
                    isBackOnTestStart = false;
                    isTestPerform = true;
                    isTestPerforming = true;
                    isTestRunning = true;
                    // txtDeadPixelMain.setVisibility(View.GONE);
//                    txtManualDeadPixelStart.setVisibility(View.GONE);
//                    txtManualDeadPixel.setVisibility(View.GONE);
                    txtDeadPixelMain.setVisibility(View.GONE);
                    //                    showDialogBoxWhite();
                    // hideShowTitleBar(false);
                    setColors();


                    handlerColor.post(runnable);

                }
            });
        } catch (Exception e) {
        }

    }

    public void setColors() {


        runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    if (count < 5) {

                        txtDeadPixelScreen.setBackgroundColor(getResources().getColor(colorArray[count]));
                        count++;
                        fullScreen();
                        txtDeadPixelScreen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                handlerColor.removeCallbacks(runnable);
                                handlerColor.postDelayed(runnable, 0);
                            }
                        });
                        // if(count<5) {

                        handlerColor.removeCallbacks(runnable);
                        handlerColor.postDelayed(runnable, 3000);
                    } else if (count == 5) {
//                        showTitleBar(true);
//                        exitFullScreen();
                        isBackOnTestStart = true;
                        exitFullScreen();
                        isTestRunning = false;
                        if (!alreadyShow) {
                            layoutField.setVisibility(View.VISIBLE);
//                        mainActivity.onChangeText(R.string.textSkip,true);
                            alreadyShow = true;
                        }
                        displayFail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layoutField.setVisibility(View.INVISIBLE);
                                displayFail.setEnabled(false);
                                displayPass.setEnabled(false);
//                                displayFail.setBackgroundColor(getResources().getColor(R.color.disabledGray_opaque));
//                                displayPass.setBackgroundColor(getResources().getColor(R.color.disabledGray_opaque));

                                try {
                                    Toast.makeText(getActivity(),"Test Failed",Toast.LENGTH_SHORT).show();
                                 changeScreen();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isTestDone = true;

//                                onNextPress();
                            }
                        });

                        displayPass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layoutField.setVisibility(View.INVISIBLE);
                                displayFail.setEnabled(false);
                                displayPass.setEnabled(false);
//                                displayFail.setBackgroundColor(getResources().getColor(R.color.disabledGray_opaque));
//                                displayPass.setBackgroundColor(getResources().getColor(R.color.disabledGray_opaque));

                                try {
                                    Toast.makeText(getActivity(),"Test Passed",Toast.LENGTH_SHORT).show();
                                   changeScreen();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isTestDone = true;

//                                onNextPress();
                            }
                        });


                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }

            }
        };


    }


    /**
     * On resume
     */

    @Override
    public void onResume() {
        try {
//            if (isTestPerforming) {
//            } else {
//                hideShowTitleBar(true);
//            }
            if (isTestRunning) {
//                if (handler != null) {
//
//                    handler.removeCallbacksAndMessages(null);
//                }
//                handlerColor.removeCallbacks(runnable);

                count = 0;
                setColors();


                handlerColor.post(runnable);
            } else if (!isBackOnTestStart) {
                //   fullScreen();
            }
            //            nextButtonHandler = new Handler();
            if (isTestDone) {

//                onNextPress();

            }

        } catch (Exception e) {
        }

        super.onResume();
    }


    /**
     * On Pause
     */
    @Override
    public void onPause() {
        try {
        } catch (Exception e) {
        }

        try {

            if (handlerColor != null) {
                handlerColor.removeCallbacks(runnable);
            }
            if (handler != null) {

                handler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
        }

        super.onPause();
    }


    /**
     * On Detach
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if (handler != null) {

                handler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
        }

    }



    /**
     * onBackPressed
     */
    public void onBackPress() {
        try {
            txtDeadPixelScreen.setOnClickListener(null);

        } catch (Exception e) {
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        exitFullScreen();
    }





    public void fullScreen() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getActivity().getWindow().setAttributes(lp);
        }
    }

    public void exitFullScreen() {

        try {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
//                getActivity().getWindow().setAttributes(lp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
            decorView.invalidate();
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
                        .replace(R.id.fragment_container_view, ProximityFragment.class, bundle)
                        .commit();
            }
        },1500);


    }

}
