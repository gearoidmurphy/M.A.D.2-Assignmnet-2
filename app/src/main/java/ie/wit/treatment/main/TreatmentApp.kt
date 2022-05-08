package ie.wit.treatment.main

import android.app.Application
import ie.wit.treatment.models.TreatmentMemStore
import ie.wit.treatment.models.TreatmentJSONStore
import ie.wit.treatment.models.TreatmentStore
import timber.log.Timber

class TreatmentApp : Application() {

    lateinit var treatmentsStore: TreatmentStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        treatmentsStore = TreatmentMemStore()
        treatmentsStore = TreatmentJSONStore(applicationContext)
        Timber.i("Treatment Application Started")
    }
}