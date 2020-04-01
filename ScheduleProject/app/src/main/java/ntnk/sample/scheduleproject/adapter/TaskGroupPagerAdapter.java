package ntnk.sample.scheduleproject.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.CreateTaskActivity;
import ntnk.sample.scheduleproject.activity.MainActivity;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskGroup;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;
import ntnk.sample.scheduleproject.sqlite.TaskGroupDAO;

public class TaskGroupPagerAdapter extends PagerAdapter {
    private List<TaskGroup> taskGroupList;
    private LayoutInflater layoutInflater;
    private AppCompatActivity activity;
    private TaskGroupDAO taskGroupDB;
    private TaskRecycleViewAdapter targetRvAdapter;
    private View targetCurrent;
    int targetPosition = -1;
    TaskGroup targetTaskgroup = new TaskGroup();
    TaskDAO taskDB;

    public TaskGroupPagerAdapter(List<TaskGroup> taskGroups, AppCompatActivity activity) {
        this.taskGroupList = taskGroups;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        taskGroupDB = new TaskGroupDAO(activity);
        taskDB = new TaskDAO(activity);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public int getTaskGroupPosition(@NonNull TaskGroup obj) {
        for (int i = 0; i < taskGroupList.size(); i++) {
            if (obj.getId() == taskGroupList.get(i).getId())
                return i;
        }
        return -1;
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // get current item
        TaskGroup current = taskGroupList.get(position);
        View view = onCreateView(container, current, position);
        view.setTag(position);
        container.addView(view, 0);
        return view;
    }

    public View onCreateView(final ViewGroup container, final TaskGroup current, final int position) {
        final View view = layoutInflater.inflate(R.layout.list_card_item, container, false);
        view.setTag(position);
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
        ImageButton deleteGroupBtn = view.findViewById(R.id.deletegroupBtn);
        deleteGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.isFinishing()) {
                    // delete task_group in DB
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirm delete");
                    builder.setMessage("Do you really want to remove this list?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            taskGroupDB.deleteTaskGroupById(current);
                            taskGroupList.remove(current);
                            destroyItem(container, position, view);
                            notifyDataSetChanged();
                            Toast.makeText(activity, "DELETED! As you wish!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        // button add Card
        Button addCardButton = view.findViewById(R.id.addCardButton);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreateTaskActivity.class);

                //pass groupId *********
                intent.putExtra("groupId", current.getId());
                //startForResult***********
                activity.startActivityForResult(intent, 101);
            }
        });
        // set up Recycle view
        RecyclerView listTaskRecycleView = view.findViewById(R.id.listTaskRecycleView);
        TaskRecycleViewAdapter taskRecycleViewAdapter = new TaskRecycleViewAdapter(activity, taskList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        listTaskRecycleView.setLayoutManager(linearLayoutManager);
        listTaskRecycleView.setItemAnimator(new DefaultItemAnimator());
        listTaskRecycleView.setAdapter(taskRecycleViewAdapter);

        container.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Toast.makeText(activity, String.valueOf(event.getAction()) + v.toString(), Toast.LENGTH_SHORT).show();
                ViewPager pager = (ViewPager) v;
                View source = (View) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED: {
                        Toast.makeText(activity, "drag entered", Toast.LENGTH_SHORT).show();
                        v.setBackgroundColor(activity.getResources().getColor(R.color.colorForStep3));
                        return true;
                    }
                    case DragEvent.ACTION_DRAG_LOCATION: {
                        v.setBackgroundColor(activity.getResources().getColor(R.color.about_description_color));
                        float posiX = event.getX();
                        float posiY = event.getY();
                        System.out.println("ACTION_DRAG_LOCATION: View current is: "+ pager.getCurrentItem());
//                        for(int i = 0; i < pager.getChildCount(); i++) {
//                            if(ViewUtils.isPointInsideView(posiX,posiY,pager.getChildAt(i))) {
//                                targetPosition = i;
//                                pager.setCurrentItem(targetPosition);
//                                targetCurrent = pager.getChildAt(targetPosition);
//                                targetTaskgroup = getItemTaskGroup(targetPosition);
//                            }
//                        }

                        return true;
                    }


                    case DragEvent.ACTION_DROP: {
                        v.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                        targetPosition = pager.getCurrentItem();
                        targetTaskgroup = getItemTaskGroup(targetPosition);
                        targetCurrent = pager.getChildAt(targetPosition);
                        System.out.println("ACTION_DROp: View current is: "+ pager.getCurrentItem());
                        ClipData data = event.getClipData();
                        RecyclerView targetView = targetCurrent.findViewById(R.id.listTaskRecycleView);
                        targetRvAdapter = (TaskRecycleViewAdapter) targetView.getAdapter();
                        if (targetPosition >= 0) {
                            ClipData.Item item = data.getItemAt(0);
                            Task added = (Task) item.getIntent().getSerializableExtra("srcItem");
                            targetRvAdapter.addItem(added);
                            added.setGroupId(targetTaskgroup.getId());
                            //update to DB
                            //taskDB.insert(added);
                        }
                    }

                }
                return true;
            }
        });
        return view;
    }

    public View addView(ViewPager pager, TaskGroup current) {
        View view = onCreateView(pager, current, 0);
        pager.addView(view);
        taskGroupList.add(current);
        notifyDataSetChanged();
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public TaskGroup getItemTaskGroup(int position) {
        return taskGroupList.get(position);
    }

    public void onEditTitleClick(EditText view) {
        if (view.getText().toString().equals("Enter title here")) {
            view.setText("");
        }
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
