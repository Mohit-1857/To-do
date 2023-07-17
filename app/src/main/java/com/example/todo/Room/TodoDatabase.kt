package com.example.todo.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.Model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Todo::class] , version = 1)

abstract class todoDatabase : RoomDatabase() {
    abstract fun getTasksDao() : TodoDao

    companion object{
        @Volatile
        private var INSTANCE : todoDatabase? = null

        fun getDatabase(context: Context , applicationScope: CoroutineScope): todoDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    todoDatabase::class.java,"todo_Database").build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class NoteDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                //database.getNotesDao().insert(Note("t"))
                scope.launch {
                    val taskDao = database.getTasksDao()
                    taskDao.insert(Todo("Task " , "Time" , "Date" ) )
                }
            }
        }
    }

}