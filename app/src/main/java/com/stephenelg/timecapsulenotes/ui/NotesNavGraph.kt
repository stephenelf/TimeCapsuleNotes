package com.stephenelg.timecapsulenotes.ui

import android.app.Activity
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stephenelg.timecapsulenotes.R
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.NOTE_ID_ARG
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.TITLE_ARG
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.stephenelg.timecapsulenotes.ui.addEditNote.AddEditNoteScreen
import com.stephenelg.timecapsulenotes.ui.notesList.NotesListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotesNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = TodoDestinations.NOTES_ROUTE,
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            TodoDestinations.NOTES_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->
            AppModalDrawer(drawerState, currentRoute, navActions) {
                NotesListScreen(
                    userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                    onUserMessageDisplayed = { entry.arguments?.putInt(USER_MESSAGE_ARG, 0) },
                    onAddTask = { navActions.navigateToAddEditNote(R.string.add_note, null) },
                    onNoteClick = { noteId -> navActions.navigateToNoteDetail(noteId) },
                    openDrawer = { coroutineScope.launch { drawerState.open() } }
                )
            }
        }
        composable(
            TodoDestinations.ADD_EDIT_NOTE_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(NOTE_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val noteId = entry.arguments?.getString(NOTE_ID_ARG)

            AddEditNoteScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onTaskUpdate = {
                    navActions.navigateToNotes(
                        if (noteId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onBack = { navController.popBackStack() }
            )

        }
        composable(TodoDestinations.NOTE_DETAIL_ROUTE) {

            /*
            TaskDetailScreen(
                onEditTask = { taskId ->
                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
                },
                onBack = { navController.popBackStack() },
                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK) }
            )

             */
        }
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3