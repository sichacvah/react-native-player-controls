package io.sichacvah.remotecontrols;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessagingPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       new MessagingHelper((Application) context.getApplicationContext()).sendNotification(intent.getExtras());
    }
}