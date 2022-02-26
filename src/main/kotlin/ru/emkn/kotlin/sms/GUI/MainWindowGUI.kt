package ru.emkn.kotlin.sms.GUI

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.generateAllData
import ru.emkn.kotlin.sms.loadParameters
import ru.emkn.kotlin.sms.updateParametersFile
import java.awt.FileDialog
import java.io.File

class MainWindow(private val winStatus: MutableState<Windows>) {

    @Composable
    fun init() {
        Column {
            changeParams()
            actionButtons()
        }
    }

    @Composable
    fun changeParams() {
        val parameters by remember { mutableStateOf(loadParameters()) }
        Text(text = "Input data:",
            fontSize = 28.sp,
            modifier = Modifier.padding(15.dp),
            fontFamily = FontFamily.SansSerif)

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            // Files
            parameters.map { (key, value) ->
                if (key != "collectives")
                    item {
                        Row {
                            var text by remember {
                                mutableStateOf("${key.replaceFirstChar { it.titlecase() }}: ${File(value).name}")
                            }
                            SelectionContainer { Text(text = text, fontSize = 20.sp) }

                            val file = FileDialog(ComposeWindow(), "Choose *.csv file")
                            IconButton(
                                modifier = Modifier.padding(5.dp, 0.dp, 0.dp).size(24.dp),
                                onClick = {
                                    file.isVisible = true

                                    if (!file.file.isNullOrEmpty() && file.file.endsWith(".csv")) {
                                        parameters[key] = file.directory + file.file
                                        updateParametersFile(parameters)
                                    }
                                    text = "${key.replaceFirstChar { it.titlecase() }}: ${File(parameters[key]!!).name}"
                                },
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    tint = Color(139, 0, 255),
                                    contentDescription = "Editing input file"
                                )
                            }
                        }
                    }
            }

            // Folder
            item {
                Row {
                    var text by remember { mutableStateOf(parameters.values.last()) }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = text,
                        textStyle = TextStyle(fontSize = 20.sp),
                        onValueChange = { text = it },
                        maxLines = 1,
                        label = { Text("Collectives folder") }
                    )

                    IconButton(
                        onClick = {
                            check(File(text).isDirectory) { Exception("Directory $text doesn't exist") }
                            parameters["collectives"] = text
                            updateParametersFile(parameters)
                        },
                        modifier = Modifier.size(65.dp).padding(9.dp, 0.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Confirm changes"
                        )
                    }
                }
            }
        }

    }

    @Composable
    fun actionButtons() {

        Column(
            modifier = Modifier.fillMaxSize().padding(0.dp, 10.dp, 0.dp)
        ) {
            Row {
                Button(
                    modifier = Modifier.size(width = 175.dp, height = 110.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(255, 150, 79),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = {
                        winStatus.value = Windows.PARTICIPANTS_MARKS_WINDOW
                    },
                ) { Text("Make draw", fontSize = 18.sp) }

                Button(
                    modifier = Modifier.size(width = 175.dp, height = 110.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(249, 179, 84),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = { generateAllData() },
                ) { Text("Generate data", fontSize = 18.sp) }
            }
            Row {
                Button(
                    modifier = Modifier.size(width = 175.dp, height = 175.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(219, 88, 86),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = { winStatus.value = Windows.GROUPS_WINDOW },
                ) { Text("Groups", fontSize = 23.sp) }

                Button(
                    modifier = Modifier.size(width = 175.dp, height = 175.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(139, 0, 255),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = { winStatus.value = Windows.DISTANCES_WINDOW },
                ) { Text("Distances", fontSize = 23.sp) }
            }

            Row {
                Button(
                    modifier = Modifier.size(width = 175.dp, height = 175.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(125, 196, 225),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = { winStatus.value = Windows.EVENT_WINDOW },
                ) { Text("Event", fontSize = 23.sp) }


                Button(
                    modifier = Modifier.size(width = 175.dp, height = 175.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(109, 211, 109),
                        contentColor = Color(242, 243, 244)
                    ),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onClick = { winStatus.value = Windows.PARTICIPANTS_WINDOW },
                ) { Text("Participants", fontSize = 21.sp) }
            }

        }
    }
}