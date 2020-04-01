package ntnk.sample.scheduleproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
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

    @BindView(R.id.imageViewProfilePic_v)
    ImageView imageViewProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(this);
        Intent intent = getIntent();
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

        imageViewProfilePic = findViewById(R.id.imageViewProfilePic_v);
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
                break;
            }
            case 2 :{
                radioButtonDoing.setChecked(true);
                radioButtonDoing.setVisibility(View.VISIBLE);
                break;
            }
            case 3 :{
                radioButtonDone.setChecked(true);
                radioButtonDone.setVisibility(View.VISIBLE);
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
            Uri uri = Uri.fromFile(new File(task.getTaskImage()));
            try {
                ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
                if(parcelFileDescriptor != null) {
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
                    Glide.with(ViewTaskActivity.this)
                            .load(bitmap)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.profile_pic_place_holder))
                            .into(imageViewProfilePic);
                }
            }catch (FileNotFoundException e){

            }
        }
    }

    public void backBtnAction(View view){
        finish();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(ViewTaskActivity.this, BoardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(ViewTaskActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(ViewTaskActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            };
}
