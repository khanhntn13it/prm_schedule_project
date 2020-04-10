package ntnk.sample.scheduleproject.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.ViewTaskActivity;
import ntnk.sample.scheduleproject.entity.Task;

public class NotifTaskReceiver extends BroadcastReceiver {
    /**
     * By MY_RULE: Notification Id ~ task Id in order to delete
     */

    private int notificationId;   //set as TaskId
    private int taskId;
    private String title;
    private String content;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationId = intent.getIntExtra("notificationId", 0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        taskId = intent.getIntExtra("taskId", -1);

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, ViewTaskActivity.class);
        resultIntent.putExtra("taskId", taskId);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //create notification
        Notification notification = createNotification(context, NotifiTaskChannel.CHANNEL_ID, title, content, resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    /**
     * create a notification
     * @param context
     * @param channelId
     * @param title
     * @param content
     * @return
     */
    private Notification createNotification(Context context, String channelId, String title, String content, PendingIntent resultPendingIntent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        return builder.build();
    }
}
