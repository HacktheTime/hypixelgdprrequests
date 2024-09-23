import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.swing.SwingUtilities

class Analyzer {
    private lateinit var inputStream: InputStream

    // Constructor that accepts an InputStream
    constructor(inputStream: InputStream) {
        this.inputStream = inputStream
    }

    // Constructor that accepts a File and converts it to an InputStream
    constructor(file: File) {
        this.inputStream = file.inputStream()
    }

    lateinit var feedback: FeedbackPopup

    fun analyze() {
        // Simulate analysis process
        SwingUtilities.invokeLater {
            feedback = FeedbackPopup("GPDR DATA Analyser")
            // Create a temporary directory for unpacking
            feedback.appendMessage("Creating Tempdir")
            val tempDir = createTempDir()
            try {
                ZipInputStream(inputStream).use { zipInputStream ->
                    var entry: ZipEntry? = zipInputStream.nextEntry
                    while (entry != null) {
                        // Unpack the entry to the temporary directory
                        unpackEntry(zipInputStream, entry, tempDir)
                        feedback.appendMessage("Unpacked " + entry.name)
                        entry = zipInputStream.nextEntry
                    }
                }
                // Return the path of the temporary directory via callback
                analyze(tempDir)
            } catch (e: Exception) {
                e.printStackTrace()
                SimplePopup("GPDR DATA Analyser", "Error processing file: ${e.message}")
                // Clean up the temporary directory in case of an error
                tempDir.deleteRecursively()
                // Return null via callback
            }
        }
    }

    private fun analyze(tempDir: File) {
        feedback.appendMessage("Unpacking Done. Analysing... Dir: " + tempDir.absolutePath)
        for (file in tempDir.listFiles()!!) {
            val fName = file.name
            when (fName) {
                "chat" -> analyzeChat(file)
                "data" -> analyseGeneralData(file)
                "developer" -> analyseDeveloper(file)
                "housing" -> analyzeHousing(file)
                "skyblock" -> analyzeSkyblock(file)
                "smp" -> analyzeSMP(file)
            }
        }
    }

    private fun analyzeSMP(file: File) {
        // Process SMP data
    }

    private fun analyzeSkyblock(file: File) {
        for (profile in file.resolve("profiles").listFiles()) {
            feedback.appendMessage("Found Profile ID: " + profile.name)
        }
        // Process Skyblock data
    }

    private fun analyzeHousing(file: File) {
        // Process Housing data
    }

    private fun analyseDeveloper(file: File) {
        // Process Developer data
    }

    private fun analyseGeneralData(file: File) {
        // Process General Data
    }

    private fun analyzeChat(file: File) {
        // Process Chat data
    }

    private fun createTempDir(): File {
        // Create a unique temporary directory
        val tempDir = File.createTempFile("analyzer_", "")
        tempDir.delete() // Delete the file to create a directory
        tempDir.mkdir() // Create the directory
        return tempDir
    }

    private fun unpackEntry(zipInputStream: ZipInputStream, entry: ZipEntry, outputDir: File) {
        val outputFile = File(outputDir, entry.name)
        if (entry.isDirectory) {
            outputFile.mkdirs() // Create directory structure
        } else {
            // Ensure the parent directory exists
            outputFile.parentFile.mkdirs()
            // Write the file to the output directory
            FileOutputStream(outputFile).use { fos ->
                zipInputStream.copyTo(fos)
            }
        }
    }
}
