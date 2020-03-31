package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.UpdateTaskActivity;
import ntnk.sample.scheduleproject.activity.ViewTaskActivity;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;
import ntnk.sample.scheduleproject.sqlite.TaskGroupDAO;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> implements Filterable {
    public List<Task> taskList;
    private LayoutInflater layoutInflater;
    private List<Task> resultSearchList;
    private Activity activity;
    private TaskDAO taskDB;
    private TaskGroupDAO taskGroupDB;

    public TaskRecycleViewAdapter(Context context, List<Task> tasks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = tasks;
        this.resultSearchList = tasks;
        this.activity = (Activity) context;
        taskGroupDB = new TaskGroupDAO(context);
        taskDB = new TaskDAO(context);
    }

    @NonNull
    @Override
    public TaskRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    /* not yet : 1
        doing : 2
        done : 3
    * */
    @Override
    public void onBindViewHolder(@NonNull final TaskRecycleViewAdapter.ViewHolder holder, int position) {
        holder.itemCard.setTag(position);
        String title = resultSearchList.get(position).getTitle();
        holder.taskNameTextView.setText(title);
        holder.taskNameTextView.setTextColor(activity.getResources().getColor(R.color.titleTaskTextColor));

        final int currentPosition = position;
        final Task chosen = resultSearchList.get(currentPosition);

        if (chosen.getStatus() == 1) {
            holder.itemCard.setBackgroundColor(activity.getResources().getColor(R.color.notyetColor));
        }
        if (chosen.getStatus() == 2) {
            holder.itemCard.setBackgroundColor(activity.getResources().getColor(R.color.doingColor));
        }
        if (chosen.getStatus() == 3) {
            holder.itemCard.setBackgroundColor(activity.getResources().getColor(R.color.doneColor));
        }
        holder.itemCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.putExtra( "srcItem", chosen);
                ClipData.Item item = new ClipData.Item(intent);
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_INTENT};
                ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                holder.itemCard.startDragAndDrop(data, dragShadowBuilder,v,0 );

                return true;
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next to edit View
                Intent intent = new Intent(activity, UpdateTaskActivity.class);
                intent.putExtra("taskId", chosen.getId());
                intent.putExtra("taskPosi", currentPosition);
                activity.startActivityForResult(intent, 102,  ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
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
                if (!activity.isFinishing()) {
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
                            taskDB.deleteTaskById(chosen.getId());
                            removeItem(currentPosition);
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
        resultSearchList.add(task);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        resultSearchList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, Task task) {
        resultSearchList.set(position, task);
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
        CardView itemCard;

        ViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.titleTask);
            editButton = itemView.findViewById(R.id.buttonEditTask);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            viewButton = itemView.findViewById(R.id.buttonViewTask);
            itemCard = itemView.findViewById(R.id.itemCardView);
        }
    }
}
