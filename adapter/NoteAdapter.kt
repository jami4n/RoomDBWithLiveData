package `in`.jamian.roomdbwithlivedata.adapter

import `in`.jamian.roomdbwithlivedata.R
import `in`.jamian.roomdbwithlivedata.model.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.each_note.view.*

class NoteAdapter(var notes:List<Note>) : RecyclerView.Adapter<NoteAdapter.MYVH>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYVH {
        return MYVH(LayoutInflater.from(parent.context).inflate(R.layout.each_note,parent,false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    override fun onBindViewHolder(holder: MYVH, position: Int) {
        var note = notes[position]
        holder.tv_title.text = note.title
        holder.tv_description.text = note.description

    }

    fun getNoteAt(position:Int):Note{
        return notes[position]
    }


    class MYVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        var tv_description = itemView.findViewById<TextView>(R.id.tv_description)

    }

    interface NoteActions{
        fun noteLongPressed(note:Note)
        fun noteClicked(note:Note)
    }


}