import java.awt.BorderLayout
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream
import javax.swing.*

class ObtainDataFrame : JFrame() {
    private val linkField: JTextField = JTextField(30)
    private val selectFileButton: JButton = JButton("Select File")
    private val analyzeButton: JButton = JButton("Analyze")

    init {
        title = "GDPR Requests Analyzer"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(400, 150)

        val panel = JPanel()
        panel.add(JLabel("Enter Link or Select the File to the Hypixel GPDR Data:"))
        panel.add(linkField)
        panel.add(selectFileButton)
        panel.add(analyzeButton)

        add(panel)

        selectFileButton.addActionListener { selectFile() }
        analyzeButton.addActionListener { analyze() }

        isVisible = true
    }

    private fun selectFile() {
        val fileChooser = JFileChooser()
        val returnValue = fileChooser.showOpenDialog(this)
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            Analyzer(file).analyze()
        }
    }

    private fun analyze() {
        val link = linkField.text
        if (link.startsWith("https://gdpr-requests.hypixel.net/")) {
            val file = downloadZip(link)
            if (file != null) {
                Analyzer(file).analyze()
            } else {
                SimplePopup("GPDR DATA Analyser","Failed to download the file.")
            }
        } else {
            SimplePopup("GPDR DATA Analyser","Invalid link. Please provide a valid link.")
        }
    }

    private fun downloadZip(link: String): File? {
        return try {
            val url = URL(link)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = BufferedInputStream(connection.inputStream)
                val tempFile = File.createTempFile("downloaded", ".zip")
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                tempFile
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

