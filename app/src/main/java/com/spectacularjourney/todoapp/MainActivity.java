package com.spectacularjourney.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_OK = 1;

    private static final int REQUEST_CODE_EDIT_TASK = 1001;

    private static final String TODO_STORAGE_FILE = "todo.txt";

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aToDoAdapter;

    private ListView lvItems;
    private EditText etEditText;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editTask = new Intent(parent.getContext(), EditItemActivity.class);
                editTask.putExtra("name", todoItems.get(position));
                editTask.putExtra("position", position);

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

        etEditText = (EditText) findViewById(R.id.etEditText);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
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
        String newTaskName = etEditText.getText().toString();
        aToDoAdapter.add(newTaskName);

        etEditText.setText("");

        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_TASK) {
            String newTaskName = data.getStringExtra("name");
            int position = data.getIntExtra("position", -1);

            if (position == -1) {
                Toast.makeText(this, "Couldn't update task", Toast.LENGTH_SHORT);
                return;
            }

            String toastMessage = todoItems.get(position) + " -> " + newTaskName;

            todoItems.remove(position);
            todoItems.add(position, newTaskName);
            aToDoAdapter.notifyDataSetChanged();

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

            writeItems();
        }
    }

    private void populateArrayItems() {
        readItems();

        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, TODO_STORAGE_FILE);
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException ioe) {
            System.out.println("oops reading");
            System.out.println(ioe);
            todoItems = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, TODO_STORAGE_FILE);
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException ioe) {
            System.out.println("oops writing");
            System.out.println(ioe);
        }
    }
}
