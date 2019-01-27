package `in`.jamian.roomdbwithlivedata.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application){

    private var noteDao:NoteDao
    private var allNotes:LiveData<List<Note>>

    init {

        val database:NoteDatabase = NoteDatabase.getInstance(application.applicationContext)!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()

    }

    fun insert(note: Note){
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note){
        UpdateNoteAsyncTask(noteDao).execute(note)

    }

    fun delete(note: Note){
        DeleteNoteAsyncTask(noteDao).execute(note)

    }

    fun deleteAllNotes(){
        DeleteAllNoteAsyncTask(noteDao).execute()

    }

    fun getAllNotes():LiveData<List<Note>>{

        return allNotes
    }

    //these asynctasks are used to handle db operations on a seperate thread.
    //these operations will ideally be replaced with rxjava class
    //however to simplify the code sample, i've stuck to using on Architecture components
    //jamian 27 Jan

    companion object {

        private class InsertNoteAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Unit,Unit>(){
            override fun doInBackground(vararg params: Note?) {
                noteDao.insert(params[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Unit,Unit>(){
            override fun doInBackground(vararg params: Note?) {
                noteDao.update(params[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(val noteDao: NoteDao):AsyncTask<Note,Unit,Unit>(){
            override fun doInBackground(vararg params: Note?) {
                noteDao.delete(params[0]!!)
            }
        }

        private class DeleteAllNoteAsyncTask(val noteDao: NoteDao):AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }


}