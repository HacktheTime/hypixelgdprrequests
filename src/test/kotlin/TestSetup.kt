fun main() {
    // Load the hypixel-request.zip file from the classpath
    val resourceStream = Analyzer::class.java.classLoader.getResourceAsStream("hypixel-request.zip")

    // Check if the resource stream is not null
    if (resourceStream != null) {
        // Create an instance of Analyzer and analyze the file
        val analyzer = Analyzer(resourceStream)
        analyzer.analyze()
    } else {
        println("File not found in classpath: hypixel-request.zip")
    }
}