package com.xavierclavel.services

import com.xavierclavel.utils.logger
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.USERS_IMG_PATH
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.plugins.BadRequestException
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.koin.core.component.KoinComponent
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import net.coobird.thumbnailator.Thumbnails
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter

class ImageService: KoinComponent {


    fun saveImage(path: String, id: Long, version: Long, targetSize: Pair<Int, Int>, image: BufferedImage) =
        saveImage("$path/$id-v$version.webp", targetSize, image)

    fun deleteImage(path: String, id: Long, version: Long) {
        if (version == 0L) return
        deleteImage("$path/$id-v$version.webp")
    }

    private fun deleteImage(path:String) = Path(path).deleteIfExists()




    private fun saveImage(path: String, targetSize: Pair<Int, Int>, image: BufferedImage) {
        val file = Path(path)
        file.createParentDirectories()
        if (!file.exists()) {
            file.createFile()
        }
        val cleanedImage = cropAndResizeImage(image, targetSize.first, targetSize.second)
        ImageIO.write(cleanedImage, "webp", file.toFile())
    }

    fun saveWebP(image: BufferedImage, outputFile: File, quality: Float = 0.9f) {
        val writers = ImageIO.getImageWritersByFormatName("webp")
        if (!writers.hasNext()) {
            throw IllegalStateException("No WebP ImageWriter available")
        }

        val imageWriter: ImageWriter = writers.next()
        val outputStream = ImageIO.createImageOutputStream(outputFile)
        imageWriter.output = outputStream

        val writeParam = imageWriter.defaultWriteParam
        if (writeParam.canWriteCompressed()) {
            writeParam.compressionMode = ImageWriteParam.MODE_EXPLICIT

            val compressionTypes = writeParam.compressionTypes // Get available compression types
            if (compressionTypes != null && compressionTypes.isNotEmpty()) {
                writeParam.compressionType = compressionTypes[0] // Use the first available type
            }

            writeParam.compressionQuality = quality // Set quality (0.0 - 1.0)
        }

        imageWriter.write(null, IIOImage(image, null, null), writeParam)
        outputStream.close()
        imageWriter.dispose()
    }

    private fun cropAndResizeImage(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val targetRatio = targetWidth.toDouble() / targetHeight.toDouble()
        val originalRatio = originalImage.width.toDouble() / originalImage.height.toDouble()

        var cropX = 0
        var cropY = 0
        var cropWidth = originalImage.width
        var cropHeight = originalImage.height

        // Crop the image to match the target aspect ratio
        if (originalRatio > targetRatio) {
            // Image is too wide, crop the sides
            cropWidth = (originalImage.height * targetRatio).toInt()
            cropX = (originalImage.width - cropWidth) / 2
        } else if (originalRatio < targetRatio) {
            // Image is too tall, crop the top and bottom
            cropHeight = (originalImage.width / targetRatio).toInt()
            cropY = (originalImage.height - cropHeight) / 2
        }

        val croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight)

        // Resize the cropped image
        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.createGraphics()

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.drawImage(croppedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null)
        graphics.dispose()

        return resizedImage
    }

    fun resizeImageThumbnailator(inputImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        return Thumbnails.of(inputImage)
            .size(targetWidth, targetHeight)
            .outputQuality(1.0) // 100% quality
            .asBufferedImage()
    }




    private fun removeMetadata(image: BufferedImage): BufferedImage {
        val cleanedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        val graphics = cleanedImage.createGraphics()
        graphics.drawImage(image, 0, 0, null)
        graphics.dispose()
        return cleanedImage
    }


    private fun convertWebPToJpeg(webpBytes: ByteArray): ByteArray {
        val image = ImageIO.read(ByteArrayInputStream(webpBytes))
        ByteArrayOutputStream().use { baos ->
            ImageIO.write(image, "jpg", baos)
            return baos.toByteArray()
        }
    }

    /**
     * Reads a webp image and returns it as a jpeg byte array
     */
    fun getRecipeImageAsJpegBytes(id: Long): ByteArray {
        // Specify the path to the WebP file
        val webpPath = "$RECIPES_IMG_PATH/$id.webp"

        // Read the WebP image from the filesystem
        val webpBytes = Files.readAllBytes(Paths.get(webpPath))

        // Convert WebP to JPEG format
        val jpegBytes = convertWebPToJpeg(webpBytes)

        return jpegBytes
    }

}