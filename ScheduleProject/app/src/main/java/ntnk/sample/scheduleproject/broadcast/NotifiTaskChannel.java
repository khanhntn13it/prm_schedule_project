package ntnk.sample.scheduleproject.broadcast;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

import ntnk.sample.scheduleproject.entity.Task;

public class NotifiTaskChannel {
    /**
     * By MY_RULE: Notification Id ~ task Id in order to delete
     */

    public final static String CHANNEL_ID = "Schedule_CHANNEL_ID";
    public final static String channel_name = "Schedule";
    public final static String channel_description = "Channel for Reminder";

    Context context;

    public NotifiTaskChannel(Context context) {
        this.context = context;
    }

    /**
     * Register your app's notification channel with the system
     */
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name; //(CharSequence) channel_name;//getString(R.string.channel_name);
            String description = channel_description; //getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Set alarm to notify NotificationTaskReceiver
     * @param task : information to notify
     */

    public void setAlarm(Task task){
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendar = (Calendar)calendarNow.clone();
        calendar.setTime(task.getDate());

        //not set alarm for past time
        if(calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < 0){
            return;
        }

        Intent intent = new Intent(context, NotifTaskReceiver.class);
        intent.putExtra("notificationId", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("content", task.getDescription());
        intent.putExtra("taskId", task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    /**
     * Cancel alarm with specify task ~ notification
     * @param task
     */
    public void clearAlarm(Task task){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotifTaskReceiver.class);

        //need id to just delete this alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

}
