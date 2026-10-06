package com.nhom0.scholarshipgateway.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChatSyncService extends Service {
    public ChatSyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}