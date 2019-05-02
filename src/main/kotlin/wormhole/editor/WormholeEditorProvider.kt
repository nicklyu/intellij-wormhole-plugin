package wormhole.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import org.jdom.Element

class WormholeEditorProvider : FileEditorProvider, DumbAware     {
    override fun getEditorTypeId(): String {
        return "nicklyu.wormhole.editor"
    }

    override fun accept(project: Project, file: VirtualFile) = SystemInfo.isWindows && file is WormholeVirtualFile

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val wormholeFile = file as WormholeVirtualFile
        return WormholeEditor(project, wormholeFile)
    }

    override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {}

    override fun getPolicy() = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}