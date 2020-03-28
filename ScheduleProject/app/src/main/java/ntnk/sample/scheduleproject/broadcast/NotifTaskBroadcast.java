package ntnk.sample.scheduleproject.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.ViewTaskActivity;
import ntnk.sample.scheduleproject.entity.Task;

public class NotifTaskBroadcast extends BroadcastReceiver {
        int notifiId;   //set as TaskId
        String title;
        String content;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create an explicit intent for an Activity in your app
//        Intent intentOpen = new Intent(ViewTaskActivity.this);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyLemubit")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notifiId, builder.build());
    }
}
