package `in`.jamian.roomdbwithlivedata.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase(): RoomDatabase() {

    abstract fun noteDao():NoteDao


    companion object {
        private var instance:NoteDatabase? = null

        fun getInstance(context:Context):NoteDatabase?{

            if(instance == null){
                synchronized(NoteDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }

            return instance
        }

        private val roomCallback:RoomDatabase.Callback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                PopulateDatabaseAsyncTask(instance).execute()
            }
        }
    }

    class PopulateDatabaseAsyncTask(noteDatabase: NoteDatabase?): AsyncTask<Unit, Unit, Unit>() {

        var noteDao = noteDatabase?.noteDao()

        override fun doInBackground(vararg params: Unit?) {
            noteDao?.insert(Note("Note 1","Description 1", 1))
            noteDao?.insert(Note("Note 2","Description 2", 1))
            noteDao?.insert(Note("Note 3","Description 3", 1))
        }

    }


}