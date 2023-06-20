package com.cellde.diagnostics.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cellde.diagnostics.LibActivity;
import com.cellde.diagnostics.R;


import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Girish on 8/24/2016.
 */
@SuppressLint("ValidFragment")
public class TouchScreenCanvasManualFragment extends Fragment {
    View view;
    ArrayList<Integer> boxIdArray = new ArrayList<>();
    Context ctx;
    CanvasView canvasView;
    TextView mtxtViewCompleteMsg;
    RelativeLayout mLayout, desRL;
    boolean isDialogEnable = true;
    boolean isPaused = false;
    // Handler nextButtonHandler = null;
    TextView countCanvasManual;
    CountDownTimer cTimer = null;
    boolean isTestPerformed = false;
    boolean test_done = false;
    Button starttouchTV;
    boolean is_instruction = true;
    ImageView touchImage;

    // isTouchEnabled is to enable disable touch when dialog is visible on screen
    boolean isTouchEnabled = true;
    TextView textView;
    private int column_count = 6;
    private int row_count = 10;


    public TouchScreenCanvasManualFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
                view = inflater.inflate(R.layout.fragment_manual_touchscreencanvas, null);
                ((LibActivity) getActivity())
                        .setActionBarTitle("Touch Screen");
                ctx = getActivity();
                //  Crashlytics.getInstance().log(FragmentTag.SCREEN_TOUCH_FRAGMENT.name());
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

            mLayout = (RelativeLayout) view.findViewById(R.id.layoutMain);
            touchImage = view.findViewById(R.id.imgd);
            countCanvasManual = (TextView) view.findViewById(R.id.countCanvasManual);
            starttouchTV = (Button) view.findViewById(R.id.starttouchTV);
            desRL = (RelativeLayout) view.findViewById(R.id.desRL);
            mtxtViewCompleteMsg = (TextView) view.findViewById(R.id.txtViewCompleteMsg);
            //  utils.showToast(ctx, getResources().getString(R.string.txtManualScreenTouchHelp));
            canvasView = new CanvasView(ctx);
            starttouchTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreen();

                    mLayout.setBackgroundColor(getResources().getColor(R.color.dark_grey));


                    is_instruction = false;

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayout.addView(canvasView);
                        }
                    }, 100);
                    desRL.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
        }

    }


    /**
     * onPause
     */
    @Override
    public void onPause() {
        super.onPause();


    }


    public void exitFullScreen() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
                //  getActivity().getWindow().setStatusBarColor(Color.GREEN);

