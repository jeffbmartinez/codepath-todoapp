package com.spectacularjourney.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spectacularjourney.todoapp.storage.Task;

import java.util.ArrayList;

/**
 * Created by jeff on 9/23/15.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(task.name);

        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        tvPosition.setText(Integer.toString(task.position));

        return convertView;
    }
}
