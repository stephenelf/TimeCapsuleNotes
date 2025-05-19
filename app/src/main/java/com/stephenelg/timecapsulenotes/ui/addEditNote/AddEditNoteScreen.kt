package com.stephenelg.timecapsulenotes.ui.addEditNote

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stephenelg.timecapsulenotes.R
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.stephenelg.timecapsulenotes.data.model.PokemonHabitat
import com.stephenelg.timecapsulenotes.ui.NoteCard
import com.stephenelg.timecapsulenotes.ui.theme.TimeCapsuleNotesTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddEditNoteScreen(
    @StringRes topBarTitle: Int,
    onTaskUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { AddEditNoteTopAppBar(topBarTitle, onBack) },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = viewModel::saveTask) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.cd_save_note))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        AddEditNoteContent(
            loading = uiState.isLoading,
            title = uiState.title,
            description = uiState.content,
            onTitleChanged = viewModel::updateTitle,
            onContentChanged = viewModel::updateContent,
            onHabitatChanged = viewModel::updateHabitat,
            onDateChanged = viewModel::updateDate,
            habitats = uiState.habitats,
            modifier = Modifier.padding(paddingValues)
        )

        // Check if the task is saved and call onTaskUpdate event
        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                onTaskUpdate()
            }
        }

        // Check for user messages to display on the screen
        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditNoteContent(
    loading: Boolean,
    title: String,
    description: String,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onHabitatChanged: (String) -> Unit,
    onDateChanged: (Date) -> Unit,
    habitats: List<PokemonHabitat> = emptyList(),
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    val isRefreshing by remember { mutableStateOf(false) }
    val refreshingState = rememberPullToRefreshState()
    if (loading) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = refreshingState,
            onRefresh = { /* DO NOTHING */ },
            content = { }
        )
    } else {
        Column(
            modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSecondary
            )
            OutlinedTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_hint),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall
                    .copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = description,
                onValueChange = onContentChanged,
                placeholder = { Text(stringResource(id = R.string.content_hint)) },
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                colors = textFieldColors
            )
         //   HabitatsDropdownMenu(onHabitatChanged, habitats)
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { },
                label = { Text(stringResource(R.string.do_not_open_until)) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
            if (showDatePicker) {
                Popup(
                    onDismissRequest = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { Date(it) }
                            ?.let { onDateChanged(it) }
                    },
                    alignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 64.dp)
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun HabitatsDropdownMenu(
    onHabitatChanged: (String) -> Unit,
    habitats: List<PokemonHabitat> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedHabitat = remember {
        mutableStateOf("")
    }
    Row {
        OutlinedTextField(
            value = selectedHabitat.value,
            onValueChange = { },
            label = { Text(stringResource(R.string.pokemon_habitat)) },
            readOnly = true,

            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            habitats.forEach { habitatItem ->
                DropdownMenuItem(
                    text = { Text(habitatItem.name) },
                    onClick = {
                        expanded = false
                        selectedHabitat.value = habitatItem.name
                        onHabitatChanged(habitatItem.name)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteTopAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview("Time note capsule details")
@Composable
fun PreviewNoteCard() {
    TimeCapsuleNotesTheme {
        Surface {
            AddEditNoteContent(
                loading = false,
                title = "Test Title",
                description = "Test Content",
                onTitleChanged = {},
                onHabitatChanged = {},
                onDateChanged = {},
                onContentChanged = {})
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


