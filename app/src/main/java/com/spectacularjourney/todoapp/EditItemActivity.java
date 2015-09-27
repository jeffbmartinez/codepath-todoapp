package com.spectacularjourney.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    public static final int RESULT_BAD = 0;
    public static final int RESULT_OK = 1;

    private int taskPosition = -1;

    private EditText etNewTaskName;
    private EditText etNewDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String originalTaskName = getIntent().getStringExtra("name");
        taskPosition = getIntent().getIntExtra("position", -1);
        long taskDueDate = getIntent().getLongExtra("dueDate", -1);

        etNewTaskName = (EditText) findViewById(R.id.etNewTaskName);
        etNewTaskName.setText(originalTaskName);
        etNewTaskName.requestFocus();

        etNewDueDate = (EditText) findViewById(R.id.etNewDueDate);
        etNewDueDate.setText(Long.toString(taskDueDate));

        if (taskPosition == -1) {
            setResult(RESULT_BAD);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View view) {
        String newTaskName = etNewTaskName.getText().toString();

        try {
            long newDueDate = Long.parseLong(etNewDueDate.getText().toString(), 10);

            Intent data = new Intent();
            data.putExtra("name", newTaskName);
            data.putExtra("position", taskPosition);
            data.putExtra("dueDate", newDueDate);
            setResult(RESULT_OK, data);
        } catch (Exception e) {
            setResult(RESULT_BAD);
        }

        this.finish();
    }
}
