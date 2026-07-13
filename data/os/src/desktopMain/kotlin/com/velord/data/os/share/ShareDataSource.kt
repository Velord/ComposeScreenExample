package com.velord.data.os.share

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

@Module
actual class SharePlatformModule {
    @Single
    actual fun provideShareDataSource(scope: Scope): ShareDataSource = DesktopShareDataSource()
}

private class DesktopShareDataSource : ShareDataSource {

    override suspend fun share(text: String) {
        SwingUtilities.invokeAndWait {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(StringSelection(text), null)
            JOptionPane.showMessageDialog(null, "Movie information was copied to the clipboard")
        }
    }
}
