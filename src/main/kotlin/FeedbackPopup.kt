import java.awt.BorderLayout
import java.awt.Frame
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class FeedbackPopup(title: String? ) : JDialog(null as Frame?, title, true) {
    private val textArea: JTextArea
    private val scrollPane: JScrollPane
    private var isAtBottom = true

    init {
        setSize(400, 300)
        setLocationRelativeTo(owner)
        defaultCloseOperation = DISPOSE_ON_CLOSE

        textArea = JTextArea()
        textArea.isEditable = false
        scrollPane = JScrollPane(textArea)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

        textArea.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                checkScrollPosition()
            }

            override fun removeUpdate(e: DocumentEvent) {
                checkScrollPosition()
            }

            override fun changedUpdate(e: DocumentEvent) {
                checkScrollPosition()
            }
        })

        scrollPane.viewport.addChangeListener { e: ChangeEvent ->
            val viewport = e.source as JViewport
            isAtBottom = viewport.viewPosition.y + viewport.height >= textArea.height
        }

        add(scrollPane, BorderLayout.CENTER)
        val closeButton = JButton("Close")
        closeButton.addActionListener { dispose() }
        add(closeButton, BorderLayout.SOUTH)
    }

    fun appendMessage(message: String) {
        textArea.append(message + "\n")
        if (isAtBottom) {
            SwingUtilities.invokeLater {
                val vertical = scrollPane.verticalScrollBar
                vertical.value = vertical.maximum
            }
        }
    }

    fun openPopupWithMessage(message: String) {
        appendMessage(message)
        isVisible = true
    }

    private fun checkScrollPosition() {
        if (isAtBottom) {
            val vertical = scrollPane.verticalScrollBar
            vertical.value = vertical.maximum
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                val frame = JFrame("Main Frame")
                frame.defaultCloseOperation = EXIT_ON_CLOSE
                frame.setSize(300, 200)
                frame.setLocationRelativeTo(null)
                frame.isVisible = true

                val popup = FeedbackPopup("GPDR DATA Analyser")
                popup.openPopupWithMessage("Welcome to the chat!")
                popup.appendMessage("User1: Hello!")
                popup.appendMessage("User2: Hi there!")
            }
        }
    }
}