//                getActivity().getWindow().setAttributes(lp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);

        decorView.invalidate();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void fullScreen() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

                getActivity().getWindow().setAttributes(lp);
                // getActivity().getWindow().setStatusBarColor(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        exitFullScreen();
        super.onStop();
    }


    /**
     * This is used to mock unit test on this class
     *
     * @param event
     * @param arrayCount
     * @return
     */
    public boolean mockOnTouchEvent(int event, int arrayCount) {
        try {
            if (event == MotionEvent.ACTION_DOWN) {
                if (arrayCount > 60) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }


    }

    /**
     * Canvas Custom View To draw Rectangles on screen
     */
    public class CanvasView extends View {
        private int statusBarHeight;
        public int width;
        public int height;
        private Path mPath;
        Context context;
        private Paint mPaint;
        private float mX, mY;
        private static final float TOLERANCE = 5;
        Rect rectangle;
        Display display;

        private List<RectangleCanvasModel> rectangles;
        private List<RectangleCanvasModel> touchedRectangles;
        boolean isRefresh = false;
        boolean isDrawingNeeded = false;
        private RectangleCanvasModel currentRectangle;
        private boolean isShowToast = true;
        Handler handler = null;
        AlertDialog.Builder alertDialog;
        AlertDialog alert;


        public CanvasView(Context c) {
            super(c);
            try {
                context = c;
                handler = new Handler();
                alertDialog = new AlertDialog.Builder(getActivity());
                // we set a new Path
                mPath = new Path();
                rectangle = new Rect();

                Window window = getActivity().getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        statusBarHeight = getNavBarHeight(getActivity()) + getStatusBarHeight();
                    } else {
                        statusBarHeight = getNavBarHeight(getActivity());

                    }
                }
                //statusBarHeight = 100;
                // and we set a new Paint with the desired attributes
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(getResources().getColor(R.color.Black));
                mPaint.setStrokeWidth(1f);
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                display = wm.getDefaultDisplay();
                rectangles = new ArrayList<RectangleCanvasModel>();
                touchedRectangles = new ArrayList<RectangleCanvasModel>();
//                showDialog(true);


            } catch (Exception e) {
            }

        }

        // override onSizeChanged
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        // override onDraw
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            try {
                if (isRefresh) {
                    drawFunction(canvas, isRefresh);
                    isRefresh = true;
                } else {
                    drawFunction(canvas, isRefresh);
                }
            } catch (Exception e) {
            }

        }

        private void AddTouchedRectangle(RectangleCanvasModel rectangle) {
            try {
                if (touchedRectangles.size() == 0) {
                    touchedRectangles.add(rectangle);
                } else {
                    boolean found = false;
                    for (int i = 0; i < touchedRectangles.size(); i++) {
                        RectangleCanvasModel item = touchedRectangles.get(i);
                        if (item.isInsideBounds(rectangle.getCoordX(), rectangle.getCoordY())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        touchedRectangles.add(rectangle);
                        textView = new TextView(getActivity());
                        textView.setX(rectangle.getCoordX());
                        textView.setY(rectangle.getCoordY());
                        textView.setShadowLayer(1.5f, -1, 1, Color.LTGRAY);
                        textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.iconcolor));
                        textView.setHeight(rectangle.getDimensionHeight());
                        textView.setWidth(rectangle.getDimensionWidth());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textView.setElevation(100);
                        }
                        mLayout.addView(textView);
                        setAnimationOnTextView(textView);
                    }
                }
            } catch (Exception e) {
            }

        }

        private void drawFunction(Canvas canvas, boolean isRefresh) {
            try {
                if (!isRefresh) {
                    int x = 0;
                    int yIncrement = ((display.getHeight() + statusBarHeight) - 10) / 10;
                    int y = (display.getHeight() + statusBarHeight) / 10;
                    for (int i = 0; i < 9; i++) {
                        canvas.drawLine(x, y, display.getWidth(), y, mPaint);
                        y = y + yIncrement;
                    }
                    int yWidth = 0;
                    int xWidth = (display.getWidth() - 6) / 6;
                    int xIncrement = display.getWidth() / 6;
                    for (int j = 0; j < 6; j++) {
                        canvas.drawLine(xWidth, yWidth, xWidth, (display.getHeight() + statusBarHeight), mPaint);
                        xWidth = xWidth + xIncrement;
                    }
                    rectangles.clear();
                    int yRect = 0;
                    for (int rows = 0; rows < 10; rows++) {
                        int xRect = 0;
                        for (int cols = 0; cols < 6; cols++) {
                            rectangles.add(new RectangleCanvasModel(xRect, yRect, xIncrement, yIncrement));
                            xRect += xIncrement + 1;
                        }
                        yRect += yIncrement + 1;
                    }

                }


                if (isDrawingNeeded) {
                    Paint paint = new Paint();
                    // border
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(1f);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);

                    for (int i = 0; i < touchedRectangles.size(); i++) {
                        RectangleCanvasModel r = touchedRectangles.get(i);
                        canvas.drawRect(r.getCoordX(), r.getCoordY(), (r.getDimensionWidth() + r.getCoordX()) + 1, (r.getDimensionHeight() + r.getCoordY()) + 1, paint);
                    }
                }
                if (touchedRectangles.size() >= 60) {
                    if (isShowToast) {
                        test_done = true;
                        isShowToast = false;
                        isTestPerformed = true;
                        handler.removeCallbacksAndMessages(null);
                        isPaused = true;
                        Toast.makeText(context, "Test Passed", Toast.LENGTH_SHORT).show();

                        changeScreen();





                    }
                }
            } catch (Exception e) {
            }

        }


        // when ACTION_DOWN start touch according to the x,y values
        private void startTouch(float x, float y) {
            try {
                mPath.moveTo(x, y);
                mX = x;
                mY = y;
            } catch (Exception e) {
            }

        }

        // when ACTION_MOVE move touch according to the x,y values
        private void moveTouch(float x, float y) {
            try {
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOLERANCE || dy >= TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
            } catch (Exception e) {
            }

        }

        public void clearCanvas() {
            try {
                mPath.reset();
                invalidate();
            } catch (Exception e) {
            }

        }

        // when ACTION_UP stop touch
        private void upTouch() {
            try {
                mPath.lineTo(mX, mY);
            } catch (Exception e) {
            }

        }

        protected RectangleCanvasModel GetRectangle(int x, int y) {
            try {
                RectangleCanvasModel returnValue = null;
//                for (Iterator<RectangleCanvasModel> sit = rectangles.iterator(); sit.hasNext(); ) {
//                    RectangleCanvasModel current = sit.next();
//                    if (current.isInsideBounds(x, y)) {
//                        returnValue = current;
//                        break;
//                    }
//                }

                for (int i = 0; i < rectangles.size(); i++) {
                    RectangleCanvasModel current = rectangles.get(i);
                    //  RectangleCanvasModel current = sit.next();
                    if (current.isInsideBounds(x, y)) {
                        returnValue = current;

                        // Toast.makeText(getActivity(), "TouchedItem"+i, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return returnValue;
            } catch (Exception e) {
                return null;
            }

        }

        //override the onTouchEvent
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            try {
                if (isTouchEnabled) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    //dtermine if the button is pressed
                    //check if the current rectangle is set
                    //
                    if (event.getButtonState() == MotionEvent.ACTION_DOWN) {
                        if (currentRectangle == null) {
                            currentRectangle = GetRectangle(x, y);
                            AddTouchedRectangle(currentRectangle);
                            isDrawingNeeded = true;
                        } else {
                            RectangleCanvasModel reported = GetRectangle(x, y);
                            if (reported != null) {
                                if (reported.getCoordX() != currentRectangle.getCoordX() || reported.getCoordY() != currentRectangle.getCoordY()) {
                                    //we are in new rectangle-do the stuff here - e.g paint background
                                    /*Toast.makeText(context, "Rectangle Changed: [X - " + reported.coordX + ", Y - " + reported.coordY + "]", Toast.LENGTH_SHORT).show();*/

                                    currentRectangle = reported;
                                    AddTouchedRectangle(reported);
                                    isDrawingNeeded = true;
                                }
                            }
                        }
                    } else {
                        currentRectangle = null;
                    }
                    invalidate();
                }
                return true;
            } catch (Exception e) {
                return false;
            }

        }


        public int getStatusBarHeight() {

            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            return statusBarHeight;
        }

        public int getNavBarHeight(Context context) {
            Point appUsableSize = getAppUsableScreenSize(context);
            Point realScreenSize = getRealScreenSize(context);

// navigation bar on the side
            if (appUsableSize.x < realScreenSize.x) {
                return appUsableSize.y;
// new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
            }

// navigation bar at the bottom
            if (appUsableSize.y < realScreenSize.y) {
                return realScreenSize.y - appUsableSize.y;
// new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
            }

// navigation bar is not present
            return 0;
//new Point();
        }

        public Point getAppUsableScreenSize(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size;
        }

        public  Point getRealScreenSize(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();

            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealSize(size);
            } else if (Build.VERSION.SDK_INT >= 14) {
                try {
                    size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                    size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                }
            }

            return size;
        }

        private boolean isTablet(Context c) {
            return (c.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }

        private void setAnimationOnTextView(final TextView textView) {
            final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            textView.startAnimation(fadeIn);
            textView.startAnimation(fadeOut);
            fadeIn.setDuration(400);
            fadeIn.setFillAfter(true);
            fadeOut.setDuration(400);
            fadeOut.setFillAfter(true);
            fadeOut.setStartOffset(400 + fadeIn.getStartOffset());
        }


        public class RectangleCanvasModel {
            public int getCoordX() {
                return coordX;
            }

            public int getCoordY() {
                return coordY;
            }

            public int getDimensionWidth() {
                return dimensionWidth;
            }

            public int getDimensionHeight() {
                return dimensionHeight;
            }

            private int coordX;
            private int coordY;
            private int dimensionWidth;
            private int dimensionHeight;

            public RectangleCanvasModel(int x, int y, int width, int height) {
                coordX = x;
                coordY = y;
                dimensionWidth = width;
                dimensionHeight = height;
            }

            public boolean isInsideBounds(int x, int y) {
                return ((x >= coordX && x <= coordX + dimensionWidth) && (y >= coordY && y <= coordY + dimensionHeight));
            }
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
                        .replace(R.id.fragment_container_view, DisplayManualFragment.class, bundle)
                        .commit();
            }
        },1500);


    }

}
