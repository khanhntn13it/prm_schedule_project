package ntnk.sample.scheduleproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPagerUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.TaskGroupPagerAdapter;
import ntnk.sample.scheduleproject.adapter.TaskRecycleViewAdapter;
import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskGroup;
import ntnk.sample.scheduleproject.sqlite.BoardDAO;
import ntnk.sample.scheduleproject.sqlite.TaskGroupDAO;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String boardName = "Spring 2019";
    TaskGroupPagerAdapter taskGroupPagerAdapter;
    TaskRecycleViewAdapter taskRecycleViewAdapter;
    List<TaskGroup> taskGroupList;
    ViewPager taskGroupViewPager;
    Button addListButton;
    int menu_layout = R.menu.search_menu;
    TaskGroupDAO taskGroupDB;
    BoardDAO boardDB;
    ActionBar actionBar;
    int currentBoardId = -1; // get from intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        taskGroupViewPager = findViewById(R.id.viewPager);
        addListButton = findViewById(R.id.addListButton);
        taskGroupDB = new TaskGroupDAO(this);
        boardDB = new BoardDAO(this);
        // get Board then set actionbar
        Intent intent = getIntent();
        currentBoardId = intent.getIntExtra("boardId", -1);
        Board board = boardDB.getBoardByID(currentBoardId);
        actionBar = getSupportActionBar();
        boardName = board.getName();
        actionBar.setTitle(boardName);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        taskGroupList = taskGroupDB.getTaskGroupListByBoard(currentBoardId);
        if(taskGroupList == null) taskGroupList = new ArrayList<>();
        taskGroupPagerAdapter = new TaskGroupPagerAdapter(taskGroupList, this);

        taskGroupViewPager.setAdapter(taskGroupPagerAdapter);
        //taskGroupViewPager.setPadding(50, 50, 50, 50);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
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
        menu_layout = menutoChoose;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_title) {

        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home: {
                this.finish();
                return true;
            }
            case R.id.edit_title : {
                int currentPosi = taskGroupViewPager.getCurrentItem();
                EditText currentTitle = styleBackForEditText();
                String new_title = currentTitle.getText().toString();
                //update taskgroup name title in db
                TaskGroup updateGroup = taskGroupPagerAdapter.getItemTaskGroup(currentPosi);
                updateGroup.setTitle(new_title);
                taskGroupDB.update(updateGroup);
                taskGroupPagerAdapter.notifyDataSetChanged();
                //change menu_layout to search
                updateMenu(R.menu.search_menu, boardName, true);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (menu_layout == R.menu.menu_edit_title)
            updateMenu(R.menu.search_menu, boardName, true);
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
        TaskGroup taskGroup = new TaskGroup("Enter title here", currentBoardId);
        long taskGroupId = taskGroupDB.insert(taskGroup);
        taskGroup.setId((int) taskGroupId);
        taskGroupPagerAdapter.addView(taskGroupViewPager, taskGroup).requestFocus();
        taskGroupViewPager.setCurrentItem(getItem(+1), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
             if(requestCode == 101 && resultCode == 201) {
                    setCurrentTaskRecycleViewAdapter();
                    Task newTask = (Task) data.getSerializableExtra("new_task");
                    taskRecycleViewAdapter.addItem(newTask);

                }
            if(requestCode == 102 && resultCode == 202) {
                setCurrentTaskRecycleViewAdapter();
                Task updateTask = (Task) data.getSerializableExtra("update_task");
                int position = data.getIntExtra("taskPosi", -1);
                if(position >= 0) {
                    taskRecycleViewAdapter.updateItem(position, updateTask);
                }
            }
        }
    }
    private int getItem(int i) {
        return taskGroupViewPager.getCurrentItem() + i;
    }
    public void setCurrentTaskRecycleViewAdapter() {
        View child = ViewPagerUtils.getCurrentView(taskGroupViewPager);
        RecyclerView currentRecycleView = child.findViewById(R.id.listTaskRecycleView);
        taskRecycleViewAdapter = (TaskRecycleViewAdapter) currentRecycleView.getAdapter();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(MainActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(MainActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            return true;
                    }
                    return false;
                }
            };
}
