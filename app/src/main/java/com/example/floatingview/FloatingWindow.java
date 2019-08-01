package com.example.floatingview;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatingWindow extends Service {

    private WindowManager wm;
    private LinearLayout ll;
    private Button btnStop;

    public FloatingWindow() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);

        btnStop = new Button(this);
//        btnStop.setOnClickListener();
        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnStop.setText("STOP");
        btnStop.setLayoutParams(btnParameters);


        LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.argb(66, 255, 0, 0));
        ll.setLayoutParams(llParameters);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(500, 400, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        parameters.x = 0;
        parameters.y = 0;
        parameters.gravity = Gravity.CENTER | Gravity.CENTER;

        ll.addView(btnStop);

        wm.addView(ll, parameters);

        ll.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams updateParameters = parameters;
            int x,y;
            float touchedX, touchedY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updateParameters.x;
                        y = updateParameters.y;

                        touchedX = motionEvent.getRawX();
                        touchedY = motionEvent.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateParameters.x = (int) (x + (motionEvent.getRawX()) - touchedX);
                        updateParameters.y = (int) (y + (motionEvent.getRawY()) - touchedY);

                        wm.updateViewLayout(ll, updateParameters);
                    default:
                        break;
                }
                return false;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wm.removeView(ll);
                stopSelf();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
