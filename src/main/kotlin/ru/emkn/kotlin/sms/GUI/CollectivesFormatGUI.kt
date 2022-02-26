package ru.emkn.kotlin.sms.GUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import radioGroup
import ru.emkn.kotlin.sms.Group
import ru.emkn.kotlin.sms.initMemberFromPoints
import ru.emkn.kotlin.sms.initMembersFromPoint
import java.awt.FileDialog

class CollectivesFormat(private val windowStatus: MutableState<Windows>) {

    @Composable
    fun collectivesFormat(groups: List<Group>) {
        val formatOptions: List<String> = listOf("Member - Points", "Point - Members")

        Column {
            val selectedFormat = radioGroup(
                radioOptions = formatOptions,
                title = "File format:",
            )

            val file = FileDialog(ComposeWindow(), "Choose *.csv file")
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Button(
                    onClick = {
                        file.isVisible = true
                        if (!file.file.isNullOrEmpty() && file.file.endsWith(".csv")) {
                            when (selectedFormat) {
                                "Member - Points" -> initMemberFromPoints(file.directory + file.file, groups)
                                "Point - Members" -> initMembersFromPoint(file.directory + file.file, groups)
                                else -> throw Exception("Неправильный формат файла с дистанциями")
                            }
                            windowStatus.value = Windows.FINISH_FOR_GROUPS_WINDOW
                        }
                    }) {
                    Text("Choose file...")
                }
            }
        }
    }
}