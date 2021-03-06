package com.notes.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.App
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.ui.BUNDLE_KEY_NOTE_ID
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.details.NoteDetailsFragment
import javax.inject.Inject

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    @Inject
    lateinit var viewModelFactory: NoteListViewModelFactory
    private val viewModel: NoteListViewModel by viewModels { viewModelFactory }

    private val itemClicked = fun(noteId: Long) {
        viewModel.noteClicked(noteId)
    }

    private val deleteItem = fun(note: NoteListItem) {
        viewModel.deleteNote(note)
    }

    private val recyclerViewAdapter = RecyclerViewAdapter(itemClicked, deleteItem)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerViewAdapter.setItems(it)
            }
        }

        viewModel.navigateToNoteDetails.observe(viewLifecycleOwner) {
            it?.let {
                findImplementationOrThrow<FragmentNavigator>()
                    .navigateTo(
                        NoteDetailsFragment().apply {
                            arguments = Bundle().apply {
                                putLong(BUNDLE_KEY_NOTE_ID, it)
                            }
                        }
                    )
            }
        }
    }

    private class RecyclerViewAdapter(
        private val itemClicked: (Long) -> Unit,
        private val deleteItem: (NoteListItem) -> Unit
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked,
            deleteItem
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        @SuppressLint("NotifyDataSetChanged")
        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        private class ViewHolder(
            private val binding: ListItemNoteBinding,
            private val itemClicked: (Long) -> Unit,
            private val deleteItem: (NoteListItem) -> Unit
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {
            fun bind(
                note: NoteListItem
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content

                binding.note.setOnClickListener {
                    itemClicked.invoke(note.id)
                }

                binding.buttonDelete.setOnClickListener {
                    binding.swipeView.close(true)
                    deleteItem.invoke(note)
                }
            }
        }
    }
}