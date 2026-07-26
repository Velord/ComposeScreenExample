package com.velord.data.os.share

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class DesktopShareDataSource : ShareDataSource {

    override suspend fun share(text: String) {
        SwingUtilities.invokeAndWait {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(StringSelection(text), null)
            JOptionPane.showMessageDialog(null, "Movie information was copied to the clipboard")
        }
    }
}
