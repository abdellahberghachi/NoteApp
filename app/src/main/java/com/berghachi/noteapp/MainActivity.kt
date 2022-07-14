package com.berghachi.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.berghachi.noteapp.screen.NoteScreen
import com.berghachi.noteapp.screen.NoteViewModel
import com.berghachi.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val noteViewModel: NoteViewModel by viewModels()
                NotesApp(noteViewModel)
            }
        }
    }
}

@Composable
fun NotesApp(noteViewModel: NoteViewModel) {
    val noteList = noteViewModel.getAllNotes().collectAsState().value
    NoteScreen(noteList, onAddNote = {
        noteViewModel.addNote(it)
    }, onRemoveNote = {
        noteViewModel.removeNote(it)
    })
}

@androidx.compose.runtime.Composable
fun MyApp(content: @Composable () -> Unit) {
    NoteAppTheme {
        content()
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NoteAppTheme {

    }
}