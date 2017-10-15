package net.techbrewery.weekendowka.document

import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document
import java.io.FileNotFoundException
import java.io.FileOutputStream


/**
 * Created by Jacek Kwiecień on 15.10.2017.
 */
class PdfCreator {

    fun createDocument(company: Company, model: Document) {

        val document = com.itextpdf.text.Document()

        try {
            PdfWriter.getInstance(document, FileOutputStream("${model.id}.pdf"))

            document.open()
            document.add(Paragraph("A Hello World PDF document."))
            document.close() // no need to close PDFwriter?

        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }
}