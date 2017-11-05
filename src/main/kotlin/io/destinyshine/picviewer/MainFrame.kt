package io.destinyshine.picviewer

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel
import java.awt.BorderLayout
import java.awt.datatransfer.DataFlavor.javaFileListFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

fun main(args: Array<String>) {

    JFrame.setDefaultLookAndFeelDecorated(true)
    JDialog.setDefaultLookAndFeelDecorated(true)
    SwingUtilities.invokeLater {
        UIManager.setLookAndFeel(SubstanceGraphiteLookAndFeel())

        val mainFrame = JFrame()
        val contentPane = JPanel(BorderLayout())
        val imagePane = ImagePane()
        contentPane.add(imagePane, BorderLayout.CENTER)
        mainFrame.contentPane = contentPane
        mainFrame.setSize(400, 600)
        mainFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val toolbar = createToolBar(mainFrame.rootPane.inputMap, mainFrame.rootPane.actionMap, imagePane)
        contentPane.add(toolbar, BorderLayout.NORTH)
        mainFrame.isVisible = true

        DropTarget(contentPane, object : DropTargetAdapter() {
            override fun drop(dtde: DropTargetDropEvent) {
                if (dtde.isDataFlavorSupported(javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_MOVE)
                    val files: List<File> = dtde.transferable.getTransferData(javaFileListFlavor) as List<File>
                    System.out.print(files)
                    if (files.isNotEmpty()) {
                        imagePane.image = ImageIO.read(files[0])
                        imagePane.repaint()
                    }
                }
            }
        })
    }


}

fun createToolBar(inputMap: InputMap, actionMap: ActionMap, imagePane: ImagePane): JToolBar {
    val toolBar = JToolBar()
    val zoomIn = object : AbstractAction("+") {
        override fun actionPerformed(e: ActionEvent) {
            imagePane.zoomIn()
        }
    }

    val zoomOut = object : AbstractAction("-") {
        override fun actionPerformed(e: ActionEvent) {
            imagePane.zoomOut()
        }
    }

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.META_MASK), "image.zoomIn")
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.META_MASK), "image.zoomOut")

    actionMap.put("image.zoomIn", zoomIn)
    actionMap.put("image.zoomOut", zoomOut)

    toolBar.add(JButton(zoomIn))
    toolBar.add(JButton(zoomOut))

    return toolBar
}