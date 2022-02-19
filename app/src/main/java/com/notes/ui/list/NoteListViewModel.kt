package com.notes.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import com.notes.ui.toNoteDbo
import com.notes.ui.toNoteListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {
    private val TAG = NoteListViewModel::class.java.simpleName

    private val _notes = MutableLiveData<List<NoteListItem>?>()
    val notes: LiveData<List<NoteListItem>?> = _notes

    private val _navigateToNoteCreation = MutableLiveData<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation

    val navigateToNoteSelected: LiveData<Long?> by lazy { MutableLiveData() }

    init {
        Log.e(TAG, "init")

        viewModelScope.launch {
            noteDatabase.noteDao().getAll().collect {
                _notes.postValue(
                    it.map { dbo ->
                        dbo.toNoteListItem()
                    }.sortedByDescending { listItem ->
                        listItem.modifiedAt
                    }
                )
            }
        }
    }

    fun updateLise() {
        Log.e(TAG, "updateLise")

        viewModelScope.launch {
            noteDatabase.noteDao().getAll().collect {
                _notes.postValue(
                    it.map { dbo ->
                        dbo.toNoteListItem()
                    }.sortedByDescending { listItem ->
                        listItem.modifiedAt
                    }
                )
            }
        }
    }

    fun onCreateNoteClick() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertAll(
                NoteDbo(
                    title = "Note",
                    content = "",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                )
            )
        }

        _navigateToNoteCreation.value = Unit
        _navigateToNoteCreation.value = null
    }

    fun deleteNote(note: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteNote(note.toNoteDbo())
        }
    }

    fun noteClicked(noteId: Long) {
        (navigateToNoteSelected as MutableLiveData).value = noteId
        (navigateToNoteSelected as MutableLiveData).value = null
    }

}

data class NoteListItem(
    val id: Long,
    var title: String,
    var content: String,
    val createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime,
)