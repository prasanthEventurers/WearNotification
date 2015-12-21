package com.example.admin.wearnotification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Admin on 12/8/2015.
 */
public class WearShowActivity extends WearableActivity{
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TextView textView;
    WearableListView mWearableListView;
    String[] mListValues = {"my list 1","my list 2","my list 3","my list 4","my list 5","my list 6","my list 7","my list 8","my list 9","my list 10"};
    GridViewPager gridViewPager;
    DotsPageIndicator mDotsPageIndicator;
    DismissOverlayView mDismissOverlayView;
    GestureDetector mGestureDetector;
    FrameLayout mFrameLayout;
//    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_show_activity);

//**Tried Ambiemt Mode Operation
//        setAmbientEnabled();
//        textView = (TextView)findViewById(R.id.text);
//        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this,MainActivity.class);
//        pendingIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,0);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME,1000,pendingIntent);
//        WatchViewStub watchViewStub = (WatchViewStub)findViewById(R.id.watch_view_stub);
//        watchViewStub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

// ** Card Layout using CardFragment in java
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        CardFragment cardFragment =CardFragment.create("CARD TITLE","Card description");
//        cardFragment.setExpansionEnabled(false);
//        cardFragment.setCardMarginBottom(20);
//        cardFragment.setContentPadding(0, 0, 0, 0);
//        fragmentTransaction.add(R.id.frame_layout, cardFragment);
//        fragmentTransaction.commit();

//**Wearable List View in Android Wear
//        mWearableListView = (WearableListView)findViewById(R.id.wearable_list);
//        WearableAdapter wearableAdapter = new WearableAdapter(this,mListValues);
//        mWearableListView.setAdapter(wearableAdapter);
//        mWearableListView.setClickListener(new WearableListView.ClickListener() {
//            @Override
//            public void onClick(WearableListView.ViewHolder viewHolder) {
//                Integer tag = (Integer) viewHolder.itemView.getTag();
////              Toast.makeText(getApplicationContext(),"item clicked"+tag,Toast.LENGTH_SHORT).show();
//                Intent mainintent = new Intent(WearShowActivity.this,MainActivity.class);
//                Intent confirmIntent = new Intent(WearShowActivity.this,ConfirmationActivity.class);
//                mainintent.putExtra("item",""+(tag+1));
////                confirmIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,ConfirmationActivity.SUCCESS_ANIMATION);
////                confirmIntent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,"list item clicked "+(tag+1));
////                startActivity(confirmIntent);
//                startActivity(mainintent);
//            }
//
//            @Override
//            public void onTopEmptyRegionClick() {
//                Toast.makeText(getApplicationContext()," empty space !!",Toast.LENGTH_SHORT).show();
//            }
//        });


//** 2D Picker
        gridViewPager = (GridViewPager)findViewById(R.id.grid_view);
        gridViewPager.setAdapter(new WearableGridPageAdapter(getApplicationContext(),getFragmentManager()));
        mDotsPageIndicator = (DotsPageIndicator)findViewById(R.id.dot_pager_indicator);
        mDotsPageIndicator.setPager(gridViewPager);

//** longPress to Dismiss the application
        mFrameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        mDismissOverlayView = new DismissOverlayView(this);
        mFrameLayout.addView(mDismissOverlayView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mDismissOverlayView = (DismissOverlayView)findViewById(R.id.dismiss_overlay_button);
//        mDismissOverlayView.setIntroText("Long Press To Exit the Screen is Enabled !!");
//        mDismissOverlayView.showIntroIfNecessary();
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Log.e("Came: ", "onLongPress");
                mDismissOverlayView.show();
            }
        });

//**GoogleApi Client
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(Bundle bundle) {
//                        Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
//                            @Override
//                            public void onDataChanged(DataEventBuffer dataEventBuffer) {
//                              String wearablePath = "/sync_data";
//                                DataMap mDataMap;
//                                String values = null;
//                                Log.e("Came: ", "ondatachanged");
//                                for (DataEvent event:dataEventBuffer)
//                                {
//                                    if (event.getType() == DataEvent.TYPE_CHANGED)
//                                    {
//                                        String path = event.getDataItem().getUri().getPath();
//                                        if (path.equals(wearablePath))
//                                        {
//                                            mDataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
//                                            values = mDataMap.getString("myFirstData");
//                                            Log.e("DATAMAP: ", "" + mDataMap);
//                                            Log.e("Values",""+values);
//                                        }
//                                    }
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onConnectionSuspended(int i) {
//
//                    }
//                })
//                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//                    }
//                })
//                .addApi(Wearable.API)
//                .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mGoogleApiClient.disconnect();
    }

    //**To detect the touch event
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("Came: ","ontouchevent");
//        return mGestureDetector.onTouchEvent(event);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("Came: ","dispatchTouchevent");
        return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }


    //    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

//    @Override
//    public void onEnterAmbient(Bundle ambientDetails) {
//        super.onEnterAmbient(ambientDetails);
//        textView.setTextColor(Color.BLACK);
//    }


}
