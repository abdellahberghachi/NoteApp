package com.berghachi.noteapp.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berghachi.noteapp.data.NoteDataSource
import com.berghachi.noteapp.model.Note
import com.berghachi.noteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {


    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNotes().distinctUntilChanged()
                .collect { listOfNotes ->
                    if (listOfNotes.isNullOrEmpty()) {
                        Log.d("Empty", ": Empty list")
                    }else {
                        _noteList.value = listOfNotes
                    }

                }

        }
        // noteList.addAll(NotesDataSource().loadNotes())
    }

    fun addNote(note: Note) = viewModelScope.launch { noteRepository.addNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { noteRepository.updateNote(note) }
    fun removeNote(note: Note) = viewModelScope.launch { noteRepository.deleteNote(note) }

    fun getAllNotes() = noteList
}