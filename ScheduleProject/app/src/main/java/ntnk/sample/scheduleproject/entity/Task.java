package ntnk.sample.scheduleproject.entity;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private int id;
    private String title;
    private Date date;
    private String description;
    private int status;
    private int urgent_importance;
    private int groupId;
    private String taskImage;

    public Task() {
    }

    public Task(String title, int groupId) {
        this.id = 1;
        this.title = title;
        this.date = new Date();
        this.description = "";
        this.groupId = groupId;
        this.taskImage = null;
    }

    public Task(int id, String title, Date date, String description, int status, int urgent_importance, int groupId, String taskImage) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.status = status;
        this.urgent_importance = urgent_importance;
        this.groupId = groupId;
        this.taskImage = taskImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUrgent_importance() {
        return urgent_importance;
    }

    public void setUrgent_importance(int urgent_importance) {
        this.urgent_importance = urgent_importance;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }
}