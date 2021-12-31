import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.pushingpixels.aurora.theming.auroraBackground

@Composable
fun DaySelection(items: List<String>, onSelect: (Int) -> Unit = {}): Int {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }


    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().zIndex(500f)) {
        Text(items[selectedIndex],modifier = Modifier.fillMaxWidth().background(Color.Gray).clickable(onClick = { expanded = true }).padding(8.dp))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().background(
                Color.Gray)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    onSelect(index)
                }) {
                    Text(text = s)
                }
            }
        }
    }

    return selectedIndex
}