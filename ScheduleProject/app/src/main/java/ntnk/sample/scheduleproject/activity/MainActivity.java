package ntnk.sample.scheduleproject.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPagerUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.TaskGroupPagerAdapter;
import ntnk.sample.scheduleproject.adapter.TaskRecycleViewAdapter;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskGroup;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String boardName = "Spring 2019";
    TaskGroupPagerAdapter taskGroupPagerAdapter;
    TaskRecycleViewAdapter taskRecycleViewAdapter;
    List<TaskGroup> taskGroupList;
    ViewPager taskGroupViewPager;
    Button addListButton;
    int menu_layout = R.menu.search_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskGroupViewPager = findViewById(R.id.viewPager);
        addListButton = findViewById(R.id.addListButton);
        //for testing
        taskGroupList = new ArrayList<>();
        TaskGroup taskGroup = new TaskGroup("to do");
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Test 1"));
        taskList.add(new Task("Test 2"));
        taskList.add(new Task("Test 3"));
        taskGroup.setTaskList(taskList);
        taskGroupList.add(taskGroup);
        taskGroupList.add(new TaskGroup("Doing", taskList, 1));
        taskGroupList.add(new TaskGroup("Done"));
        taskGroupPagerAdapter = new TaskGroupPagerAdapter(taskGroupList, this);

        taskGroupViewPager.setAdapter(taskGroupPagerAdapter);
        taskGroupViewPager.setPadding(50, 50, 50, 50);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_layout, menu);
        if (menu_layout == R.menu.search_menu) {
            MenuItem menuItem = menu.findItem(R.id.search_icon);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterSearch(newText);
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void filterSearch(String query) {
        for (int i = 0; i < taskGroupViewPager.getChildCount(); ++i) {
            View child = taskGroupViewPager.getChildAt(i);
            RecyclerView currentRecycleView = child.findViewById(R.id.listTaskRecycleView);
            taskRecycleViewAdapter = (TaskRecycleViewAdapter) currentRecycleView.getAdapter();
            taskRecycleViewAdapter.getFilter().filter(query);
        }
    }

    public void updateMenu(int menutoChoose, String title, boolean showXIcon) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showXIcon);
        menu_layout = menutoChoose;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_title) {
            EditText currentTitle = styleBackForEditText();
            String new_title = currentTitle.getText().toString();
            //update taskgroup name title in db

            //change menu_layout to search
            updateMenu(R.menu.search_menu, boardName, false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (menu_layout == R.menu.menu_edit_title)
            updateMenu(R.menu.search_menu, boardName, false);
        styleBackForEditText();
        return super.onSupportNavigateUp();
    }

    public EditText styleBackForEditText() {
        //get current View
        View current = ViewPagerUtils.getCurrentView(taskGroupViewPager);
        EditText currentTitle = current.findViewById(R.id.listName);
        //set style back for editText
        currentTitle.setBackgroundColor(Color.TRANSPARENT);
        currentTitle.setCursorVisible(false);
        currentTitle.setTextColor(Color.BLACK);
        return currentTitle;
    }

    public void onAddListClick(View view) {
        taskGroupPagerAdapter.addView(taskGroupViewPager, new TaskGroup("")).requestFocus();
    }
}
