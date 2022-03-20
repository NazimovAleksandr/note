package com.notes.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notes.data.NoteDatabase
import javax.inject.Inject

class NoteListViewModelFactory @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(noteDatabase) as T
        }

        throw IllegalArgumentException("ViewModel class note found")
    }

}