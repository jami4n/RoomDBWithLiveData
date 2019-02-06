package `in`.jamian.roomdbwithlivedata.model

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class NoteRepository(application: Application):LifecycleObserver{

    private var noteDao:NoteDao
    private var allNotes:LiveData<List<Note>>

    private var disposable: CompositeDisposable? = null

    init {
        val database:NoteDatabase = NoteDatabase.getInstance(application.applicationContext)!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
        disposable = CompositeDisposable()

    }

    fun insert(note: Note){
        //InsertNoteAsyncTask(noteDao).execute(note)

        disposable?.add(Completable.create{ emitter: CompletableEmitter -> noteDao.insert(note) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun update(note: Note){

        //UpdateNoteAsyncTask(noteDao).execute(note)

        disposable?.add(Completable.create {emitter: CompletableEmitter -> noteDao.update(note) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun delete(note: Note){
        //DeleteNoteAsyncTask(noteDao).execute(note)

        disposable?.add(Completable.create { noteDao.delete(note) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())

    }

    fun deleteAllNotes(){
        //DeleteAllNoteAsyncTask(noteDao).execute()

        disposable?.add(Completable.create { noteDao.deleteAllNotes() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())

    }

    fun getAllNotes():LiveData<List<Note>>{
        return allNotes
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposable?.clear()
    }


    //these asynctasks are used to handle db operations on a seperate thread.
    //these operations will ideally be replaced with rxjava class
    //however to simplify the code sample, i've stuck to using only Architecture components
    //jamian 27 Jan

    //added RXJava implementations for DAO operations
    //below companion object is now Deprecated
    //jamian 6 Feb

    @Deprecated("Added RxJava implementations for handling Async DAO operations")
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