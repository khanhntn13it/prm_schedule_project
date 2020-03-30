package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.UpdateTaskActivity;
import ntnk.sample.scheduleproject.activity.ViewTaskActivity;
import ntnk.sample.scheduleproject.broadcast.NotifiTaskChannel;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;
import ntnk.sample.scheduleproject.sqlite.TaskGroupDAO;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> implements Filterable {
    public  List<Task> taskList;
    private LayoutInflater layoutInflater;
    private List<Task> resultSearchList;
    private Activity activity;
    private TaskDAO taskDB;
    private TaskGroupDAO taskGroupDB;
    NotifiTaskChannel notifiTaskChannel;

    public TaskRecycleViewAdapter(Context context, List<Task> tasks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = tasks;
        this.resultSearchList = tasks;
        this.activity = (Activity) context;
        taskGroupDB = new TaskGroupDAO(context);
        taskDB = new TaskDAO(context);
        notifiTaskChannel = new NotifiTaskChannel(context);
    }

    @NonNull
    @Override
    public TaskRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.ViewHolder holder, int position) {
        String title = resultSearchList.get(position).getTitle();
        holder.taskNameTextView.setText(title);
        final int currentPosition = position;
        final Task chosen = resultSearchList.get(currentPosition);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next to edit View
                Intent intent = new Intent(activity, UpdateTaskActivity.class);
                intent.putExtra("taskId", chosen.getId());
                intent.putExtra("taskPosi", currentPosition);
                activity.startActivityForResult(intent, 102);
                Toast.makeText(v.getContext(), "edit chosen " + chosen.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next to Detail View
                Intent intent = new Intent(activity, ViewTaskActivity.class);
                intent.putExtra("taskId", chosen.getId());
                activity.startActivity(intent);
                Toast.makeText(v.getContext(), "chosen " + chosen.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isFinishing()) {
                    // delete task in DB
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirm delete");
                    builder.setMessage("Do you really want to remove this task?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Task task = taskDB.getTaskById(chosen.getId());

                            taskDB.deleteTaskById(chosen.getId());
                            removeItem(currentPosition);

                            //remove alarm********
                            notifiTaskChannel.clearAlarm(task);
                            //*****************

                            Toast.makeText(activity, "DELETED! As you wish!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return resultSearchList.size();
    }

    public void addItem(Task task) {
        resultSearchList.add( task);
        //taskList.add(task);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        //if (resultSearchList.size() == 1 || taskList.size() == 1) position = 0;
        resultSearchList.remove(position);
        //taskList.remove(position);
        notifyDataSetChanged();
        //notifyItemRangeChanged(position, taskList.size());
    }
    public void updateItem(int position, Task task) {
        resultSearchList.set(position, task);
        //taskList.set(position, task);
        notifyItemChanged(position, task);
    }
    public Task getItem(int position) {
        if (resultSearchList.size() != 0)
            return resultSearchList.get(position);
        return null;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resultSearchList = taskList;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task row : taskList) {
                        // here we are looking for title match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    resultSearchList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = resultSearchList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultSearchList = (ArrayList<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        ImageButton editButton;
        ImageButton deleteButton;
        ImageButton viewButton;

        ViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.titleTask);
            editButton = itemView.findViewById(R.id.buttonEditTask);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            viewButton = itemView.findViewById(R.id.buttonViewTask);
        }
    }
}
