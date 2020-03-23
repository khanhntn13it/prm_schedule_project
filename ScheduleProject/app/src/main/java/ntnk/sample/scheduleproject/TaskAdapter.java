package ntnk.sample.scheduleproject;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class TaskAdapter extends ArrayAdapter {
    private List<Task> taskList;
    private Activity activity;
    public TaskAdapter(@NonNull Context context,List<Task> taskList) {
        super(context, 0);
        this.taskList = taskList;

    }
}
