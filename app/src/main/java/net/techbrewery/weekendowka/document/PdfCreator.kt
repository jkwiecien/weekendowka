package net.techbrewery.weekendowka.document

import android.content.Context
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import net.techbrewery.weekendowka.base.extensions.toDateTime
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
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
        val DATE_FORMAT: DateTimeFormatter by lazy { DateTimeFormat.forPattern("dd.MM.yyyy") }
        val DATE_TIME_FORMAT: DateTimeFormatter by lazy { DateTimeFormat.forPattern("HH:mm, dd.MM.yyyy") }

        val ROBOTO_BOLD: BaseFont by lazy { BaseFont.createFont("assets/fonts/Roboto-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED) }
        val ROBOTO_REGULAR: BaseFont by lazy { BaseFont.createFont("assets/fonts/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED) }
        val ROBOTO_ITALIC: BaseFont by lazy { BaseFont.createFont("assets/fonts/Roboto-Italic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED) }
    }

    fun createDocument(company: Company, document: Document): File? {

        val pdfDocument = com.itextpdf.text.Document()

        try {
            val file = File(fileDir, "${document.id}.pdf")
            if (file.exists()) return file
            else file.createNewFile()

            val os = FileOutputStream(file)
            val ps = PrintStream(os, true, "UTF-8")

            PdfWriter.getInstance(pdfDocument, ps)

            pdfDocument.open()

            val titleParagraph = Paragraph()
            titleParagraph.alignment = Paragraph.ALIGN_CENTER
            titleParagraph.add(Chunk("ZAŚWIADCZENIE O DZIAŁALNOŚCI\n\n", Font(ROBOTO_BOLD, 25f)))
            titleParagraph.add(Chunk("(ROZPORZĄDZENIE (WE) NR 561/2006 LUB AETR)\n\n\n", Font(ROBOTO_BOLD, 20f)))
            pdfDocument.add(titleParagraph)

            pdfDocument.add(Phrase("Nazwa przedsiębiorstwa: ${company.name}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Adres przedsiębiorstwa: ${company.address}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Numer telefonu: ${company.phone}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Adres e-mail: ${company.email}\n\n", Font(ROBOTO_REGULAR, 14f)))

            pdfDocument.add(Phrase("Ja niżej podpisany: ${company.name}\n", Font(ROBOTO_ITALIC, 14f)))
            pdfDocument.add(Phrase("Imię i nazwisko: ${document.declarer?.name}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Stanowisko w przedsiębiorstwie: ${document.declarer?.position}\n\n", Font(ROBOTO_REGULAR, 14f)))

            pdfDocument.add(Phrase("Oświadczam, że kierowca:", Font(ROBOTO_ITALIC, 14f)))
            pdfDocument.add(Phrase("Imię i nazwisko: ${document.driver?.name}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Data urodzenia: ${DATE_FORMAT.print(document.driver?.birthday?.toDateTime())}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Numer prawa jazdy lub dowodu osobistego lub paszportu:: ${document.driver?.idNumber}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Data rozpoczęcia pracy w przedsiębiorstwie: ${DATE_FORMAT.print(document.driver?.birthday?.toDateTime())}\n\n", Font(ROBOTO_REGULAR, 14f)))

            pdfDocument.add(Phrase("W okresie:\n", Font(ROBOTO_ITALIC, 14f)))
            pdfDocument.add(Phrase("od: ${DATE_TIME_FORMAT.print(document.actionStartDate.toDateTime())}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("do: ${DATE_TIME_FORMAT.print(document.actionEndDate.toDateTime())}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("miał czas wolny od pracy\n\n", Font(ROBOTO_ITALIC, 14f)))

            pdfDocument.add(Phrase("Miejscowość i data: ${document.placeOfDeclarerSigning}, ${DATE_FORMAT.print(document.dateOfDeclarerSigning.toDateTime())}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Podpis: ________________________\n\n\n\n", Font(ROBOTO_REGULAR, 14f)))

            pdfDocument.add(Phrase("Ja, jako kierowca, potwierdzam, że w wyżej wymienionym okresie nie prowadziłem pojazdu wchodzącego w zakres stosowania (WE) nr 561/2006 lub AETR.\n\n", Font(ROBOTO_ITALIC, 14f)))
            pdfDocument.add(Phrase("Miejscowość i data: ${document.placeOfDriverSigning}, ${DATE_FORMAT.print(document.dateOfDriverSigning.toDateTime())}\n", Font(ROBOTO_REGULAR, 14f)))
            pdfDocument.add(Phrase("Podpis kierowcy: ________________________\n\n\n\n", Font(ROBOTO_REGULAR, 14f)))


            pdfDocument.close()

            return file
        } catch (error: DocumentException) {
            Timber.e(error)
        } catch (error: FileNotFoundException) {
            Timber.e(error)
        }
        return null
    }
}