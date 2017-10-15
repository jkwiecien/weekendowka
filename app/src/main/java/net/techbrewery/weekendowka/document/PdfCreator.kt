package net.techbrewery.weekendowka.document

import android.content.Context
import com.itextpdf.text.Chunk
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream


/**
 * Created by Jacek Kwiecień on 15.10.2017.
 */
class PdfCreator(context: Context) {

    private val fileDir = context.cacheDir

    companion object {
        val ROBOTO_BOLD: BaseFont by lazy { BaseFont.createFont("assets/fonts/Roboto-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED) }
    }

    fun createDocument(company: Company, model: Document): File? {

        val document = com.itextpdf.text.Document()

        try {
            val file = File(fileDir, "${model.id}.pdf")
            if (file.exists()) return file
            else file.createNewFile()

            val os = FileOutputStream(file)
            val ps = PrintStream(os, true, "UTF-8")

            PdfWriter.getInstance(document, ps)

            val titleParagraph = Paragraph()
            titleParagraph.alignment = Paragraph.ALIGN_CENTER
            titleParagraph.add(Chunk("ZAŚWIADCZENIE O DZIAŁALNOŚCI\n\n", Font(ROBOTO_BOLD, 25f)))
            titleParagraph.add(Chunk("(ROZPORZĄDZENIE (WE) NR 561/2006 LUB AETR)", Font(ROBOTO_BOLD, 20f)))

            document.open()
            document.add(titleParagraph)
            document.close()

            return file
        } catch (error: DocumentException) {
            Timber.e(error)
        } catch (error: FileNotFoundException) {
            Timber.e(error)
        }
        return null
    }
}