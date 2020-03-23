package ntnk.sample.scheduleproject;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {
    private String title;
    private List<Task> taskList;

    public TaskGroup(String title) {
        this.title = title;
        taskList = new ArrayList<>();

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
