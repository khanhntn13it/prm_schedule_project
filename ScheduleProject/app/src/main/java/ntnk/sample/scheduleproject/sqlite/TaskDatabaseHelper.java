package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ntnk.sample.scheduleproject.entity.Task;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schedule_database";
    private static final int DB_VER = 2;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public TaskDatabaseHelper(Context context){
        super(context,DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE task ("
                + "id INTEGER PRIMARY KEY, "
                +"title TEXT, "
                +"date TEXT, "
                +"description TEXT, "
                +"status INTEGER, "
                +"urgent_importance INTEGER, "
                + "board_id INTEGER"
                +")";

        db.execSQL(script);
    }
//    title, data, description, status, urgent-importance, board_id
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropScript = "DROP TABLE IF EXISTS task";
        db.execSQL(dropScript);
        onCreate(db);
    }


    //get
    public Task getTaskById(int taskId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("task",
                new String[]{"id", "title", "date", "description", "status", "urgent_importance", "board_id"},
                "id = ?",
                new String[]{String.valueOf(taskId)},
                null,null, null, null);
        if(cursor == null){
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
        try{
            date = dateFormat.parse(dateStr);
        }catch (ParseException e){
            Log.e("TaskDatabaseHelper","SQLite Task parse date", e);
        }
        //--------------
        task.setDate(date);
        task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        task.setUrgent_importance(cursor.getInt(cursor.getColumnIndex("urgent_importance")));
        task.setBoardId(cursor.getInt(cursor.getColumnIndex("board_id")));

        return task;
    }

    /**
     * Insert Task
     * @param task
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("date", dateFormat.format(task.getDate()));
        contentValues.put("description",task.getDescription());
        contentValues.put("status",task.getStatus());
        contentValues.put("urgent_importance",task.getUrgent_importance());
        contentValues.put("board_id",task.getBoardId());

        return db.insert("task", null, contentValues);
    }
//    title, data, description, status, urgent-importance, board_id

    /**
     * Update Task
     * @param task
     * @return the number of rows affected
     */
    public int update(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("date",dateFormat.format(task.getDate()));
        contentValues.put("description",task.getDescription());
        contentValues.put("status",task.getStatus());
        contentValues.put("urgent_importance",task.getUrgent_importance());
        contentValues.put("board_id",task.getBoardId());

        return db.update("task",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(task.getId())});
    }

    /**
     * Delete Task by id.
     * @param taskId
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int deleteTaskById(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("task",
                "id = ?",
                new String[]{String.valueOf(taskId)});
    }
}
