package com.berghachi.noteapp.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.berghachi.noteapp.R
import com.berghachi.noteapp.components.NoteButton
import com.berghachi.noteapp.components.NoteInputText
import com.berghachi.noteapp.data.NoteDataSource
import com.berghachi.noteapp.model.Note
import com.berghachi.noteapp.utils.formatDate


@Composable
fun NoteScreen(notes: List<Note>, onAddNote: (Note) -> Unit, onRemoveNote: (Note) -> Unit) {

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.LightGray,
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
            })
    }, content = {
        Column {
            Form {
                onAddNote(it)
            }
            Divider(modifier = Modifier.padding(10.dp))
            NoteList(notes) {
                onRemoveNote(it)
            }
        }
    })
}

@Composable
fun NoteList(list: List<Note>, onRemoveNote: (Note) -> Unit) {

    LazyColumn {
        items(list) { note ->
            NoteRow(note = note, onNoteClick = {
                onRemoveNote(it)
            })
        }
    }
}

@Composable
fun NoteRow(modifier: Modifier = Modifier, note: Note, onNoteClick: (Note) -> Unit) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(
                shape = RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp)
            )
            .fillMaxWidth(),
        color = Color(0xFFDFE6EB),
        elevation = 6.dp
    ) {
        Column(
            modifier = modifier
                .clickable { onNoteClick(note) }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = note.title, style = MaterialTheme.typography.subtitle2)

            Text(text = note.description, style = MaterialTheme.typography.subtitle1)

            Text(text = formatDate(note.entryDate.time), style = MaterialTheme.typography.caption)
        }

    }


}

@Composable
fun Form(onAddNote: (Note) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoteInputText(
            modifier = Modifier.padding(top = 9.dp, bottom = 8.dp),
            text = title,
            label = "Title",
            onTextChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace()
                    }) title = it

            })
        NoteInputText(
            modifier = Modifier.padding(top = 9.dp, bottom = 8.dp),
            text = description,
            label = "Add Note ",
            onTextChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace()
                    }) description = it

            })

        NoteButton(text = "Save", onClick = {
            if (title.isNotEmpty() && description.isNotEmpty()) {
                onAddNote(Note(title = title, description = description))
                title = ""
                description = ""
                Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show()
            }
        })

    }


}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    NoteScreen(NoteDataSource().loadNotes(), {}, {})
}