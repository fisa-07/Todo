package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton addTaskBtn;
    RecyclerView recyclerView;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addTaskBtn = findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);
        taskList = new ArrayList<>();
        new BgThreadOnLoad().start();


        // Redirect to AddTaskActivity
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddTaskActivity.class));
            }
        });
        //


    }


    // Load data every time activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        new BgThreadOnLoad().start();
    }
    //


    // Background thread for loading data from db to arraylist so we can represent it in recyclerview
    public class BgThreadOnLoad extends Thread{
        @Override
        public void run() {
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "task-db").build();

            TaskDao taskDao = db.taskDao();

            taskList = taskDao.getAll();


            // Use to modify recyclerview
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(new MyAdapter(db,taskList,MainActivity.this));
                }
            });
            //


        }
    }
    //


}