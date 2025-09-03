package com.bos.payment.appName.utils.imageCompressor

import android.content.Context
import android.graphics.Bitmap
import com.bos.payment.appName.utils.imageCompressor.ImageUtil.decodeSampledBitmapFromFile
import java.io.File
import java.io.IOException

class Compressor(context: Context) {
    private var maxWidth = 612
    private var maxHeight = 816
    private var compressFormat = Bitmap.CompressFormat.JPEG
    private var quality = 80
    private var destinationDirectoryPath: String
    fun setMaxWidth(maxWidth: Int): Compressor {
        this.maxWidth = maxWidth
        return this
    }

    fun setMaxHeight(maxHeight: Int): Compressor {
        this.maxHeight = maxHeight
        return this
    }

    fun setCompressFormat(compressFormat: Bitmap.CompressFormat): Compressor {
        this.compressFormat = compressFormat
        return this
    }

    fun setQuality(quality: Int): Compressor {
        this.quality = quality
        return this
    }

    fun setDestinationDirectoryPath(destinationDirectoryPath: String): Compressor {
        this.destinationDirectoryPath = destinationDirectoryPath
        return this
    }

    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File?): Bitmap {
        return decodeSampledBitmapFromFile(imageFile!!, maxWidth, maxHeight)
    }

    init {
        destinationDirectoryPath = context.cacheDir.path + File.separator + "images"
    }
}