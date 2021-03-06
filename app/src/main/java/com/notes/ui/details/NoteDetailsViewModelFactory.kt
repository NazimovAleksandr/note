package com.notes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notes.data.NoteDatabase
import javax.inject.Inject

class NoteDetailsViewModelFactory @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NoteDetailsViewModel::class.java)) {
            return NoteDetailsViewModel(noteDatabase) as T
        }

        throw IllegalArgumentException("ViewModel class note found")
    }

}