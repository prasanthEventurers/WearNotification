package com.example.admin.wearnotification;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private DelayedConfirmationView delayedConfirmationView;
    private FrameLayout mFrameLayout;
    private DismissOverlayView mDismissOverlayView;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        final String cardClick = getIntent().getExtras().getString("item");
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

//** To Create a DelayedConfirmationView
//                delayedConfirmationView = (DelayedConfirmationView)stub.findViewById(R.id.delayed_confirmation);
//                delayedConfirmationView.setTotalTimeMs(2000);
//                delayedConfirmationView.start();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CardFragment cardFragment = CardFragment.create("CARD TITLE ", "Card Clicked "+cardClick);
                cardFragment.setExpansionEnabled(false);
                cardFragment.setCardMarginBottom(20);
                cardFragment.setContentPadding(0, 0, 0, 0);
                fragmentTransaction.add(R.id.frame_layout_main, cardFragment);
                fragmentTransaction.commit();

//** Dismiss Overlay
                mFrameLayout = (FrameLayout)findViewById(R.id.frame_layout_main);
                mDismissOverlayView = new DismissOverlayView(MainActivity.this);
                mFrameLayout.addView(mDismissOverlayView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mGestureDetector = new GestureDetector(MainActivity.this,new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public void onLongPress(MotionEvent e) {
                        mDismissOverlayView.show();
                        finish();
                    }
                });

            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return  mGestureDetector.onTouchEvent(ev)||super.dispatchTouchEvent(ev);
    }
}
