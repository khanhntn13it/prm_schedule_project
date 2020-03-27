package ntnk.sample.scheduleproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.entity.TaskGroup;

public class TaskGroupDAO extends ModelDAO {
    private TaskDAO taskDAO;

    public TaskGroupDAO(Context mContext) {
        super(mContext);
        taskDAO = new TaskDAO(mContext);
    }

    public TaskGroup getTaskGroupByID(int xID) {
        TaskGroup taskGroup;
        Cursor cursor = db.query("task_group",
                new String[]{"id", "title", "board_id",},
                "id = ?",
                new String[]{String.valueOf(xID)},
                null, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        taskGroup = new TaskGroup();
        taskGroup.setId(cursor.getInt(cursor.getColumnIndex("id")));
        taskGroup.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        taskGroup.setBoardId(cursor.getInt(cursor.getColumnIndex("board_id")));
        taskGroup.setTaskList(taskDAO.getTaskListByGroup(xID));
        return taskGroup;
    }

    public List<TaskGroup> getTaskGroupListByBoard(int boardId) {
        List<TaskGroup> taskGroups = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("task_group",
                new String[]{"id", "title", "board_id",},
                "board_id = ?",
                new String[]{String.valueOf(boardId)},
                null, null, null, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setId(cursor.getInt(cursor.getColumnIndex("id")));
            taskGroup.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            taskGroup.setBoardId(cursor.getInt(cursor.getColumnIndex("board_id")));
            taskGroup.setTaskList(taskDAO.getTaskListByGroup(taskGroup.getId()));
            taskGroups.add(taskGroup);
        }
        return taskGroups;
    }

    public long insert(TaskGroup taskgroup) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", taskgroup.getTitle());
        contentValues.put("board_id", taskgroup.getBoardId());
        return db.insert("task_group", null, contentValues);
    }

    public int update(TaskGroup taskGroup) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", taskGroup.getTitle());
        return db.update("task_group",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(taskGroup.getId())});
    }

    public int deleteTaskGroupById(TaskGroup taskGroup) {
        List<Task> tasks = taskGroup.getTaskList();
        for (Task t : tasks) {
            db.delete("task",
                    "id = ?",
                    new String[]{String.valueOf(t.getId())});
        }
        return db.delete("taskgroup",
                "id = ?",
                new String[]{String.valueOf(taskGroup.getId())});
    }
}
