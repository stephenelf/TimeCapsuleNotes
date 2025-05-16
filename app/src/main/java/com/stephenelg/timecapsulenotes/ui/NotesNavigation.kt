package com.stephenelg.timecapsulenotes.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.NOTE_ID_ARG
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.TITLE_ARG
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.stephenelg.timecapsulenotes.ui.TodoScreens.ADD_EDIT_NOTE_SCREEN
import com.stephenelg.timecapsulenotes.ui.TodoScreens.NOTES_SCREEN
import com.stephenelg.timecapsulenotes.ui.TodoScreens.NOTE_DETAIL_SCREEN

/**
 * Screens used in [TodoDestinations]
 */
private object TodoScreens {
    const val NOTES_SCREEN = "notes"
    const val NOTE_DETAIL_SCREEN = "note"
    const val ADD_EDIT_NOTE_SCREEN = "addEditNote"
}

/**
 * Arguments used in [TodoDestinations] routes
 */
object TodoDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val NOTE_ID_ARG = "noteId"
    const val TITLE_ARG = "title"
}

/**
 * Destinations used in the [TodoActivity]
 */
object TodoDestinations {
    const val NOTES_ROUTE = "$NOTES_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val NOTE_DETAIL_ROUTE = "$NOTE_DETAIL_SCREEN/{$NOTE_ID_ARG}"
    const val ADD_EDIT_NOTE_ROUTE = "$ADD_EDIT_NOTE_SCREEN/{$TITLE_ARG}?$NOTE_ID_ARG={$NOTE_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToNotes(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            NOTES_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }


    fun navigateToNoteDetail(noteId: String) {
        navController.navigate("$NOTE_DETAIL_SCREEN/$noteId")
    }

    fun navigateToAddEditNote(title: Int, taskId: String?) {
        navController.navigate(
            "$ADD_EDIT_NOTE_SCREEN/$title".let {
                if (taskId != null) "$it?$NOTE_ID_ARG=$taskId" else it
            }
        )
    }
}
