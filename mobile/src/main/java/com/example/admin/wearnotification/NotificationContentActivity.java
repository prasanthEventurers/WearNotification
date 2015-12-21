package com.example.admin.wearnotification;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;

/**
 * Created by Admin on 12/8/2015.
 */
public class NotificationContentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_content);
        init();
        setUpDefaults();
        setUpEvents();
    }

    private void init() {
        Notification.Builder notification = new Notification.Builder(this);
    }

    private void setUpDefaults() {

    }

    private void setUpEvents() {

    }
}
