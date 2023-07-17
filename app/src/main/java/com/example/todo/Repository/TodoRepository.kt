package com.example.todo.Repository

import androidx.annotation.WorkerThread
import com.example.todo.Model.Todo
import com.example.todo.Room.TodoDao
import kotlinx.coroutines.flow.Flow

class todoRepository(private val todoDao : TodoDao) {
    val myAllTasks : Flow<List<Todo>> = todoDao.getAllTasks()

    @WorkerThread
    suspend fun insert(todo : Todo) {
        todoDao.insert(todo)
    }

    @WorkerThread
    suspend fun update(todo : Todo){
        todoDao.update(todo)
    }

    @WorkerThread
    suspend fun delete(todo : Todo){
        todoDao.delete(todo)
    }

    @WorkerThread
    suspend fun deleteAll(){
        todoDao.deleteAll()
    }

    @WorkerThread
    fun getTasks(searchTask : String) : Flow<List<Todo>>{
        return todoDao.getTasks(searchTask)
    }



}