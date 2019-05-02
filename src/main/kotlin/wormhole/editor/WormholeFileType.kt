package wormhole.editor

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.UIBasedFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

object WormholeFileType : UIBasedFileType {
    override fun getDefaultExtension() = "window"

    override fun getIcon(): Icon = AllIcons.FileTypes.UiForm

    override fun getCharset(file: VirtualFile, content: ByteArray): String? = null

    override fun getName() = "Wormhole"

    override fun getDescription() = "External system window"

    override fun isBinary() = false

    override fun isReadOnly() = true
}