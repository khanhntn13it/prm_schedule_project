package ntnk.sample.scheduleproject.adapter;

import android.content.Context;
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
import ntnk.sample.scheduleproject.entity.Task;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> implements Filterable {
    private List<Task> taskList;
    private LayoutInflater layoutInflater;
    private List<Task> resultSearchList;

    public TaskRecycleViewAdapter(Context context, List<Task> tasks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = tasks;
        this.resultSearchList = tasks;
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
                //Intent intent = new Intent(this, )
                Toast.makeText(v.getContext(), "edit chosen " + chosen.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next to Detail View
                Toast.makeText(v.getContext(), "chosen " + chosen.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete task in DB
                removeItem(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultSearchList.size();
    }

    public void addItem(int position, Task task) {
        resultSearchList.add(position, task);
        taskList.add(position, task);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (resultSearchList.size() == 1) position = 0;
        resultSearchList.remove(position);
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public Task getItem(int position) {
        if (resultSearchList.size() != 0)
            return resultSearchList.get(position);
        return null;
    }

    public void updateList(String word) {
        List<Task> searchList = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getTitle().toLowerCase().contains(word.toLowerCase())) {
                searchList.add(task);
            }
        }
        taskList.clear();
        taskList.addAll(searchList);
        notifyDataSetChanged();
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
