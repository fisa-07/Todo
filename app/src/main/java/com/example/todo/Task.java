package com.example.todo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "checked",defaultValue = "false") // By default check box is unchecked
    boolean radioButtonValue;

    public boolean isRadioButtonValue() {
        return radioButtonValue;
    }

    public void setRadioButtonValue(boolean radioButtonValue) {
        this.radioButtonValue = radioButtonValue;
    }

    public Task(String note) {
        this.note = note;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
