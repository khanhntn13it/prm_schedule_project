package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.CreateTaskActivity;
import ntnk.sample.scheduleproject.activity.MainActivity;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskGroup;

public class TaskGroupPagerAdapter extends PagerAdapter {
    private List<TaskGroup> taskGroupList;
    private LayoutInflater layoutInflater;
    private AppCompatActivity activity;

    public TaskGroupPagerAdapter(List<TaskGroup> taskGroups, AppCompatActivity activity) {
        this.taskGroupList = taskGroups;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return taskGroupList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // get current item
        TaskGroup current = taskGroupList.get(position);
        View view = onCreateView(container, current);
        container.addView(view, 0);
        return view;
    }

    public View onCreateView(ViewGroup container, final TaskGroup current) {
        View view = layoutInflater.inflate(R.layout.list_card_item, container, false);
        final List<Task> taskList = current.getTaskList();
        // set-up Title
        final EditText listnameText = view.findViewById(R.id.listName);
        listnameText.setText(current.getTitle());
        listnameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditTitleClick(listnameText);
            }
        });
        // button add Card
        Button addCardButton = view.findViewById(R.id.addCardButton);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreateTaskActivity.class);
                intent.putExtra("group_id", current.getId());
                activity.startActivityForResult(intent, 100);
            }
        });
        // set up Recycle view
        RecyclerView listTaskRecycleView = view.findViewById(R.id.listTaskRecycleView);
        TaskRecycleViewAdapter taskRecycleViewAdapter = new TaskRecycleViewAdapter(activity, taskList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        listTaskRecycleView.setLayoutManager(linearLayoutManager);
        listTaskRecycleView.setItemAnimator(new DefaultItemAnimator());
        listTaskRecycleView.setAdapter(taskRecycleViewAdapter);
        return view;
    }

    public View addView(ViewPager pager, TaskGroup current) {
        View view = onCreateView(pager, current);
        pager.addView(view);
        taskGroupList.add(current);
        notifyDataSetChanged();
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void onEditTitleClick(EditText view) {
        // change action bar
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateMenu(R.menu.menu_edit_title, "Edit title", true);
        }
        // change edit view
        view.requestFocus();
        view.setBackgroundColor(Color.WHITE);
        view.setCursorVisible(true);
    }
}
