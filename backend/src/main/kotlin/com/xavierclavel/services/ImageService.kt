package com.xavierclavel.services

import com.xavierclavel.utils.logger
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.USERS_IMG_PATH
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.plugins.BadRequestException
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.koin.core.component.KoinComponent
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

class ImageService: KoinComponent {


    suspend fun saveRecipeImage(id: Long, multipartData: MultiPartData) =
        saveImage(getRecipeImagePath(id), multipartData)

    suspend fun saveUserImage(id: Long, multipartData: MultiPartData) =
        saveImage(getUserImagePath(id), multipartData)

    fun deleteRecipeImage(id: Long) = deleteImage(getRecipeImagePath(id))

    fun deleteUserImage(id: Long) = deleteImage(getUserImagePath(id))

    private fun deleteImage(path:String) = Path(path).deleteIfExists()

    private fun getRecipeImagePath(id: Long) = "$RECIPES_IMG_PATH/${id}.webp"

    private fun getUserImagePath(id:Long) = "$USERS_IMG_PATH/${id}.webp"



    private suspend fun saveImage(path: String, multipartData: MultiPartData) {
        val file = Path(path)
        file.createParentDirectories()
        if (!file.exists()) {
            file.createFile()
        }
        var fileReceived = false

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    fileReceived = true
                    val image = ImageIO.read(part.provider().toInputStream())
                    ImageIO.write(image, "webp", file.toFile())
                }
                else -> {
                    logger.error {"unexpected form data"}
                    throw BadRequestException("Unexpected form data")
                }
            }
            part.dispose()
        }

        if (!fileReceived) {
            throw BadRequestException("File not received")
        }
    }

}