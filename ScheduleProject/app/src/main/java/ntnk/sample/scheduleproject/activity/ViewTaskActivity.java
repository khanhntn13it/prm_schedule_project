package ntnk.sample.scheduleproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;

public class ViewTaskActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDate;

    private RadioButton radioButtonNotyet;
    private RadioButton radioButtonDoing;
    private RadioButton radioButtonDone;
    private RadioButton radioButtonPriority1;
    private RadioButton radioButtonPriority2;
    private RadioButton radioButtonPriority3;
    private RadioButton radioButtonPriority4;

    TaskDAO taskDAO;
    Task task;

    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(this);
        Intent intent = getIntent();
        //*****remember to fix default value*******************************
        int taskId = intent.getIntExtra("taskId", -1);
        if(taskId  < 0){
            setContentView(R.layout.activity_task_notfound);
            return;
        }else {
            task = taskDAO.getTaskById(taskId);
            if(task == null){
                setContentView(R.layout.activity_task_notfound);
                return;
            }
        }
        setContentView(R.layout.activity_view_task);

        assignUIComponent();
        displayCurrentTask();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void assignUIComponent(){
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDate = findViewById(R.id.textViewDate);
        textViewDescription = findViewById(R.id.textViewTextDes);

        radioButtonNotyet = findViewById(R.id.radioButtonNotyet);
        radioButtonNotyet.setVisibility(View.GONE);
        radioButtonDoing = findViewById(R.id.radioButtonDoing);
        radioButtonDoing.setVisibility(View.GONE);
        radioButtonDone = findViewById(R.id.radioButtonDone);
        radioButtonDone.setVisibility(View.GONE);

        radioButtonPriority1 = findViewById(R.id.radioButtonUI1);
        radioButtonPriority1.setVisibility(View.GONE);
        radioButtonPriority2 = findViewById(R.id.radioButtonUI2);
        radioButtonPriority2.setVisibility(View.GONE);
        radioButtonPriority3 = findViewById(R.id.radioButtonUI3);
        radioButtonPriority3.setVisibility(View.GONE);
        radioButtonPriority4 = findViewById(R.id.radioButtonUI4);
        radioButtonPriority4.setVisibility(View.GONE);
    }

    private void displayCurrentTask(){
        textViewTitle.setText(task.getTitle());
        textViewDescription.setText(task.getDescription());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(task.getDate());
        textViewDate.setText(datetime);

        switch (task.getStatus()){
            case 1 :{
                radioButtonNotyet.setChecked(true);
                radioButtonNotyet.setVisibility(View.VISIBLE);
//                ((ViewManager)radioButtonDoing.getParent()).removeView(radioButtonDoing);
//                ((ViewManager)radioButtonDone.getParent()).removeView(radioButtonDone);
                break;
            }
            case 2 :{
                radioButtonDoing.setChecked(true);
                radioButtonDoing.setVisibility(View.VISIBLE);
                break;
            }
            case 3 :{
                radioButtonDone.setChecked(true);
                radioButtonDoing.setVisibility(View.VISIBLE);
                break;
            }
        }

        switch (task.getUrgent_importance()){
            case 1:{
                radioButtonPriority1.setChecked(true);
                radioButtonPriority1.setVisibility(View.VISIBLE);
                break;
            }
            case 2:{
                radioButtonPriority2.setChecked(true);
                radioButtonPriority2.setVisibility(View.VISIBLE);
                break;
            }
            case 3:{
                radioButtonPriority3.setChecked(true);
                radioButtonPriority3.setVisibility(View.VISIBLE);
                break;
            }
            case 4:{
                radioButtonPriority4.setChecked(true);
                radioButtonPriority4.setVisibility(View.VISIBLE);
                break;
            }
        }
        if(task.getTaskImage() != null) {
            Glide.with(ViewTaskActivity.this)
                    .load(task.getTaskImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.profile_pic_place_holder))
                    .into(imageViewProfilePic);
        }
    }

    public void backBtnAction(){
        finish();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(ViewTaskActivity.this, BoardActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(ViewTaskActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(ViewTaskActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            return true;
                    }
                    return false;
                }
            };
}
