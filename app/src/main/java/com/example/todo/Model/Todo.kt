package com.example.todo.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_todo")
class Todo(val taskTitle : String, val taskTime : String, val taskDate : String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    //val timeDate : String = "$taskTime $taskDate"
}