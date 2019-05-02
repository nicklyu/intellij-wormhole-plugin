package wormhole.action

import com.intellij.ide.actions.GotoActionBase
import com.intellij.ide.util.gotoByName.ChooseByNameBase
import com.intellij.ide.util.gotoByName.ChooseByNameItemProvider
import com.intellij.ide.util.gotoByName.ChooseByNamePopup
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.LocalTimeCounter
import com.intellij.util.Processor
import com.jetbrains.wormhole.interop.NativeWindowInfo
import com.jetbrains.wormhole.interop.windows.WindowsWindowId
import wormhole.editor.WormholeFileType
import wormhole.editor.WormholeVirtualFile
import wormhole.search.GotoExternalWindowModel

class GotoExternalWindowAction: GotoActionBase(), DumbAware  {
    override fun gotoActionPerformed(e: AnActionEvent) {
        if(!SystemInfo.isWindows) return

        val project = e.getData(CommonDataKeys.PROJECT) ?: return

        val actionCallback = object : GotoActionCallback<String>() {
            override fun elementChosen(popup: ChooseByNamePopup, element: Any) {
                openNewNativeWindow(project, element as NativeWindowInfo)
            }
        }

        showNavigationPopup(
            e, GotoExternalWindowModel(), actionCallback, "Open native window", true, true, buildItemProvider()
        )

    }

    private fun buildItemProvider(): ChooseByNameItemProvider {
        return object : DefaultChooseByNameItemProvider(null) {
            override fun filterNames(base: ChooseByNameBase, names: Array<out String>, pattern: String): List<String> {
                return super.filterNames(base, names, "*$pattern")
            }

            override fun filterElements(base: ChooseByNameBase,
                                        pattern: String,
                                        everywhere: Boolean,
                                        cancelled: ProgressIndicator,
                                        consumer: Processor<Any>
            ): Boolean {
                return super.filterElements(base, "*$pattern", everywhere, cancelled, consumer)
            }
        }
    }

    private fun openNewNativeWindow(project: Project, windowInfo: NativeWindowInfo) {
        val file = WormholeVirtualFile(
            windowInfo.windowId as WindowsWindowId,
            windowInfo.name,
            WormholeFileType,
            LocalTimeCounter.currentTime()
        )

        val manager = FileEditorManager.getInstance(project)
        manager.openFile(file, true, true)
    }
}