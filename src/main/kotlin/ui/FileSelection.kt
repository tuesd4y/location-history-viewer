import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileSelection(onSelect: (File) -> Unit) {
    var isFileChooserOpen by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Button(onClick = {
        isFileChooserOpen = true
    }) {
        Text("Open google takeout zip file")
    }

    if (isFileChooserOpen) {
        FileDialog { dir, fileName ->
            val file = File(dir + fileName)
            if (file.extension != "zip") {
                dialogMessage = "Please select a zip file"
                isDialogOpen = true
            } else if (!file.exists()) {
                dialogMessage = "No file selected"
                isDialogOpen = true
            } else {
                isFileChooserOpen = false
                onSelect(file)
            }
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Problem with opening file") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FileSelectionPreview() = FileSelection { }
