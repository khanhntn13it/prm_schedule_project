package ntnk.sample.scheduleproject.entity;

import java.util.Date;

public class Task {
    private int id;
    private String title;
    private Date date;
    private String description;
    private int status;
    private int urgent_importance;
    private int boardId;

    public Task() {
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

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
}
