package ru.emkn.kotlin.sms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

@Composable
fun <T> table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    modifier: Modifier = Modifier,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
    changes: () -> Unit,
    buttonIcon: ImageVector,
) {
    Surface(
        modifier = modifier
    ) {
        Column {
            IconButton(
                onClick = changes,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    buttonIcon,
                    contentDescription = "Some action"
                )
            }

            LazyRow(
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp)
            ) {
                items((0 until columnCount).toList()) { columnIndex ->
                    Column {
                        (0..data.size).forEach { index ->
                            Surface(
                                border = BorderStroke(1.dp, Color.LightGray),
                                contentColor = Color.Transparent,
                                modifier = Modifier.width(cellWidth(columnIndex))
                            ) {
                                when (index) {
                                    0 -> headerCellContent(columnIndex)
                                    else -> cellContent(columnIndex, data[index - 1])
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun showCollectives(data: List<String>, action: (String) -> Unit, changes: () -> Unit) {
    Column {
        IconButton(
            onClick = changes,
            modifier = Modifier.size(65.dp).absolutePadding(16.dp)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Go back"
            )
        }

        LazyVerticalGrid(
            cells = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(data) { item ->
                Button(
                    onClick = {
                        action(item)
                    },
                    // Uses ButtonDefaults.ContentPadding by default
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = item,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }

}

@Composable
fun csvToTable(data: List<List<String>>, changes: () -> Unit) {
    val cellWidth: (Int) -> Dp = { _ -> 150.dp }
    val headerCellTitle: @Composable (Int) -> Unit = { index ->
        val value = data[0][index]

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Black,
            textDecoration = TextDecoration.Underline
        )
    }
    val cellText: @Composable (Int, List<String>) -> Unit = { index, item ->
        val value = item[index]


        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }

    table(
        columnCount = data.first().size,
        cellWidth = cellWidth,
        data = data.drop(1),
        modifier = Modifier.verticalScroll(rememberScrollState()),
        headerCellContent = headerCellTitle,
        cellContent = cellText,
        changes,
        Icons.Filled.ArrowForward
    )
}

@Composable
fun csvToTableMutable(dataOld: String, amount: Int = 0, changes: () -> Unit) {
    val data by remember { mutableStateOf(csvReader().readAll(File(dataOld)) as MutableList<MutableList<String>>) }
    data.addAll(MutableList(amount) { MutableList(data.first().size) { "" } })
    val cellWidth: (Int) -> Dp = { _ -> 200.dp }
    val headerCellTitle: @Composable (Int) -> Unit = { index ->
        val value = data[if (data.first()[1].isEmpty()) 1 else 0][index]

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Black,
            textDecoration = TextDecoration.Underline
        )
    }
    val cellText: @Composable (Int, MutableList<String>) -> Unit = { index, item ->
        var value by remember { mutableStateOf(item[index]) }

        TextField(
            value = value,
            onValueChange = {
                value = it
                item[index] = it
                csvWriter().writeAll(data, dataOld)
            },
            maxLines = 1,
            textStyle = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(20.dp)
        )
    }

    table(
        columnCount = data.first().size,
        cellWidth = cellWidth,
        data = if (data.first()[1].isEmpty()) data.drop(2) else data.drop(1),
        modifier = Modifier.verticalScroll(rememberScrollState()),
        headerCellContent = headerCellTitle,
        cellContent = cellText,
        changes,
        Icons.Filled.ArrowBack
    )
}

