package com.notes.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteDbo>>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Update
    fun updateNote(note: NoteDbo)

    @Delete
    fun deleteNote(note: NoteDbo)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteByID(id: Long): NoteDbo

    @Query("SELECT * FROM notes ORDER BY id DESC LIMIT 1")
    fun getLastNote(): NoteDbo
}