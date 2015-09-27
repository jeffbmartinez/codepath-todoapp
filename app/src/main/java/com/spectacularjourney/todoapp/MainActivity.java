package com.spectacularjourney.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.spectacularjourney.todoapp.storage.Task;
import com.spectacularjourney.todoapp.storage.TasksDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_OK = 1;

    private static final int REQUEST_CODE_EDIT_TASK = 1001;

    private ArrayList<Task> todoItems;
    private TasksAdapter aToDoAdapter;

    private EditText etTaskName;
    private EditText etDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editTask = new Intent(parent.getContext(), EditItemActivity.class);
                editTask.putExtra("name", todoItems.get(position).name);
                editTask.putExtra("position", position);
                editTask.putExtra("dueDate", todoItems.get(position).dueDate);

                startActivityForResult(editTask, REQUEST_CODE_EDIT_TASK);
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        etTaskName = (EditText) findViewById(R.id.etTaskName);
        etDueDate = (EditText) findViewById(R.id.etDueDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        String newTaskName = etTaskName.getText().toString();

        long newDueDate = -1;
        try {
            newDueDate = Long.parseLong(etDueDate.getText().toString(), 10);
        } catch (Exception e) {
            Toast.makeText(this, "Due Date isn't an integer", Toast.LENGTH_SHORT).show();
        }

        int newPosition = aToDoAdapter.getCount();
        aToDoAdapter.add(new Task(newTaskName, newPosition, newDueDate));

        etTaskName.setText("");
        etDueDate.setText("");

        etTaskName.requestFocus();

        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_TASK) {
            String newTaskName = data.getStringExtra("name");

            int position = data.getIntExtra("position", -1);
            if (position == -1) {
                Toast.makeText(this, "Couldn't update task, bad position", Toast.LENGTH_SHORT).show();
                return;
            }

            long dueDate = data.getLongExtra("dueDate", -1);
            if (dueDate == -1) {
                Toast.makeText(this, "Couldn't update task, bad due date", Toast.LENGTH_SHORT).show();
                return;
            }

            String toastMessage = todoItems.get(position).name + " -> " + newTaskName;

            todoItems.remove(position);
            todoItems.add(position, new Task(newTaskName, position, dueDate));
            aToDoAdapter.notifyDataSetChanged();

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

            writeItems();
        }
    }

    private void populateArrayItems() {
        readItems();

        aToDoAdapter = new TasksAdapter(this, todoItems);
    }

    private void readItems() {
        List<Task> tasks = TasksDatabaseHelper.getInstance(this).getAllTasks();

        todoItems = new ArrayList<>();
        for (Task task: tasks) {
            todoItems.add(task);
        }
    }

    private void writeItems() {
        TasksDatabaseHelper.getInstance(this).deleteAllTasks();

        for (int i = 0; i < todoItems.size(); i++) {
            Task task = todoItems.get(i);
            task.position = i;

            TasksDatabaseHelper.getInstance(this).addOrUpdateTask(task);
        }
    }
}
