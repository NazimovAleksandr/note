package com.notes.ui.details

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.notes.App
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.ui.BUNDLE_KEY_NOTE_ID
import com.notes.ui._base.ViewBindingFragment
import javax.inject.Inject

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {

    @Inject
    lateinit var viewModelFactory: NoteDetailsViewModelFactory
    private val viewModel: NoteDetailsViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent.inject(this)

        arguments?.let {
            viewModel.openNote(it.getLong(BUNDLE_KEY_NOTE_ID))
        }
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
            viewBinding?.noteDetails?.text.toString()
        )
    }
}