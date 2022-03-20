package com.notes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import com.notes.ui.list.NoteListItem
import com.notes.ui.toNoteDbo
import com.notes.ui.toNoteListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NoteDetailsViewModel(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    val note: LiveData<NoteListItem?> by lazy { MutableLiveData() }

    fun saveNote(title: String, content: String) {
        if (note.value != null) {
            updateNote(title, content)
        } else {
            if (title != "" || content != "") {
                createNewNote(title, content)
            }
        }
    }

    fun openNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            (note as MutableLiveData).postValue(
                noteDatabase.noteDao().getNoteByID(id)?.toNoteListItem()
            )
        }
    }

    private fun updateNote(title: String, content: String) {
        val note = this.note.value!!

        if (note.title != title || note.content != content) {
            note.title = title
            note.content = content
            note.modifiedAt = LocalDateTime.now()

            viewModelScope.launch(Dispatchers.IO) {
                noteDatabase.noteDao().updateNote(note.toNoteDbo())
            }
        }
    }

    private fun createNewNote(title: String, content: String) {
        val noteTitle = if (title != "") title else "Title"

        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertAll(
                NoteDbo(
                    title = noteTitle,
                    content = content,
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                )
            )
        }
    }
}