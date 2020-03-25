package ntnk.sample.scheduleproject.entity;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {
    private String title;
    private int boardId;
    private List<Task> taskList;

    public TaskGroup(String title) {
        this.title = title;
        taskList = new ArrayList<>();
    }

    public TaskGroup(String title, List<Task> taskList, int boardId) {
        this.title = title;
        this.taskList = taskList;
        this.boardId = boardId;
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
}
