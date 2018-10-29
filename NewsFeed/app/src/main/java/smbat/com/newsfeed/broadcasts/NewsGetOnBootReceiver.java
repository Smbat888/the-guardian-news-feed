package smbat.com.newsfeed.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import smbat.com.newsfeed.services.NewsNotificationService;


public class NewsGetOnBootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NewsNotificationService.class));
    }
}
