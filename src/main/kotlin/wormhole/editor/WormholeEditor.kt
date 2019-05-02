package wormhole.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.application.TransactionGuard
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.util.ui.JBUI
import com.jetbrains.wormhole.events.WormholeEvent
import com.jetbrains.wormhole.swing.JWormhole
import java.beans.PropertyChangeListener

class WormholeEditor(private val project: Project, private val file: WormholeVirtualFile) : UserDataHolderBase(), FileEditor {


    private val wormhole: JWormhole = JWormhole { JBUI.sysScale(this.component) }.apply {
        hideTargetOnRootDetach = true
    }


    private var state = FileEditorState.INSTANCE

    init {
        if (wormhole.isCompatiblePlatform) {
            wormhole.embed(file.id)

            val fileCloseOperation = Runnable {
                FileEditorManagerEx.getInstanceEx(project).currentWindow.closeFile(file)
            }

            wormhole.addEventListener(WormholeEvent.EmbeddedWindowDead) {
                TransactionGuard.getInstance().submitTransactionLater(Disposer.newDisposable(), fileCloseOperation)
            }
        }
    }

    override fun isModified() = false

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getFile() = file

    override fun getName() = file.presentableName

    override fun setState(state: FileEditorState) {
        this.state = state
    }

    override fun getComponent() = wormhole

    override fun getPreferredFocusedComponent() = wormhole

    override fun selectNotify() {}

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun deselectNotify() {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun isValid() = true

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun dispose() {
        wormhole.unembed()
    }
}