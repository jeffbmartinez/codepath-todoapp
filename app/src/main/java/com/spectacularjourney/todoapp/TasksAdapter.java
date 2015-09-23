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

    private static class ViewHolder {
        TextView name;
        TextView position;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.position = (TextView) convertView.findViewById(R.id.tvPosition);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(task.name);
        viewHolder.position.setText(Integer.toString(task.position));

        return convertView;
    }
}
