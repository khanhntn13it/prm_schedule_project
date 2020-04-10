package ntnk.sample.scheduleproject.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskGroup implements Serializable {
    private String title;
    private int boardId;
    private List<Task> taskList;
    private int id;


    public TaskGroup(String title) {
        this.title = title;
        taskList = new ArrayList<>();
    }

    public TaskGroup() {
    }

    public TaskGroup(String title, int boardId) {
        this.title = title;
        this.boardId = boardId;
        this.taskList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
