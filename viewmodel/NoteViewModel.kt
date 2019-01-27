package `in`.jamian.roomdbwithlivedata.viewmodel


import `in`.jamian.roomdbwithlivedata.model.Note
import `in`.jamian.roomdbwithlivedata.model.NoteRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    var repository = NoteRepository(application)
    var allNotes: LiveData<List<Note>> = repository.getAllNotes()

    fun insert(note: Note){
        repository.insert(note)
    }

    fun update(note: Note){
        repository.update(note)
    }

    fun delete(note: Note){
        repository.delete(note)
    }

    fun deleteAll(){
        repository.deleteAllNotes()
    }

    fun getNotes():LiveData<List<Note>> {
        return allNotes
    }


}