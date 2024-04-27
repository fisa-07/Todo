package com.example.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddTaskActivity extends AppCompatActivity {
    EditText editText;
    Button saveBtn;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editText = findViewById(R.id.edit_text);
        saveBtn = findViewById(R.id.save_button);


        // Get text from EditText;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString().trim();
                if(s.isEmpty()){
                    Toast.makeText(AddTaskActivity.this, "Empty note can't be saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    task = new Task(s);
                    new BgThreadOnsaveBtn().start();
                    editText.setText("");
                    Toast.makeText(AddTaskActivity.this, "Inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
    }


    // Background thread for putting the task to db (execute on click of saveBtn )
    public class BgThreadOnsaveBtn extends Thread{
        @Override
        public void run() {
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "task-db").build();

            TaskDao taskDao = db.taskDao();

            taskDao.insert(task);
        }
    }
    //

}