package com.notes.ui.details

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.ui.BUNDLE_KEY_NOTE_ID
import com.notes.ui.RootViewModel
import com.notes.ui._base.ViewBindingFragment

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {

    private val rootViewModel: RootViewModel by activityViewModels()

    private val viewModel: NoteDetailsViewModel by viewModels {
        DependencyManager.noteDetailsViewModelFactory(
            arguments?.getLong(BUNDLE_KEY_NOTE_ID) ?: 0,
        )
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.note.observe(viewLifecycleOwner) {
            it?.let {
                viewBinding.noteTitle.setText(it.title)
                viewBinding.noteDetails.setText(it.content)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveNote(
            viewBinding?.noteTitle?.text.toString(),
            viewBinding?.noteDetails?.text.toString(),
            rootViewModel
        )
    }
}