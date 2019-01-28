package `in`.jamian.roomdbwithlivedata

import `in`.jamian.roomdbwithlivedata.adapter.NoteAdapter
import `in`.jamian.roomdbwithlivedata.model.Note
import `in`.jamian.roomdbwithlivedata.viewmodel.NoteViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.each_note.*

class MainActivity : AppCompatActivity() {

    //hello jamian

    var noteViewModel: NoteViewModel? = null
    var notes: ArrayList<Note> = ArrayList()
    lateinit var notesAdapter: NoteAdapter

    enum class NOTE_ACTION {
        ADD,
        EDIT
    }

    var noteAction = NOTE_ACTION.ADD
    var editNoteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes.clear()
        notesAdapter = NoteAdapter(notes)
        recyc_notes.layoutManager = LinearLayoutManager(this)
        recyc_notes.adapter = notesAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel?.delete(notesAdapter.getNoteAt(viewHolder.adapterPosition))
            }

        }).attachToRecyclerView(recyc_notes)

        notesAdapter.setItemClickListener(object : NoteAdapter.NoteActions {
            override fun onNoteClicked(note: Note) {
                Toast.makeText(this@MainActivity, "test", Toast.LENGTH_LONG)

                noteAction = NOTE_ACTION.EDIT
                btn_addnote.text = "Update Note"
                editNoteId = note.id

                et_title.setText(note.title)
                et_description.setText(note.description)
                et_priority.setText(note.priority.toString())
                cl_exit_add.visibility = View.VISIBLE

            }
        })

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel?.getNotes()?.observe(this, Observer<List<Note>> {
            Log.d("12345", it.toString())
            notes.clear()
            notes.addAll(it)
            notesAdapter.notifyDataSetChanged()
        })

        btn_add.setOnClickListener {

            et_title.setText("")
            et_description.setText("")
            et_priority.setText("")
            cl_exit_add.visibility = View.VISIBLE
            noteAction = NOTE_ACTION.ADD
            btn_addnote.text = "Add Note"
            editNoteId = -1
        }

        cl_exit_add.setOnClickListener {
            cl_exit_add.visibility = View.GONE
        }

        btn_addnote.setOnClickListener {


            if (noteAction == NOTE_ACTION.ADD) {

                var note = Note(
                    et_title.text.toString(),
                    et_description.text.toString(),
                    Integer.parseInt(et_priority.text.toString())
                )

                noteViewModel?.insert(note)

            }else if(noteAction == NOTE_ACTION.EDIT){

                var note = Note(
                    et_title.text.toString(),
                    et_description.text.toString(),
                    Integer.parseInt(et_priority.text.toString())
                )

                note.id = editNoteId

                if(editNoteId == -1){
                    Toast.makeText(this,"This note could not be saved",Toast.LENGTH_LONG).show()
                }else{
                    noteViewModel?.update(note)
                }
            }
            cl_exit_add.visibility = View.GONE

        }
    }
}
