package com.notes.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.ui.RootViewModel
import com.notes.ui.list.NoteListItem
import com.notes.ui.toNoteDbo
import com.notes.ui.toNoteListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NoteDetailsViewModel(
    noteId: Long,
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val TAG = NoteDetailsViewModel::class.java.simpleName

    val note: LiveData<NoteListItem?> by lazy { MutableLiveData() }

    init {
        Log.e(TAG, "$noteId")

        viewModelScope.launch(Dispatchers.IO) {
            (note as MutableLiveData).postValue(
                if (noteId == 0L) {
                    noteDatabase.noteDao().getLastNote().toNoteListItem()
                } else {
                    noteDatabase.noteDao().getNoteByID(noteId).toNoteListItem()
                }
            )
        }
    }

    fun saveNote(title: String, content: String, rootViewModel: RootViewModel) {
        note.value?.let {
            if (it.title != title || it.content != content) {
                it.title = title
                it.content = content
                it.modifiedAt = LocalDateTime.now()

                viewModelScope.launch(Dispatchers.IO) {
                    noteDatabase.noteDao().updateNote(it.toNoteDbo())
                }

                rootViewModel.updateList()
            }
        }
    }
}