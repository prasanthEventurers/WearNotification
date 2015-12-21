package com.example.admin.wearnotification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Admin on 12/15/2015.
 */
public class ListenerService {
//        extends WearableListenerService {
//    private static String wearablePath = "/sync_data";
//    DisplayActivity displayActivity=new DisplayActivity();
//    DataMap mDataMap;
//
//    @Override
//    public void onDataChanged(DataEventBuffer dataEvents) {
//        super.onDataChanged(dataEvents);
//        Log.e("Came: ", "listenerService");
//        for (DataEvent event:dataEvents)
//        {
//            if (event.getType() == DataEvent.TYPE_CHANGED)
//            {
//                String path = event.getDataItem().getUri().getPath();
//                if (path.equals(wearablePath))
//                {
//                    mDataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
//                    String firstValue =mDataMap.getString("myFirstData");
//                    Log.e("DATAMAP: ", "" + mDataMap);
//                    Log.e("Values: ",""+firstValue);
//                    displayActivity.update(firstValue);
//                }
//            }
//        }
//
//    }


}
