package wormhole.search

import com.intellij.ide.util.gotoByName.ChooseByNameModel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.ArrayUtil
import com.jetbrains.wormhole.interop.NativeWindowInfo
import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner

class GotoExternalWindowModel: ChooseByNameModel, Comparator<Any>, DumbAware {
    private val defaultListCellRenderer = NativeWindowListCellRenderer()

    private val nativeWindowList = WindowsNativeApplicationScanner.getOpenedWindows().map { WindowsNativeApplicationScanner.getWindowInfo(it, iconNeeded = true) }

    override fun willOpenEditor() = false

    override fun getCheckBoxMnemonic() = if (SystemInfo.isMac) 'P' else 'n'

    override fun getNotInMessage() = null

    override fun getElementsByName(name: String?, checkBoxState: Boolean, pattern: String): Array<NativeWindowInfo> {
        return nativeWindowList.filter { it.name == name }.toTypedArray()
    }

    override fun saveInitialCheckBoxState(state: Boolean) {
    }

    override fun getCheckBoxName(): String? = null

    override fun getSeparators(): Array<String> {
        return ArrayUtil.EMPTY_STRING_ARRAY
    }

    override fun getFullName(element: Any?) = element.toString()

    override fun loadInitialCheckBoxState() = false

    override fun getListCellRenderer() = defaultListCellRenderer

    override fun getNames(checkBoxState: Boolean): Array<String> {
        return nativeWindowList.map { it.name }.toTypedArray()
    }

    override fun getPromptText() = "Find and open external window"

    override fun getHelpId(): String? = null

    override fun useMiddleMatching() = true

    override fun getNotFoundMessage(): String? = null

    override fun getElementName(element: Any) = (element as NativeWindowInfo).name

    override fun compare(o1: Any?, o2: Any?) =
        (o1 as NativeWindowInfo).compareTo(o2 as NativeWindowInfo)
}