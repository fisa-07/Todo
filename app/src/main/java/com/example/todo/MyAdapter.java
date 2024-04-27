package com.example.todo;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    AppDatabase database;
    List<Task> taskList;
    Activity context;

    public MyAdapter(AppDatabase database, List<Task> taskList,Activity context) {
        this.database = database;
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(taskList.get(position).getNote());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BgThreadOnDelete(position).start();
            }
        });

        holder.radioButton.setChecked(taskList.get(position).isRadioButtonValue());

        // Add a check listener to the list item itself to handle unchecking
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.radioButton.setChecked(b);
                taskList.get(position).setRadioButtonValue(b);
                // Update database
                new BgThreadOnCheck(position).start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox radioButton;
        ImageButton imageButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            radioButton = itemView.findViewById(R.id.radio_button);
            imageButton = itemView.findViewById(R.id.delete_button);
        }
    }


    // Background thread for deleting the note from db on click of imageButton
    public class BgThreadOnDelete extends Thread{
        int position;
        BgThreadOnDelete(int position){
            this.position = position;
        }
        @Override
        public void run() {
            super.run();

            TaskDao taskDao = database.taskDao();
            taskDao.delete(taskList.get(position));


            // Remove from recyclerview
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    taskList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }


    // Background thread for updating the checkbox in db
    public class BgThreadOnCheck extends Thread{
        int position;
        BgThreadOnCheck(int position){
            this.position = position;
        }
        @Override
        public void run() {
            super.run();
            TaskDao taskDao = database.taskDao();
            taskDao.update(taskList.get(position));

        }
    }
}
