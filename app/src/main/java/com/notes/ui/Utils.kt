package com.notes.ui

import com.notes.data.NoteDbo
import com.notes.ui.list.NoteListItem

fun NoteDbo.toNoteListItem() = NoteListItem(
    id = this.id,
    title = this.title,
    content = this.content,
    createdAt = this.createdAt,
    modifiedAt = this.modifiedAt
)

fun NoteListItem.toNoteDbo() = NoteDbo(
    id = this.id,
    title = this.title,
    content = this.content,
    createdAt = this.createdAt,
    modifiedAt = this.modifiedAt
)