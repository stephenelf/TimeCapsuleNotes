package com.stephenelg.timecapsulenotes.ui.notesList

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stephenelg.timecapsulenotes.R
import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote
import com.stephenelg.timecapsulenotes.ui.LoadingContent
import com.stephenelg.timecapsulenotes.ui.NoteCard
import com.stephenelg.timecapsulenotes.ui.TasksTopAppBar
import com.stephenelg.timecapsulenotes.ui.theme.TimeCapsuleNotesTheme
import java.util.Date

@Composable
fun NotesListScreen(
    @StringRes userMessage: Int,
    onAddTask: () -> Unit,
    onNoteClick: (String) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TasksTopAppBar(
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.app_name))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Log.d("NotesListsScreen", "NotesListScreen: ${uiState.notes}")
        Log.d("NotesListsScreen", "NotesListScreen: ${uiState.isLoading}")
        NotesContent(
            loading = uiState.isLoading,
            notes = uiState.notes,
            onRefresh = viewModel::refresh,
            onNoteClick = onNoteClick,
            noTasksLabel = R.string.label_no_notes,
            noTasksIconRes = R.drawable.ic_launcher_foreground,
            modifier = Modifier.padding(paddingValues)
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        // Check if there's a userMessage to show to the user
        val currentOnUserMessageDisplayed by rememberUpdatedState(onUserMessageDisplayed)
        LaunchedEffect(userMessage) {
            if (userMessage != 0) {
                viewModel.showEditResultMessage(userMessage)
                currentOnUserMessageDisplayed()
            }
        }
    }
}

@Composable
private fun NotesContent(
    loading: Boolean,
    notes: List<TimeCapsuleNote>,
    onRefresh: () -> Unit,
    onNoteClick: (String) -> Unit,
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = notes.isEmpty() && !loading,
        emptyContent = { NotesEmptyContent(noTasksLabel, noTasksIconRes, modifier) },
        onRefresh = onRefresh
    ) {
        Log.d("NotesContent", "NotesContent: $notes")
        Log.d("NotesContent", "loading: $loading")
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            /*
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.list_item_padding),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.headlineSmall
            )

             */
            LazyColumn (
                Modifier.fillMaxSize(), state = rememberLazyListState(),
                PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp

                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                ){
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        id=note.id,
                        title = note.title ?: "No Title",
                        expires = note.content ?: "No Content",
                        onNoteClick = onNoteClick
                    )
                }
            }
        }
    }
}


@Composable
private fun NotesEmptyContent(
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    modifier: Modifier = Modifier
) {
    Log.d("NotesEmptyContent", "no notes")
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noTasksIconRes),
            contentDescription = stringResource(R.string.no_notes_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = noTasksLabel))
    }
}

@Preview("Time note capsule details")
@Composable
fun PreviewNotesListScreen() {
    TimeCapsuleNotesTheme {
        Surface {
            NotesContent(false, listOf(TimeCapsuleNote("1","title","content", date = Date())), {}, {}, R.string.label_no_notes, R.drawable.ic_launcher_foreground)
        }
    }
}
