package wormhole.editor

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.testFramework.LightVirtualFileBase
import com.jetbrains.wormhole.interop.windows.WindowsWindowId
import java.io.ByteArrayOutputStream

class WormholeVirtualFile(var id: WindowsWindowId,
                          name: String,
                          fileType: FileType,
                          modificationStamp: Long): LightVirtualFileBase(name, fileType, modificationStamp) {

    override fun contentsToByteArray() = id.toString().toByteArray()

    override fun getInputStream() = VfsUtilCore.byteStreamSkippingBOM(contentsToByteArray(), this)

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long) =
        VfsUtilCore.outputStreamAddingBOM(object : ByteArrayOutputStream() {
            override fun close() {
                val content = toByteArray()
                id = WindowsWindowId(content.toString())
            }
        }, this)

    override fun getPresentableName(): String {
        return name
    }

}