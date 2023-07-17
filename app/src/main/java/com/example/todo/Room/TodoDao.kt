package com.example.todo.Room

import androidx.room.*
import com.example.todo.Model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("DELETE FROM db_todo")
    suspend fun deleteAll()

    @Query("SELECT * FROM db_todo ORDER BY taskDate+taskTime ASC")
    fun getAllTasks() : Flow<List<Todo>>

    @Query("SELECT * FROM db_todo WHERE taskTitle LIKE :searchTask")
    fun getTasks(searchTask : String) : Flow<List<Todo>>

}