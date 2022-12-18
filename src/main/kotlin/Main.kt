// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
@Preview
fun App() {
    val appViewModel = AppViewModel()
    MaterialTheme {
        ToolsPage(appViewModel)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Lenovo",
        state = rememberWindowState(width = 400.dp, height = 250.dp, position = WindowPosition(Alignment.Center)),
        icon = buildPainter("ic_launcher.png")
    ) {
        App()
    }
}
