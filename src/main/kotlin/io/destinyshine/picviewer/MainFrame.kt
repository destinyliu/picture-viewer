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

        var mainFrame = JFrame()
        val contentPane = JPanel(BorderLayout())
        var imagePane = ImagePane()
        contentPane.add(imagePane, BorderLayout.CENTER)
        mainFrame.contentPane = contentPane
        mainFrame.setSize(400, 600)
        mainFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        var toolbar = createToolBar(mainFrame.rootPane.inputMap, mainFrame.rootPane.actionMap)
        contentPane.add(toolbar, BorderLayout.NORTH)
        mainFrame.isVisible = true

        DropTarget(contentPane, object : DropTargetAdapter() {
            override fun drop(dtde: DropTargetDropEvent) {
                if (dtde.isDataFlavorSupported(javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_MOVE)
                    var files: List<File> = dtde.transferable.getTransferData(javaFileListFlavor) as List<File>
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

fun createToolBar(inputMap: InputMap, actionMap: ActionMap): JToolBar {
    var toolBar = JToolBar()
    var zoomIn = object : AbstractAction("+") {
        override fun actionPerformed(e: ActionEvent) {
            System.out.println("zoom in event...")
        }
    }

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_MASK), "image.zoomIn")

    actionMap.put("image.zoomIn", zoomIn)

    toolBar.add(JButton(zoomIn))

    return toolBar;
}