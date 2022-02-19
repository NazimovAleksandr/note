package com.notes.di

import android.app.Application
import com.notes.ui.details.NoteDetailsViewModel
import com.notes.ui.details.NoteDetailsViewModelFactory
import com.notes.ui.list.NoteListViewModel

class DependencyManager private constructor(
    application: Application
) {

    companion object {
        private lateinit var instance: DependencyManager
        private var noteListViewModel: NoteListViewModel? = null

        fun init(application: Application) {
            instance = DependencyManager(application)
        }

        fun noteListViewModel() = noteListViewModel ?: synchronized(this) {
            val viewModel = instance.rootComponent.getNoteListViewModel()
            noteListViewModel = viewModel

            viewModel
        }

        fun noteDetailsViewModelFactory(noteId: Long) = NoteDetailsViewModelFactory(
            noteId,
            instance.appComponent.getNoteDatabase()
        )
    }

    private val appComponent = DaggerAppComponent.factory().create(application)

    private val rootComponent = DaggerRootComponent.factory().create(appComponent)

}
