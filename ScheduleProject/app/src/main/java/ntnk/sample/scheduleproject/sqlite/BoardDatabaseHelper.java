package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.entity.TaskImage;

public class BoardDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "board_database";
    private static final int DB_VER = 2;
    private static final String DATABASE_CREATE = "CREATE TABLE board (" +
            "id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "color INTEGER" +
            ")";

    public BoardDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropScript = "DROP TABLE IF EXISTS board";
        db.execSQL(dropScript);
        onCreate(db);
    }

    public List<Board> getListBoards(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("board", null, null, null,
                null, null, "id");

        List<Board> list = new ArrayList<>();
        while (cursor.moveToNext()){
            Board board = new Board(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("color"))
            );
            list.add(board);
        }
        return list;
    }

    public void insert(Board board){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", board.getId());
        contentValues.put("name", board.getName());
        contentValues.put("color", board.getColor());

        db.insert("board", null, contentValues);
    }

    public void deleteBoardById(int boardId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("board",
                "id = ?",
                new String[]{String.valueOf(boardId)});
    }

    public void update(Board board){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", board.getName());
        contentValues.put("color", board.getColor());

        db.update("board",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(board.getId())});
    }
}
