package com.example.todo

import androidx.lifecycle.*
import com.example.todo.Model.Todo
import com.example.todo.Repository.todoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: todoRepository) : ViewModel() {
    val myAllTasks : LiveData<List<Todo>> = repository.myAllTasks.asLiveData()

    fun insert(todo : Todo) = viewModelScope.launch (Dispatchers.IO) {
        repository.insert(todo)
    }

    fun update(todo : Todo) = viewModelScope.launch (Dispatchers.IO) {
        repository.update(todo)
    }

    fun delete(todo : Todo) = viewModelScope.launch (Dispatchers.IO) {
        repository.delete(todo)
    }

    fun deleteAll() = viewModelScope.launch (Dispatchers.IO) {
        repository.deleteAll()
    }

    fun searchTask(searchT : String) : LiveData<List<Todo>>{
        return repository.getTasks(searchT).asLiveData()
    }


}

class TodoViewModelFactory(private val repository: todoRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(repository) as T
        }
        else{
            throw IllegalArgumentException("Unknown View Model")
        }
        return super.create(modelClass)
    }
}

