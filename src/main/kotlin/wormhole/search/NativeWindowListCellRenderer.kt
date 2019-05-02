package wormhole.search

import com.intellij.ide.ui.UISettings
import com.intellij.ide.util.gotoByName.GotoActionModel
import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.EmptyIcon
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.jetbrains.wormhole.interop.NativeWindowInfo
import java.awt.BorderLayout
import java.awt.Component
import java.awt.image.BufferedImage
import javax.swing.DefaultListCellRenderer
import javax.swing.ImageIcon
import javax.swing.JList
import javax.swing.JPanel


class NativeWindowListCellRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        val showIcon = UISettings.instance.showIconsInMenus
        val size = UIUtil.getFontSize(UIUtil.FontSize.MINI).toInt()
        val backColor = UIUtil.getListBackground(isSelected, true)
        val panel = JPanel(BorderLayout()).also{
            border = JBUI.Borders.empty(2)
            isOpaque = true
            background = backColor
        }

        val nameComponent = SimpleColoredComponent().also {
            background = backColor
        }

        panel.add(nameComponent, BorderLayout.CENTER)

        if (value is NativeWindowInfo) {
            nameComponent.append(value.name, SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, GotoActionModel.defaultActionForeground(isSelected, null)))
            val icon = if (value.image == null) EmptyIcon.ICON_18 else createIcon(value.image!!, size)
            if (showIcon) {
                panel.add(JBLabel(icon), BorderLayout.WEST)
            }
        }
        return panel
    }

    private fun createIcon(image: BufferedImage, size: Int): ImageIcon {

        val iconImage = UIUtil.createImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g = iconImage.createGraphics()
        g.drawImage(image, 0, 0, size, size, null)
        return ImageIcon(iconImage)
    }
}