package net.techbrewery.weekendowka.document

import net.techbrewery.weekendowka.model.Document

/**
 * Created by Jacek Kwiecień on 14.10.2017.
 */
sealed class DocumentViewEvent {
    class Error(error: Throwable) : DocumentViewEvent()
    class DocumentSaved(val document: Document) : DocumentViewEvent()
}