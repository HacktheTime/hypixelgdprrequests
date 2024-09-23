import java.awt.BorderLayout
import java.awt.Frame
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.SwingConstants

class SimplePopup(title: String?, message: String?) : JDialog(null as Frame?, title, true) {
    init {
        setSize(300, 150)
        setLocationRelativeTo(null) // Center the dialog on the screen
        defaultCloseOperation = DISPOSE_ON_CLOSE

        // Create a JLabel to display the message
        val messageLabel = JLabel(message, SwingConstants.CENTER)
        add(messageLabel, BorderLayout.CENTER)
        isVisible=true;

        // Create an OK button to close the popup
        val okButton = JButton("OK")
        okButton.addActionListener { e: ActionEvent? -> dispose() }
        add(okButton, BorderLayout.SOUTH)
        repaint()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Create and show the popup with a title and message
            val popup = SimplePopup("Information", "This is a simple popup message!")
            popup.isVisible = true
        }
    }
}