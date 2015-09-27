package com.spectacularjourney.todoapp.storage;

/**
 * Created by jeff on 9/21/15.
 */
public class Task {
    public static final int INVALID_ID = -1;
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_DUE_DATE = -1;

    public long id;
    public String name;
    public int position;
    public long dueDate;

    public Task() {
        this(INVALID_ID, null, INVALID_POSITION, INVALID_DUE_DATE);
    }

    public Task(String name, int position, long dueDate) {
        this(INVALID_ID, name, position, dueDate);
    }

    public Task(long id, String name, int position, long dueDate) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.dueDate = dueDate;
    }
}
