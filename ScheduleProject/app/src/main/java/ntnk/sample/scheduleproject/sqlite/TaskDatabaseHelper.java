package ntnk.sample.scheduleproject.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schedule_database";
    private static final int DB_VER = 5;
    private static final String BOARD_CREATE = "CREATE TABLE board (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "color INTEGER" +
            ")";
    private static final String GROUP_CREATE = "CREATE TABLE task_group ("
            + "id INTEGER PRIMARY KEY, "
            +"board_id INTEGER NOT NULL, "
            +"title TEXT, "
            +"FOREIGN KEY(board_id) REFERENCES "
            +"board(id)"
            +")";
    private static final String TASK_CREATE = "CREATE TABLE task ("
            + "id INTEGER PRIMARY KEY, "
            +"title TEXT, "
            +"date TEXT, "
            +"description TEXT, "
            +"status INTEGER, "
            +"urgent_importance INTEGER, "
            +"group_id INTEGER, "
            +"image TEXT, "
            +"FOREIGN KEY(group_id) REFERENCES "
            +"taskgroup(id)"
            +")";
    private static TaskDatabaseHelper instance;
    public TaskDatabaseHelper(Context context){
        super(context,DB_NAME, null, DB_VER);
    }
    public static synchronized TaskDatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new TaskDatabaseHelper(context);
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BOARD_CREATE);
        db.execSQL(GROUP_CREATE);
        db.execSQL(TASK_CREATE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            // enable foreigin_key
            db.execSQL("PRAGMA foreign_key = ON");
        }
    }

    //    title, data, description, status, urgent-importance, group_id
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_task = "DROP TABLE IF EXISTS task" ;
        String drop_group = "DROP TABLE IF EXISTS task_group" ;
        String drop_board = "DROP TABLE IF EXISTS board;";
        db.execSQL(drop_task);
        db.execSQL(drop_group);
        db.execSQL(drop_board);
        onCreate(db);
    }

}
