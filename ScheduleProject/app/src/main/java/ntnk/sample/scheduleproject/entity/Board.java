package ntnk.sample.scheduleproject.entity;

import android.graphics.Color;

import java.io.Serializable;

public class Board implements Serializable {
    private int id;
    private String name;
    private int color;

    public Board(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Board() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
