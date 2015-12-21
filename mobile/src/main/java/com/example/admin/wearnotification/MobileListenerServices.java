package com.example.admin.wearnotification;

import android.net.sip.SipAudioCall;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;

/**
 * Created by Admin on 12/16/2015.
 */
public class MobileListenerServices implements DataApi.DataListener {
    private String path = "/wearable_path";
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent dataEvent:dataEventBuffer)
        {
            if (dataEvent.getType()== DataEvent.TYPE_CHANGED) {
                if (dataEvent.getDataItem().getUri().getPath().equals(path))
                {

                }
            }
        }
    }
}
