package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
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

    val notes: LiveData<List<NoteListItem>?> by lazy { MutableLiveData() }
    val navigateToNoteDetails: LiveData<Long?> by lazy { MutableLiveData() }

    init {
        viewModelScope.launch {
            noteDatabase.noteDao().getAll().collect {
                (notes as MutableLiveData).postValue(
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
        (navigateToNoteDetails as MutableLiveData).value = -1
        clearNavigateTo()
    }

    fun deleteNote(note: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteNote(note.toNoteDbo())
        }
    }

    fun noteClicked(noteId: Long) {
        (navigateToNoteDetails as MutableLiveData).value = noteId
        clearNavigateTo()
    }

    private fun clearNavigateTo() {
        (navigateToNoteDetails as MutableLiveData).value = null
    }
}

data class NoteListItem(
    val id: Long,
    var title: String,
    var content: String,
    val createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime,
)