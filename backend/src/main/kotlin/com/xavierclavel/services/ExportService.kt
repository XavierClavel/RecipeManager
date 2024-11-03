package com.xavierclavel.services

import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import common.infodto.RecipeInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.ByteArrayOutputStream

class ExportService: KoinComponent {
    val imageService: ImageService by inject()
    val robotoFont = ExportService::class.java.getResource("/fonts/Roboto-Regular.ttf")!!.readBytes()
    val titleColor = DeviceRgb(255, 111, 89)

    fun generatePDF(recipe: RecipeInfo) = generatePDF(listOf(recipe))

    fun generatePDF(recipes: List<RecipeInfo>): ByteArray {
        // Create a ByteArrayOutputStream to write the PDF data into
        val outputStream = ByteArrayOutputStream()

        // Create Roboto font from resources
        val robotoFont = PdfFontFactory.createFont(robotoFont, PdfEncodings.IDENTITY_H)


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

    private fun Document.writeRecipe(recipe: RecipeInfo) {
        this.addTitle(recipe.title)
        this.addParagraph(recipe.description)
        this.addImage(recipe.id)
        if (recipe.portions != null) this.addParagraph("Yield: ${recipe.portions}")
        if (recipe.preparationTime != null) this.addParagraph("Preparation time: ${recipe.preparationTime}")
        if (recipe.cookingTime != null) this.addParagraph("Cooking time: ${recipe.cookingTime}")
        this.addParagraphTitle("Steps")
        recipe.steps.forEachIndexed{index, step -> this.addParagraph("$index - $step")}

    }

    private fun Document.addImage(recipeId: Long) {
        // Convert WebP to JPEG (WebP unsupported in PDFs)
        val jpegBytes = imageService.getRecipeImageAsJpegBytes(recipeId)

        // Add the converted image to the PDF
        val imageData = ImageDataFactory.create(jpegBytes)
        val image = Image(imageData)
        this.add(image)
    }

    private fun Document.addTitle(title: String) = this.add(Paragraph(
        Text(title)
            .setFontColor(titleColor)
            .setFontSize(17f)
            .setBold()
    ))

    private fun Document.addParagraph(text : String) = this.add(Paragraph (
        text
    ))

    private fun Document.addParagraphTitle(title: String) = this.add(Paragraph(
        Text(title)
            .setFontColor(titleColor)
            .setFontSize(14f)
            .setBold()
    ))



}