package net.techbrewery.weekendowka.base.network

import com.google.firebase.auth.FirebaseUser
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document

/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
interface Repository {
    fun signInAnonymously(requestListener: FirestoreRequestListener<FirebaseUser>)
    fun getCompany(userId: String, requestListener: FirestoreRequestListener<Company?>)
    fun saveCompany(company: Company, requestListener: FirestoreRequestListener<Company>)
    fun saveSelectedDeclarer(companyId: String, declarerId: String)
    fun saveSelectedDriver(companyId: String, driverId: String)
    fun saveDocument(companyId: String, document: Document, requestListener: FirestoreRequestListener<Document>)
}