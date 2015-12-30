package com.example.admin.wearnotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.internal.constants.Capability;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.Node;
import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    String first = "6";
    private EditText editText,editMessage;
    private Button btnChange,btnSend;
    private boolean isConnected = false;
    private  String voiceTranscription ="voice_transcription";
    private String bestNode = null;
    private String strMessage = null;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SetUpDefault();
        SetUpEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void SetUpDefault(){
///GoogleApiClient connection
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.e("Connected ", "" + bundle);
                        isConnected = true;
                        checkVoiceTranscription();
                        Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
                            @Override
                            public void onDataChanged(DataEventBuffer dataEventBuffer) {
//                                for (DataEvent event:dataEventBuffer)
//                                {
//
//                                }
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.e("Suspended ",""+i);
                        isConnected = false;
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.e("Failed ",""+connectionResult);
                        isConnected = false;
                    }
                })
                .addApi(Wearable.API)
                .build();
    }

    private void SetUpEvents() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    first = editText.getText().toString();
                    if (isConnected) {
                        String wearablePath = "/sync_data";
                        DataMap mDataMap = new DataMap();
                        mDataMap.putString("myFirstData", "first: " );
                        mDataMap.putString("mySecondData", ""+ first);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.larger_size_image);
                        Asset asset = CreateAssertForBitmap(bitmap);
                        mDataMap.putAsset("asset",asset);
                        Log.e("MobileDataMap: ", "" + mDataMap);
                        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(wearablePath);
                        putDataMapRequest.getDataMap().putAll(mDataMap);
                        Log.e("DataMap: ",""+mDataMap);
                        PutDataRequest mPutDataRequest = putDataMapRequest.asPutDataRequest();
//            DataApi.DataItemResult dataItemResult =
                        Wearable.DataApi.putDataItem(mGoogleApiClient,mPutDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                            @Override
                            public void onResult(DataApi.DataItemResult dataItemResult) {
                                Log.e("RESULT", "Success");
                            }
                        });
//                        new SendToDataLayerThread(wearablePath, mDataMap).start();
                    }
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMessage.getText().toString().isEmpty())
                {
                    Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                            .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            for(com.google.android.gms.wearable.Node node:getConnectedNodesResult.getNodes())
                            {
                                if(node.isNearby())
                                {
                                    Log.e("near_node: ",node.getId());
                                    bestNode = node.getId();
//                                    String message="hi from handheld";
                                    strMessage = editMessage.getText().toString();
                                    if (bestNode!=null)
                                    {
                                        Wearable.MessageApi.sendMessage(mGoogleApiClient,bestNode,voiceTranscription,strMessage.getBytes())
                                                .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                            @Override
                                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                                Log.e("ResultCallBack: ",sendMessageResult.getStatus().toString());
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    });

                }
            }
        });
    }

    private void init() {
        btnSend = (Button)findViewById(R.id.send_btn);
        btnChange = (Button)findViewById(R.id.change_btn);
        editText = (EditText)findViewById(R.id.edt_text);
        editMessage = (EditText)findViewById(R.id.edt_message);

//**Pending intent to perform onclick action in the wear device
        Intent intent = new Intent(this,NotificationContentActivity.class);
        Intent showIntent = new Intent(this,ShowActivity.class);
        PendingIntent showPendingIntent = PendingIntent.getActivity(this, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//**normal Notification with text,title,icon,pending intent
//        NotificationCompat.Builder normalNotification =(NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.wear_image)
//                .setContentTitle("Normal Notification")
//                .setContentText("Text")
//                .setContentIntent(showPendingIntent);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1, normalNotification.build());

//**Big Text Style Notification
//        android.support.v4.app.NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
//        bigTextStyle.bigText("big text notification which should be displayed on the android wear based " +
//                "on the size of the text android wear will show the big text in the android wear device!!");
//        Notification bigTextNotification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.wear_image)
//                .setContentTitle("Big Text Notification")
//                .setStyle(bigTextStyle)
//                .build();
//        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(2,bigTextNotification);

//**InboxStyle in the Notification
//        NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();
//        inboxStyle.addLine("big text notification which should be displayed on the android wear based ")
//                    .addLine("on the size of the text android wear will show the big text in the android wear device!!");
//        Notification inboxStyleNotification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.wear_image)
//                .setContentTitle("Inbox Style Notification")
//                .setStyle(inboxStyle)
//                .build();
//        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(3,inboxStyleNotification);

//**Wearable Only Option
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.wear_image,"Show Activity",showPendingIntent);
        NotificationCompat.WearableExtender wearableExtender =new NotificationCompat.WearableExtender()
                .setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.wear_background))
                .addAction(action)
                .setHintHideIcon(true);
        Notification wearableOnlyNotification =new NotificationCompat.Builder(this)
                .setContentTitle("Wearable only Notification")
                .setContentText("Text")
                .setSmallIcon(R.drawable.wear_image)
                .extend(wearableExtender)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(4,wearableOnlyNotification);


