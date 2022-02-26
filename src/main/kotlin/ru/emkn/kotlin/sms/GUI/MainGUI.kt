package ru.emkn.kotlin.sms.GUI

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState
import ru.emkn.kotlin.sms.*
import saver

enum class Windows {
    MAIN_WINDOW, GROUPS_WINDOW, DISTANCES_WINDOW,
    MEMBERS_POINTS_WINDOW, PARTICIPANTS_WINDOW, PARTICIPANTS_MARKS_WINDOW, EVENT_WINDOW,
    DRAW_SAVER_WINDOW, FINISH_FOR_GROUPS_WINDOW, FINISH_FOR_GROUPS_SAVER_WINDOW,
    COLLECTIVES_POINTS_WINDOW, COLLECTIVES_POINTS_SAVE_WINDOW
}

@ExperimentalFoundationApi
fun gui() {
    var collectives: List<Collective> = listOf()
    var groups: List<Group> = listOf()
    application {

        val windowStatus: MutableState<Windows> = remember { mutableStateOf(Windows.MAIN_WINDOW) }
        when (windowStatus.value) {
            Windows.MAIN_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                title = "'Subibuyaso' Sport management system",
                state = rememberDialogState(width = 365.dp, height = 720.dp),
                content = {
                    MainWindow(windowStatus).init()
                })

            Windows.GROUPS_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                state = rememberDialogState(width = 450.dp, height = 750.dp),
                title = "Groups",
                content = {
                    val changes = {
                        windowStatus.value = Windows.MAIN_WINDOW
                    }
                    loadParameters()["groups"]?.let { csvToTableMutable(it, 10, changes) }
                }
            )

            Windows.PARTICIPANTS_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                title = "Participants",
                state = rememberDialogState(width = 750.dp, height = 750.dp),
                content = {
                    val collective: MutableState<String> = remember { mutableStateOf("") }
                    val changes = { windowStatus.value = Windows.MAIN_WINDOW }
                    val action: (String) -> Unit = { collective.value = it }
                    when (collective.value) {
                        "" -> loadParameters()["collectives"]?.let {
                            showCollectives(getAllFilesInResources(it),
                                action,
                                changes)
                        }
                        else -> loadParameters()["collectives"]?.let {
                            csvToTableMutable("$it/${collective.value}.csv", 10
                            ) { collective.value = "" }
                        }
                    }
                }
            )

            Windows.EVENT_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                state = rememberDialogState(width = 450.dp, height = 300.dp),
                title = "Event",
                content = {
                    val changes = {
                        windowStatus.value = Windows.MAIN_WINDOW
                    }
                    loadParameters()["event"]?.let { csvToTableMutable(it, 0, changes) }
                }
            )

            Windows.DISTANCES_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                title = "Distances",
                state = rememberDialogState(width = 750.dp, height = 750.dp),
                content = {
                    val changes = {
                        windowStatus.value = Windows.MAIN_WINDOW
                    }
                    loadParameters()["distances"]?.let { csvToTableMutable(it, 10, changes) }
                }

            )

            Windows.PARTICIPANTS_MARKS_WINDOW -> Dialog(
                onCloseRequest = ::exitApplication,
                title = "Start protocol",
                state = rememberDialogState(width = 750.dp, height = 750.dp),
                content = {
                    collectives = loadAllInitialData().component1()
                    groups = loadAllInitialData().component2()
                    draw(groups)
                    val action = { windowStatus.value = Windows.DRAW_SAVER_WINDOW }
                    csvToTable(generateAllStartProtocols(groups), action)
                }
            )

            Windows.DRAW_SAVER_WINDOW ->
                Dialog(
                    onCloseRequest = ::exitApplication,
                    title = "Start protocol saver",
                    state = rememberDialogState(width = 370.dp, height = 110.dp),
                    content = {
                        val action = { windowStatus.value = Windows.MEMBERS_POINTS_WINDOW }
                        saver(generateAllStartProtocols(groups),
                            action,
                            "File to save start protocol",
                            Icons.Filled.ArrowForward)
                    }
                )

            Windows.FINISH_FOR_GROUPS_WINDOW ->
                Dialog(
                    onCloseRequest = ::exitApplication,
                    state = rememberDialogState(width = 750.dp, height = 750.dp),
                    title = "Finish protocol",
                    content = {

                        val action = { windowStatus.value = Windows.FINISH_FOR_GROUPS_SAVER_WINDOW }
                        csvToTable(writeGroupProtocolsToCSV(groups), action)
                    }
                )

            Windows.FINISH_FOR_GROUPS_SAVER_WINDOW ->
                Dialog(
                    onCloseRequest = ::exitApplication,
                    title = "Finish protocol saver",
                    state = rememberDialogState(width = 370.dp, height = 110.dp),
                    content = {

                        val action = { windowStatus.value = Windows.COLLECTIVES_POINTS_WINDOW }
                        saver(writeGroupProtocolsToCSV(groups),
                            action,
                            "File to save finish protocol",
                            Icons.Filled.ArrowForward)
                    }
                )

            Windows.COLLECTIVES_POINTS_WINDOW -> {
                Dialog(
                    onCloseRequest = ::exitApplication,
                    title = "Collective points",
                    state = rememberDialogState(width = 375.dp, height = 750.dp),
                    content = {

                        val action = { windowStatus.value = Windows.COLLECTIVES_POINTS_SAVE_WINDOW }
                        csvToTable(getCollectivePoints(collectives, groups), action)
                    }
                )
            }

            Windows.COLLECTIVES_POINTS_SAVE_WINDOW ->
                Dialog(
                    onCloseRequest = ::exitApplication,
                    title = "Collective points saver",
                    state = rememberDialogState(width = 370.dp, height = 110.dp),
                    content = {
                        val action = ::exitApplication
                        saver(writeGroupProtocolsToCSV(groups),
                            action,
                            "File to save collective points",
                            Icons.Filled.ExitToApp)
                    }
                )


            Windows.MEMBERS_POINTS_WINDOW ->
                Dialog(
                    onCloseRequest = ::exitApplication,
                    state = rememberDialogState(width = 450.dp, height = 250.dp),
                    title = "Points loading",
                    content = {
                        CollectivesFormat(windowStatus).collectivesFormat(groups)
                    }
                )
        }
    }
}

