import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

@Composable
fun radioGroup(radioOptions: List<String> = listOf(), title: String): String {
    if (radioOptions.isNotEmpty()) {
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(radioOptions[0])
        }

        Column(Modifier.padding(10.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                modifier = Modifier.padding(5.dp),
            )

            radioOptions.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == selectedOption),
                        onClick = { onOptionSelected(item) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )

                    ClickableText(
                        text = AnnotatedString("  $item  "),
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        onClick = {
                            onOptionSelected(item)
                        }
                    )
                }
            }
        }
        return selectedOption
    }
    return ""
}


@Composable
fun saver(list: List<List<String>>, action: () -> Unit, fileDescription: String, buttonIcon: ImageVector) {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            textStyle = TextStyle(fontSize = 20.sp),
            onValueChange = { text = it },
            maxLines = 1,
            label = { Text(fileDescription) }
        )

        IconButton(
            onClick = {
                csvWriter().writeAll(list, File(text))
                action()
            },
            modifier = Modifier.size(65.dp)
        ) {
            Icon(
                buttonIcon,
                contentDescription = null
            )
        }
    }
}
