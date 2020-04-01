package ntnk.sample.scheduleproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.TaskAdapter;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;

public class TodayTaskActivity extends AppCompatActivity {

    List<Task> taskList;
    TaskDAO taskDAO;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);
        taskDAO = new TaskDAO(this);
        //Toast.makeText(getApplicationContext(), Calendar.getInstance().getTime().toString(), Toast.LENGTH_SHORT).show();
        taskList = taskDAO.getTaskListByDay(Calendar.getInstance().getTime());
        //Toast.makeText(getApplicationContext(), String.valueOf(taskList.size()), Toast.LENGTH_SHORT).show();
        taskAdapter = new TaskAdapter(taskList, this);
        ListView listView = (ListView)findViewById(R.id.lvTodayTask);
        listView.setAdapter(taskAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TodayTaskActivity.this, ViewTaskActivity.class);
                intent.putExtra("taskId", taskList.get(position).getId());
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(TodayTaskActivity.this, BoardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(TodayTaskActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(TodayTaskActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            };
}
