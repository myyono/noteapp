package com.example.noteappp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteappp.room.Constant
import com.example.noteappp.room.Note
import com.example.noteappp.room.NoteDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this)}
    lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()
    }
    override  fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity", "dbRespone: $notes")
            withContext(Dispatchers.Main){
                noteAdapter.SetData( notes )
            }
        }
    }

    fun setupListener(){
        button_create.setOnClickListener(){
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId: Int, intentType: Int){
        startActivity(
                Intent(applicationContext, EditActivity::class.java)
                        .putExtra("intent_id", noteId)
                        .putExtra("intent_type", intentType)
        )
    }
    private fun setupRecyclerView(){
        noteAdapter = NoteAdapter(arrayListOf(),object: NoteAdapter.OnAdapterlistener{
            override fun onClick(note: Note) {
//                Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
                intentEdit(note.id, Constant.TYPE_READ)
            }

        })
        list_note.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }
}