import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import com.alibaba.excel.EasyExcel
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun ToolsPage(appViewModel: AppViewModel) {
    val filePath by appViewModel.filePath.collectAsState("")
    var showFileChooser by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TextButton(onClick = { showFileChooser = true }, colors = buttonColors(backgroundColor = Color.White)) {
            Text("选择文件")
        }

        if (showFileChooser) {
            showFileSelector(arrayOf("pdf"), { showFileChooser = false }) {
                println("showFileSelector:$it")
                showFileChooser = false
                appViewModel.putFilePath(it)
            }
        }

        Text(filePath)

        TextButton(onClick = {
            appViewModel.readExcel(filePath)
        }, colors = buttonColors(backgroundColor = Color.White)) {
            Text("转换文件")
        }

        Text(filePath)
    }
}

/**
 * @param suffixList 后缀列表
 * @param onCanceled 文件选择框点击取消的操作
 * @param onFileSelected 文件选择完成的操作
 */
private fun showFileSelector(
    suffixList: Array<String>,
    onCanceled: () -> Unit,
    onFileSelected: (String) -> Unit
) {
    JFileChooser().apply {
        //设置页面风格
        try {
            val lookAndFeel = UIManager.getSystemLookAndFeelClassName()
            UIManager.setLookAndFeel(lookAndFeel)
            SwingUtilities.updateComponentTreeUI(this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        fileSelectionMode = JFileChooser.FILES_ONLY
        isMultiSelectionEnabled = false
        fileFilter = FileNameExtensionFilter("文件过滤", *suffixList)

        val result = showOpenDialog(ComposeWindow())
        println("result:$result")
        if (result == JFileChooser.APPROVE_OPTION) {
            val dir = this.currentDirectory
            val file = this.selectedFile
            println("Current apk dir: ${dir.absolutePath} ${dir.name}")
            println("Current apk name: ${file.absolutePath} ${file.name}")
            onFileSelected(file.absolutePath)
        } else if (result == JFileChooser.CANCEL_OPTION) {
            onCanceled()
        }

    }
}