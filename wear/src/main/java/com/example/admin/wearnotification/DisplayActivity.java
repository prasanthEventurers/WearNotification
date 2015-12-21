package com.example.admin.wearnotification;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Admin on 12/15/2015.
 */
public class DisplayActivity extends WearableActivity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener{

    private static String wearablePath="/sync_data";
    private DataMap mDataMap;
    private GoogleApiClient mGoogleApiClient;
    private TextView mTextView,mTitleText;
    private  String val="no value changes";
    private  String voiceTranscription ="voice_transcription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        init();
        setUpDefaults();
        setUpEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient,this);
        mGoogleApiClient.disconnect();
    }

    private void init() {

        mTextView = (TextView)findViewById(R.id.change_txt);
        mTitleText = (TextView)findViewById(R.id.title_text);

//**Google Api Client
//        mGoogleApiClient = new GoogleApiClient.Builder(DisplayActivity.this)
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(Bundle bundle) {
//                        Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
//                            @Override
//                            public void onDataChanged(DataEventBuffer dataEventBuffer) {
//
//                                for (DataEvent event:dataEventBuffer)
//                                {
//                                    if (event.getType()==DataEvent.TYPE_CHANGED)
//                                    {
//                                        String path = event.getDataItem().getUri().getPath();
//                                        if (path.equals(wearablePath))
//                                        {
//                                            mDataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
//                                            String firstValue =mDataMap.getString("myFirstData");
//                                            Log.e("DataMap: ",""+mDataMap);
//                                            Log.e("Values: ",""+firstValue);
//                                            mTextView.setText(firstValue);
//                                        }
//                                    }
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onConnectionSuspended(int i) {
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

    private void setUpDefaults() {
//        mTextView.setText(val);
        if (getIntent().hasExtra("data"))
        {
            if (getIntent().hasExtra("change"))
            {
                mTitleText.setText("A change is made");
                updateText(getIntent().getExtras().getString("change"));
            }
            else {
                val = getIntent().getExtras().getString("data");
                Log.e("Value Intent: ", val);
                mTitleText.setText("message Received !!");
                mTextView.setText(val);
            }
        }

    }

    private void setUpEvents() {

    }
    public void update(String values)
    {
        Log.e("upate-value: ", "" + values);
        this.val = values;
        Thread thread=new Thread();
        thread.start();
         mTextView.setText(val);

//        mTextView = (TextView)findViewById(R.id.change_txt);
//        Log.e("upate-value: ", "" + values);
//
//        Log.e("update-val: ",val);
//        mTextView.setText(val);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event:dataEventBuffer)
        {
            if (event.getType()==DataEvent.TYPE_CHANGED)
            {
                Log.e("DataEvent: ","Changed");
                String path = event.getDataItem().getUri().getPath();
                if (wearablePath.equals(path))
                {
                    mDataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    Log.e("DataMap: ", "" + mDataMap);
                    String strSecondData = mDataMap.getString("mySecondData");
                    Log.e("strSecondData: ", strSecondData);
                    getIntent().putExtra("change", strSecondData);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(DisplayActivity.this,"Data Changed",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
            Log.e("connect: ","Success");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("connect: ", "Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("connectionResult: ", " " + connectionResult);
        Log.e("connect: ", "Failed " + connectionResult);
        Dialog dialog=GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), DisplayActivity.this,0);
        dialog.show();
//        Bundle bundle=new Bundle();
//        bundle.putInt("error", connectionResult.getErrorCode());
//        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
//        errorDialogFragment.setArguments(bundle);
//        errorDialogFragment.show(getFragmentManager(),"ERROR DAILOG");

    }

    private void updateText(String text){
        Log.e("Came: ", "updateText");
        mTextView.setText(text);

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e("path :",messageEvent.getPath());
        if (messageEvent.getPath().equals(voiceTranscription)){
            String message = new String(messageEvent.getData());
            getIntent().putExtra("data",message);
            finish();
            startActivity(getIntent());
            Log.e("data :",""+message);
        }
    }
}
