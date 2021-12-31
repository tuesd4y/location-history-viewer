// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import org.pushingpixels.aurora.theming.marinerSkin
import org.pushingpixels.aurora.window.AuroraWindow
import org.pushingpixels.aurora.window.auroraApplication
import java.io.File

@Composable
@Preview
fun App() {
//    var state by remember { mutableStateOf(UiState.FileSelection) }
//    var fileToLoad by remember { mutableStateOf<File?>(null) }

    // for skipping file selection
    var state by remember { mutableStateOf(UiState.FileViewing) }
    var fileToLoad by remember { mutableStateOf<File?>(File("/Users/dev/Downloads/Takeout.zip")) }

    MaterialTheme {
        when(state) {
            UiState.FileSelection -> FileSelection {
                state = UiState.FileViewing
                fileToLoad = it
            }
            UiState.FileViewing -> FileOverview(fileToLoad!!) {
                state = UiState.FileSelection
            }
        }
    }
}



fun main() = auroraApplication {
    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center)
    )
    AuroraWindow(
        skin = marinerSkin(),
        state = state,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
