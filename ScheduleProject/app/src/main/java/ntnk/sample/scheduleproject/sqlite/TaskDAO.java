package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskImage;

public class TaskDAO extends ModelDAO {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public TaskDAO(Context mContext) {
        super(mContext);
    }

    //get
    public Task getTaskById(int taskId) {
        Cursor cursor = db.query("task",
                new String[]{"id", "title", "date", "description", "status", "urgent_importance", "group_id"},
                "id = ?",
                new String[]{String.valueOf(taskId)},
                null, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        Task task = new Task();
        task.setId(cursor.getInt(cursor.getColumnIndex("id")));
        task.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        String dateStr = cursor.getString(cursor.getColumnIndex("date"));
        //parse date------
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            Log.e("TaskDatabaseHelper", "SQLite Task parse date", e);
        }
        //--------------
        task.setDate(date);
        task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        task.setUrgent_importance(cursor.getInt(cursor.getColumnIndex("urgent_importance")));
        task.setGroupId(cursor.getInt(cursor.getColumnIndex("group_id")));
        task.setTaskImage(cursor.getString(cursor.getColumnIndex("image")));
        return task;
    }
    public List<Task> getTaskListByGroup(int groupId) {
        db = dbHelper.getReadableDatabase();
        List<Task> result = new ArrayList<>();
        Cursor cursor = db.query("task",
                new String[]{"id", "title", "date", "description", "status", "urgent_importance", "group_id"},
                "group_id = ?",
                new String[]{String.valueOf(groupId)},
                null, null, null, null);
        while(cursor.moveToNext()) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            //parse date------
            Date date = null;
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                Log.e("TaskDatabaseHelper", "SQLite Task parse date", e);
            }
            //--------------
            task.setDate(date);
            task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            task.setUrgent_importance(cursor.getInt(cursor.getColumnIndex("urgent_importance")));
            task.setGroupId(cursor.getInt(cursor.getColumnIndex("group_id")));
            result.add(task);
        }
        return result;
    }
    /**
     * Insert Task
     *
     * @param task
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(Task task) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("date", dateFormat.format(task.getDate()));
        contentValues.put("description", task.getDescription());
        contentValues.put("status", task.getStatus());
        contentValues.put("urgent_importance", task.getUrgent_importance());
        contentValues.put("group_id", task.getGroupId());

        return db.insert("task", null, contentValues);
    }
//    title, data, description, status, urgent-importance, board_id

    /**
     * Update Task
     *
     * @param task
     * @return the number of rows affected
     */
    public int update(Task task) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("date", dateFormat.format(task.getDate()));
        contentValues.put("description", task.getDescription());
        contentValues.put("status", task.getStatus());
        contentValues.put("urgent_importance", task.getUrgent_importance());
        contentValues.put("group_id", task.getGroupId());

        return db.update("task",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(task.getId())});
    }

    /**
     * Delete Task by id.
     *
     * @param task
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int deleteTaskById(Task task) {
        return db.delete("task",
                "id = ?",
                new String[]{String.valueOf(task.getId())});
    }
}
