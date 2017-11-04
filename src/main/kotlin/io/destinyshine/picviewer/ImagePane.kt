package io.destinyshine.picviewer

import java.awt.Graphics
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.ImageObserver
import javax.swing.JPanel

class ImagePane : JPanel {
    var image: Image? = null

    constructor(image: Image) {
        this.image = image
    }

    constructor()

    override fun paint(g: Graphics) {
        if (image != null) {
            var imageObserver = ImageObserver { img, infoFlags, x, y, width, height ->
                if (infoFlags == ALLBITS) {
                    repaint()
                    return@ImageObserver true
                }
                false
            }
            var rect = computePaintRange(image!!, imageObserver)
            if (rect != null) {
                g.drawImage(image, rect.x, rect.y, rect.width, rect.height, imageObserver)
            }
        }
    }

    fun computePaintRange(image: Image, observer: ImageObserver): Rectangle? {
        var imageWidth = image.getWidth(observer)
        if (imageWidth == -1) {
            return null
        }
        var imageHeight = image.getHeight(observer)
        if (imageHeight == -1) {
            return null
        }

        var widthScore = this.width * imageHeight
        var heightScore = this.height * imageWidth

        var paintRect = Rectangle()

        //panel  width > height
        if (widthScore > heightScore) {
            paintRect.height = this.height
            paintRect.width = heightScore / imageHeight
            paintRect.x = Math.abs((this.width - paintRect.width) / 2)
            paintRect.y = 0
        } else {
            paintRect.height = widthScore / imageWidth
            paintRect.width = this.width
            paintRect.x = 0
            paintRect.y = Math.abs((this.height - paintRect.height) / 2)
        }

        return paintRect
    }


}