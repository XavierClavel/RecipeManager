package com.xavierclavel.services

import common.infodto.RecipeInfo
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.koin.core.component.KoinComponent
import java.io.ByteArrayOutputStream

class ExportService: KoinComponent {

    fun generatePDF(recipe: RecipeInfo) = generatePDF(listOf(recipe))

    fun generatePDF(recipes: List<RecipeInfo>): ByteArray {
        PDDocument().use { doc ->
            val page = PDPage()
            doc.addPage(page)
            val pdfFont=  PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPageContentStream(doc, page).use { contentStream ->
                contentStream.beginText()
                contentStream.setFont(pdfFont, 12F)
                contentStream.newLineAtOffset(100F, 700F)
                contentStream.showText("Hello, PDF World!")
                contentStream.endText()
            }

            // Write the PDF document to a ByteArrayOutputStream
            ByteArrayOutputStream().use { outputStream ->
                doc.save(outputStream)
                return outputStream.toByteArray()  // Return as byte array
            }
        }
    }


}