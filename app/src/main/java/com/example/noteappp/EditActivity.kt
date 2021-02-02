package com.example.noteappp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.noteappp.room.Constant
import com.example.noteappp.room.Note
import com.example.noteappp.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this)}
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupListener()
        setupView()

    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType) {
            Constant.TYPE_CREATE -> {

            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }

    }

    fun setupListener() {
        button_save.setOnClickListener() {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    Note(0, edit_title.text.toString(), edit_note.text.toString())
                )
                finish()
            }
        }
    }
    fun getNote(){
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote(noteId)[0]
            edit_title.setText(notes.title)
            edit_note.setText(notes.note)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

