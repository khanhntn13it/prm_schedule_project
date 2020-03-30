package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.EditBoardActivity;
import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.entity.Task;

public class TaskAdapter extends BaseAdapter {

    private List<Task> list;
    private Activity activity;

    public TaskAdapter(List<Task> list){
        this.list = list;
    }

    public TaskAdapter(List<Task> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.task_layout, null);
        }

        TextView textViewName = view.findViewById(R.id.plainTextName);
        TextView textViewColor = view.findViewById(R.id.textViewColor);

        final Task task = list.get(position);

        textViewName.setText(task.getTitle());
        int color = R.color.design_default_color_background;
        switch (task.getStatus()){
            case 1:
                color = Integer.valueOf((activity.getResources().getColor(R.color.notyetColor)));
                break;
            case 2:
                color = Integer.valueOf((activity.getResources().getColor(R.color.doingColor)));
                break;
            case 3:
                color = Integer.valueOf((activity.getResources().getColor(R.color.doneColor)));
                break;
        }
        Drawable drawable = activity.getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        textViewColor.setBackground(drawable);

        return view;
    }
}