//**Add Pages to  notification
        List<Notification> notificationList = new ArrayList<>();

//        notificationList.add(bigTextNotification);
        Notification notification2 = new NotificationCompat.Builder(this)
                .addAction(action)
                .build();
        notificationList.add(notification2);

//        NotificationCompat.WearableExtender wearableExtender=new NotificationCompat.WearableExtender()
//                .setDisplayIntent(showPendingIntent)
//                .setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.large_icon));

        NotificationCompat.Builder notificationBuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.wear_image)
//              .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.large_icon))
//                .setContentTitle("My Wear Notification ")
//                .setContentText("Ravi liked your post !!")
//                .setContentIntent(pendingIntent)
//                .setSubText("summary")
//                .setGroupSummary(true)
//                .setGroup("groupkey")
                .extend(wearableExtender);
//                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().addPages(notificationList))
//                .extend(new android.support.v4.app.NotificationCompat.WearableExtender().addAction(action).setHintHideIcon(false)
//                        .setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.wear_background)))
//                ;
//                .setStyle(inboxStyle);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1, notificationBuilder.build());

//        NotificationCompat.Builder notificationBuilder2 =(NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.large_icon)
//                .setContentTitle(" Second Notification")
//                .setContentText("My Second Notification Text")
//                .extend(new android.support.v4.app.NotificationCompat.WearableExtender())
//                .setGroup("groupkey")
//                .setSortKey("1");
//
//        NotificationCompat.Builder notificationBuilder3 =(NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.large_icon)
//                .setContentTitle(" third Notification")
//                .setContentText("My third Notification Text")
//                .extend(new android.support.v4.app.NotificationCompat.WearableExtender())
//                .setGroup("groupkey")
//                .setSortKey("2");
//
//
//        notificationManagerCompat.notify(2,notificationBuilder2.build());
//        notificationManagerCompat.notify(3,notificationBuilder3.build());
    }

    //Thread to Send the DataItem
    public class SendToDataLayerThread extends Thread{
        String path;
        DataMap mDataMap;
        public SendToDataLayerThread(String path,DataMap mDataMap)
        {
            this.mDataMap = mDataMap;
            this.path = path;
        }

        @Override
        public void run() {
            super.run();
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
            putDataMapRequest.getDataMap().putAll(mDataMap);
            Log.e("DataMap: ",""+mDataMap);
            PutDataRequest mPutDataRequest = putDataMapRequest.asPutDataRequest();
//            DataApi.DataItemResult dataItemResult =
                    Wearable.DataApi.putDataItem(mGoogleApiClient,mPutDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            Log.e("RESULT","Success");
                        }
                    });
//                            .await();
//            if (dataItemResult.getStatus().isSuccess()){
//                Log.e("RESULT","Success");
//            }
//            else {
//                Log.e("RESULT","failed");
//            }
        }
    }

    //Asserts Creation
    public static Asset CreateAssertForBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        return Asset.createFromBytes(byteArrayOutputStream.toByteArray());
    }

    public void  checkVoiceTranscription(){
        Wearable.CapabilityApi.getCapability(mGoogleApiClient,voiceTranscription,CapabilityApi.FILTER_REACHABLE)
                .setResultCallback(new ResultCallback<CapabilityApi.GetCapabilityResult>() {
                    @Override
                    public void onResult(CapabilityApi.GetCapabilityResult getCapabilityResult) {
                        Log.e("Capability: ", "" + getCapabilityResult.getStatus());
                    }
                });

        CapabilityApi.CapabilityListener capabilityListener = new CapabilityApi.CapabilityListener() {
            @Override
            public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
                Set<com.google.android.gms.wearable.Node> nodes=capabilityInfo.getNodes();
                for (com.google.android.gms.wearable.Node node:nodes)
                {
                    if (node.isNearby())
                    {
                        bestNode = node.getId();
                    }
                }
            }
        };
        Wearable.CapabilityApi.addCapabilityListener(mGoogleApiClient, capabilityListener, voiceTranscription);
    }
}
