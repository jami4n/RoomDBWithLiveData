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

    var noteViewModel: NoteViewModel? = null
    var notes:ArrayList<Note> = ArrayList()
    lateinit var notesAdapter:NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes.clear()
        notesAdapter = NoteAdapter(notes)
        recyc_notes.layoutManager = LinearLayoutManager(this)
        recyc_notes.adapter = notesAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

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

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel?.getNotes()?.observe(this, Observer<List<Note>>{
            Log.d("12345",it.toString())
            notes.clear()
            notes.addAll(it)
            notesAdapter.notifyDataSetChanged()
        })

        btn_add.setOnClickListener {
            cl_exit_add.visibility = View.VISIBLE
        }

        cl_exit_add.setOnClickListener {
            cl_exit_add.visibility = View.GONE
        }

        btn_addnote.setOnClickListener {
            var note = Note(et_title.text.toString(),et_description.text.toString(),Integer.parseInt(et_priority.text.toString()))
            noteViewModel?.insert(note)

            et_title.setText("")
            et_description.setText("")
            et_priority.setText("")
            cl_exit_add.visibility = View.GONE

        }
    }
}
