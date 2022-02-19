package com.notes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notes.data.NoteDatabase

class NoteDetailsViewModelFactory(
    private val noteId: Long,
    private val noteDatabase: NoteDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NoteDetailsViewModel::class.java)) {
            return NoteDetailsViewModel(noteId, noteDatabase) as T
        }

        throw IllegalArgumentException("ViewModel class note found")
    }

}