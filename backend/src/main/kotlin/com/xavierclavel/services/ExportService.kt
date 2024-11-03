package com.xavierclavel.services

import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import common.infodto.RecipeInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.ByteArrayOutputStream

class ExportService: KoinComponent {
    val imageService: ImageService by inject()

    fun generatePDF(recipe: RecipeInfo) = generatePDF(listOf(recipe))

    fun generatePDF(recipes: List<RecipeInfo>): ByteArray {
        // Create a ByteArrayOutputStream to write the PDF data into
        val outputStream = ByteArrayOutputStream()

        // Create Roboto font from resources
        val robotoFont = PdfFontFactory.createFont(ExportService::class.java.getResource("/fonts/Roboto-Regular.ttf")!!.readBytes(), PdfEncodings.IDENTITY_H);


        // Initialize PDF writer and document
        PdfWriter(outputStream).use { writer ->
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)
            document.setFont(robotoFont)

            // Add a simple "Hello, World!" text
            document.add(Paragraph("Hello, World!"))
            recipes.forEach { recipe -> document.writeRecipe(recipe) }

            // Close the document
            document.close()
        }

        // Return the byte array containing the PDF
        return outputStream.toByteArray()
    }

    fun Document.writeRecipe(recipe: RecipeInfo) {
        this.add(Paragraph(recipe.title))
        this.add(Paragraph(recipe.description))

        // Convert WebP to JPEG (WebP unsupported in PDFs)
        val jpegBytes = imageService.getRecipeImageAsJpegBytes(recipe.id)

        // Add the converted image to the PDF
        val imageData = ImageDataFactory.create(jpegBytes)
        val image = Image(imageData)
        this.add(image)
    }


}