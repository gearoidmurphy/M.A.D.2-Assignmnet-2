package ie.wit.treatment.models

import androidx.lifecycle.MutableLiveData

interface TreatmentStore {
    fun findAll(): List<treatmentModel>
    fun create(treatment: treatmentModel)
    fun delete(treatment: treatmentModel)
    fun update(treatment: treatmentModel)
    fun findByName(name: String): ArrayList<treatmentModel>
}