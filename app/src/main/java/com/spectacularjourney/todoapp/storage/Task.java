package com.spectacularjourney.todoapp.storage;

/**
 * Created by jeff on 9/21/15.
 */
public class Task {
    public static final int INVALID_ID = -1;
    public static final int INVALID_POSITION = -1;

    public long id;
    public String name;
    public int position;

    public Task() {
        this(INVALID_ID, null, INVALID_POSITION);
    }

    public Task(String name, int position) {
        this(INVALID_ID, name, position);
    }

    public Task(long id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
