package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.entity.TaskImage;

public class TaskImageDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "schedule_database_2";
    private static final int DB_VER = 2;

    public TaskImageDBHelper(Context context){
        super(context,DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE task_image ("
                + "id INTEGER PRIMARY KEY, "
                +"task_id INTEGER, "
                +"image TEXT "
                +")";

        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropScript = "DROP TABLE IF EXISTS task_image";
        db.execSQL(dropScript);
        onCreate(db);
    }

    public long insert(TaskImage taskImage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("task_id", taskImage.getTaskId());
        contentValues.put("image", taskImage.getImage());

        return db.insert("task_image", null, contentValues);
    }

    public long update(TaskImage taskImage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("task_id", taskImage.getTaskId());
        contentValues.put("image", taskImage.getImage());

        return db.update("task_image",contentValues, "id = ?", new String[]{String.valueOf(taskImage.getId())});
    }

    public long delete(int taskImageId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("task_image","id = ?", new String[]{String.valueOf(taskImageId)});
    }

    public List<TaskImage> getTaskImageByTask(int taskId){
        List<TaskImage> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "task_image",
                new String[]{"id", "task_id", "image"},
                "task_id = ?",
                new String[]{String.valueOf(taskId)},
                null, null, null);

        if(cursor == null){
            return list;
        }
        while (cursor.moveToNext()){
            TaskImage taskImage = new TaskImage();
            taskImage.setId(cursor.getInt(cursor.getColumnIndex("id")));
            taskImage.setTaskId(cursor.getInt(cursor.getColumnIndex("task_id")));
            taskImage.setImage(cursor.getString(cursor.getColumnIndex("image")));
            list.add(taskImage);
        }

        return  list;
    }
}
