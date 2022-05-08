package ie.wit.treatment.models

import androidx.lifecycle.MutableLiveData
import timber.log.Timber

var lastId = 0

internal fun getId(): Long {
    return lastId++.toLong()
}

class TreatmentMemStore : TreatmentStore {

    val treatments = ArrayList<treatmentModel>()


    override fun findAll(): List<treatmentModel> {
        return treatments
    }

    override fun findByName(name: String): ArrayList<treatmentModel> {
        val foundTreatments = ArrayList<treatmentModel>()
        treatments.forEach() { p -> p.treatmentName; if(p.treatmentName == name){
            foundTreatments.add(p)
        } }

        return foundTreatments
    }


    override fun create(treatment: treatmentModel) {
        treatment.id = getId()
        treatments.add(treatment)
        logAll()
    }

    override fun delete(treatment: treatmentModel){
        treatments.remove(treatment)
        Timber.v("delete")
    }

    override fun update(treatment: treatmentModel) {
        var foundtreatment: treatmentModel? = treatments.find { p -> p.id == treatment.id }
        if (foundtreatment != null) {
            foundtreatment.tagNumber = treatment.tagNumber
            foundtreatment.treatmentName = treatment.treatmentName
            foundtreatment.withdarwal = treatment.withdarwal
            foundtreatment.affectMilk = treatment.affectMilk
            foundtreatment.amount = treatment.amount
            foundtreatment.image = treatment.image
            foundtreatment.lat = treatment.lat
            foundtreatment.lng = treatment.lng
            foundtreatment.zoom = treatment.zoom
            logAll()
        }
    }


    fun logAll() {
        Timber.v("** Treatments List **")
        treatments.forEach{ Timber.v("${it}") }
    }

}