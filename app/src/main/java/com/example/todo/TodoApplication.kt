package com.example.todo

import android.app.Application
import com.example.todo.Repository.todoRepository
import com.example.todo.Room.todoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApplication  : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { todoDatabase.getDatabase(this,applicationScope)}
    val repository by lazy { todoRepository(database.getTasksDao()) }
}