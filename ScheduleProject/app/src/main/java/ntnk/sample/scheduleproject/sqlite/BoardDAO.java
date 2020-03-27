package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.entity.Board;
import ntnk.sample.scheduleproject.entity.TaskGroup;

public class BoardDAO extends ModelDAO {
    private TaskGroupDAO taskGroupDAO;
    public BoardDAO(Context mContext) {
        super(mContext);
        taskGroupDAO = new TaskGroupDAO(mContext);
    }
    public List<Board> getListBoards(){
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
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", board.getId());
        contentValues.put("name", board.getName());
        contentValues.put("color", board.getColor());

        db.insert("board", null, contentValues);
    }

    public void deleteBoardById(Board board){
        List<TaskGroup> taskGroups = board.getTaskGroups();
        for(TaskGroup tg: taskGroups) {
            db.delete("taskgroup",
                    "id = ?",
                    new String[]{String.valueOf(tg.getId())});
        }
        db.delete("board",
                "id = ?",
                new String[]{String.valueOf(board.getId())});
    }

    public void update(Board board){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", board.getName());
        contentValues.put("color", board.getColor());

        db.update("board",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(board.getId())});
    }
}
