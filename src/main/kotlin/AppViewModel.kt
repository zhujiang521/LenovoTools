import com.alibaba.excel.EasyExcel
import com.alibaba.excel.read.listener.PageReadListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.ExcelModel
import org.dom4j.DocumentHelper
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AppViewModel {

    private val _filePath = MutableStateFlow("")
    val filePath: Flow<String>
        get() = _filePath

    /**
     * 更新选择的文件路径
     */
    fun putFilePath(path: String) {
        if (_filePath.value == path) {

            return
        }
        _filePath.value = path
    }

    fun readExcel(filePath: String) {
        if (filePath.isNotEmpty()) {
            val file = File(filePath)
            println("file:${file.absolutePath} $file ${file.parentFile.absolutePath}")
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(file, ExcelModel::class.java, PageReadListener<ExcelModel> {
                it.forEachIndexed { index, excelModel ->
                    println("index:$index")
                    println("excelModel:$excelModel")
                }
            }).sheet().doRead()
        }
    }

    fun buildXmlFile(stringList: List<ExcelModel>) {
        // 1.声明文件名称
        val fileName = "strings"
        // 2.创建dom对象
        val document = DocumentHelper.createDocument()

        // 3.添加节点，根据需求添加，这里我只是设置了一个head节点，下面有name和age两个子节点
        val head = document.addElement("resources")
        head.addAttribute("xmlns:xliff", "urn:oasis:names:tc:xliff:document:1.2")
        head.addAttribute("xmlns:tools", "http://schemas.android.com/tools")

        stringList.forEach {
            if (it.name.isEmpty() || it.value.isEmpty()) return
            val name = head.addElement("string")
            name.addAttribute("name", it.name)
            name.text = it.value
        }


        // 4、格式化模板
        //OutputFormat format = OutputFormat.createCompactFormat();
        val format = OutputFormat.createPrettyPrint()
        format.encoding = "UTF-8"

        // 5、生成xml文件
        val out = ByteArrayOutputStream()
        try {
            val writer = XMLWriter(out, format)
            writer.write(document)
            writer.close()
        } catch (e: IOException) {
            println("生成xml文件失败。文件名【$fileName】")
        }

        // 6、生成的XML文件
        // 7、利用文件输出流输出到文件， 文件输出到了您的项目根目录下了
        try {
            FileOutputStream("/Users/zhujiang/Downloads/$fileName.xml").use { fos -> fos.write(out.toByteArray()) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}