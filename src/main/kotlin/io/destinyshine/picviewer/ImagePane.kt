package io.destinyshine.picviewer

import java.awt.Graphics
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.ImageObserver
import javax.swing.JPanel

class ImagePane : JPanel {
    var image: Image? = null
    var zoomRatio: Double = 1.0

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
            var destAndSrcRect = computePaintRange(image!!, imageObserver)
            if (destAndSrcRect != null) {
                var dest = destAndSrcRect[0]
                var src = destAndSrcRect[1]
                g.drawImage(
                        image,
                        dest.x, dest.y, dest.maxX.toInt(), dest.maxY.toInt(),
                        src.x, src.y, src.maxX.toInt(), src.maxY.toInt(),
                        imageObserver
                )
            }
        }
    }

    private fun computePaintRange(image: Image, observer: ImageObserver): Array<Rectangle>? {
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

        //if panel width > height
        if (widthScore > heightScore) {
            paintRect.width = heightScore / imageHeight
            paintRect.height = this.height
            paintRect.x = (this.width - paintRect.width) / 2
            paintRect.y = 0
        } else {
            paintRect.width = this.width
            paintRect.height = widthScore / imageWidth
            paintRect.x = 0
            paintRect.y = (this.height - paintRect.height) / 2
        }

        var srcRect = Rectangle()

        srcRect.width = (imageWidth / zoomRatio).toInt()
        srcRect.height = (imageHeight / zoomRatio).toInt()
        srcRect.x = (imageWidth - srcRect.width) / 2
        srcRect.y = (imageHeight - srcRect.height) / 2

        return arrayOf(paintRect, srcRect)
    }

    fun zoomIn(ratio: Double) {
        zoomRatio = ratio
        repaint()
    }

    fun zoomIn() {
        zoomRatio *= 2.0
        repaint()
    }

    fun zoomOut() {
        zoomRatio /= 2.0
        repaint()
    }

}