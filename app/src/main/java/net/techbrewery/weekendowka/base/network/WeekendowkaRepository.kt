package net.techbrewery.weekendowka.base.network

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import net.techbrewery.weekendowka.base.Collection
import net.techbrewery.weekendowka.model.Company
import net.techbrewery.weekendowka.model.Document


/**
 * Created by Jacek Kwiecień on 13.10.2017.
 */
class WeekendowkaRepository(val context: Context) : Repository {

    override fun signInAnonymously(requestListener: FirestoreRequestListener<FirebaseUser>) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                requestListener.onSuccess(task.result.user)
            } else {
                requestListener.onFailure(task.exception ?: NullPointerException("Exception without exception... this shouldn't happen... like ever"))
            }
        }
    }

    override fun saveCompany(company: Company, requestListener: FirestoreRequestListener<Company>) {
        FirebaseFirestore.getInstance()
                .collection(Collection.COMPANIES)
                .document(company.id)
                .set(company)
                .addOnSuccessListener { requestListener.onSuccess(company) }
                .addOnFailureListener { error -> requestListener.onFailure(error) }
    }

    override fun saveSelectedDeclarer(companyId: String, declarerId: String) {
        FirebaseFirestore.getInstance()
                .collection(Collection.COMPANIES)
                .document(companyId)
                .update("selectedDeclarerId", declarerId)
    }

    override fun saveSelectedDriver(companyId: String, driverId: String) {
        FirebaseFirestore.getInstance()
                .collection(Collection.COMPANIES)
                .document(companyId)
                .update("selectedDriverId", driverId)
    }

    override fun saveDocument(companyId: String, document: Document, requestListener: FirestoreRequestListener<Document>) {
        FirebaseFirestore.getInstance()
                .collection(Collection.COMPANIES)
                .document(companyId)
                .collection(Collection.DOCUMENTS)
                .document(document.id)
                .set(document)
                .addOnSuccessListener { requestListener.onSuccess(document) }
                .addOnFailureListener { error -> requestListener.onFailure(error) }
    }

    override fun getCompany(userId: String, requestListener: FirestoreRequestListener<Company?>) {
        FirebaseFirestore.getInstance()
                .collection(Collection.COMPANIES)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val company = querySnapshot.documents[0].toObject(Company::class.java)
                        requestListener.onSuccess(company)
                    } else {
                        requestListener.onSuccess(null)
                    }
                }
                .addOnFailureListener { error -> requestListener.onFailure(error) }
    }
}
