package io.sichacvah.remotecontrols;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.content.res.Resources;
import android.R;

public class MessagingHelper {
    private Context mContext;
    private static final String TAG = MessagingHelper.class.getSimpleName();
    private static final String PLAY_ACTION = "PLAY_ACTION";
    private static final String PAUSE_ACTION = "PAUSE_ACTION";
    private static final String CLOSE_ACTION = "STOP_ACTION";
    private static final String LIKE_ACTION = "LIKE_ACTION";
    private static final String STAR_ACTION = "STAR_ACTION"

    
    public MessagingHelper(Application context) {
        mContext = context
    }

    public Class getMainActivityClass() {
        String packageName = mContext.getPackageName();
        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = launchIntent.getComponent().getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException.e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Bitmap getDefaultBitmap() {
        Resources res = mContext.getResources();
        String packageName = mContext.getPackageName();
        int largeIconResId = res.getIdentifier("largeIcon", "drawable", packageName);
        Bitmap bitmap = BitmapFactory.decodeResource(res, largeIconResId);
        return  bitmap;         
    }

    public void sendNotification(Bundle bundle) {
        try {
            Class intentClass = getMainActivityClass();
            if (intentClass == null) {
                return;
            }

            if (bundle.getString("body") == null) {
                return;
            }

            int[] args = {0, 1, 2};

            
            Resource res = mContext.getResources();
            NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(args));
            
            if (bundle.getBoolean('isPlaying')) {
                Intent pauseIntent = new Intent(PAUSE_ACTION);
                PendingIntent ppauseIntent = PendingIntent.getBroadcast(mContext, 1, pauseIntent, 0);
                notification.addAction(android.R.drawable.ic_media_pause);
            } else {
                Intent playIntent = new Intent(PLAY_ACTION);
                PendingIntent pplayIntent = PendingIntent.getBroadcast(mContext, 1, playIntent, 0);
                notification.addAction(android.R.drawable.ic_media_play);
            }

            Intent closeIntent = new Intent(CLOSE_ACTION);
            PendingIntent pcloseIntent = PendingIntent.getBroadcast(mContext, 1, closeIntent, 0);
            notification.addAction(android.R.drawable.ic_navigation_close);


            Intent starIntent = new Intent(STAR_ACTION); 
            PendingIntent pstarIntent = PendingIntent.getBroadcast(mContext, 1, starIntent, 0);
            notification.addAction(android.R.drawable.ic_social_star);

           Intent intent = new Intent(mContext, intentClass);
           intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
           intent.putExtra("notification", bundle);
           PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


           notification.setContentIntent(pendingIntent);
           NotificationManagerCompat notificationManager = notificationManager();
           notificationManager.notify(0, notification.build());
        } catch (Exception e) {
            Log.e(TAG, "failed to send local notification", e);
        }
    }

    private NotificationManagerCompat notificationManager() {
        return (NotificationManagerCompat) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}