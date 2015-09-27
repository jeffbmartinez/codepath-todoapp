package com.spectacularjourney.todoapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff on 9/21/15.
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper {
    private static TasksDatabaseHelper sInstance;

    private static final String TAG = "TasksDatabaseHelper";

    public static final String DATABASE_NAME = "tasksDatabase";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_TASKS = "tasks";
    public static final String KEY_TASK_ID = "id";
    public static final String KEY_TASK_NAME = "name";
    public static final String KEY_TASK_POSITION = "position";
    public static final String KEY_TASK_DUE_DATE = "dueDate";

    private TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TasksDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TasksDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY," +
                    KEY_TASK_POSITION + " INTEGER," +
                    KEY_TASK_NAME + " TEXT," +
                    KEY_TASK_DUE_DATE + " INTEGER" +
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

            onCreate(db);
        }
    }

    public long addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        long newTaskId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_NAME, task.name);
            values.put(KEY_TASK_POSITION, task.position);
            values.put(KEY_TASK_DUE_DATE, task.dueDate);

            newTaskId = db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add ask to the database");
        } finally {
            db.endTransaction();
        }

        return newTaskId;
    }

    public long updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", task.name);
        values.put("position", task.position);
        values.put("dueDate", task.dueDate);

        return (long) db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[]{
                String.valueOf(task.id)
        });
    }

    public long addOrUpdateTask(Task task) {
        if (updateTask(task) == 1) {
            return task.id;
        }

        return addTask(task);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        String TASKS_SELECT_QUERY =
                String.format("SELECT %s, %s, %s, %s FROM %s ORDER BY %s ASC",
                        KEY_TASK_ID,
                        KEY_TASK_NAME,
                        KEY_TASK_POSITION,
                        KEY_TASK_DUE_DATE,
                        TABLE_TASKS,
                        KEY_TASK_POSITION
                );

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);

        try {
            if (!cursor.moveToFirst()) {
                return tasks;
            }

            do {
                Task task = new Task();
                task.id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TASK_ID));
                task.name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TASK_NAME));
                task.position = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TASK_POSITION));
                task.dueDate = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TASK_DUE_DATE));
                tasks.add(task);
            } while (cursor.moveToNext());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to read tasks from the database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return tasks;
    }

    public int deleteAllTasks() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TASKS, null, null);
    }
}
