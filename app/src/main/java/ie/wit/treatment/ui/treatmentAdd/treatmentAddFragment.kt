package ie.wit.treatment.ui.treatmentAdd

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.treatment.R
import ie.wit.treatment.databinding.FragmentTreatmentBinding
import ie.wit.treatment.main.TreatmentApp
import ie.wit.treatment.models.Location
import ie.wit.treatment.models.treatmentModel
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import ie.wit.treatment.utils.showImagePicker
import com.squareup.picasso.Picasso

class treatmentAddFragment : Fragment() {

    lateinit var app: TreatmentApp
    private var _fragBinding: FragmentTreatmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController
    var treatment = treatmentModel()
    var setLocation = Location()
    private lateinit var treatmentViewModel: treatmentViewModel
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as TreatmentApp

        var edit = false


//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
////        if (ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : android.location.Location? ->
//                if (location != null){
//
//                    setLocation = Location(location.latitude,location.longitude,15f)
//                }
//                else{
//                    setLocation = Location(52.245696, -7.139102, 15f)
//                }
//            }.addOnFailureListener(){
//                setLocation = Location(52.245696, -7.139102, 15f)
//            }

        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var edit = false
        _fragBinding = FragmentTreatmentBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_treatment)

//        treatmentViewModel =
//            ViewModelProvider(this)[treatmentViewModel::class.java]
//        //val textView: TextView = root.findViewById(R.id.text_home)
//        treatmentViewModel.text.observe(viewLifecycleOwner, Observer {
//            //textView.text = it
//        })
        fragBinding.treatmentAmount.minValue = 1
        fragBinding.treatmentAmount.maxValue = 1000

//        if (intent.hasExtra("treatment_edit")) {
//            edit = true
//            treatment = intent.extras?.getParcelable("treatment_edit")!!
//            binding.tagNumber.setText(treatment.tagNumber.toString())
//            binding.treatmentName.setText(treatment.treatmentName)
//            binding.treatmentAmount.value = treatment.amount.toString().toInt()
//            binding.withdrawal.progress = treatment.withdarwal
//            binding.btnAdd.setText(R.string.save_treatment)
//            Picasso.get()
//                .load(treatment.image)
//                .into(binding.treatmentImage)
//            if (Uri.parse(treatment.image) != Uri.EMPTY) {
//                binding.chooseImage.setText(R.string.change_treatment_image)
//            }
//
//        }

        fragBinding.btnAdd.setOnClickListener() {
            treatment.tagNumber = fragBinding.tagNumber.text.toString().toInt()
            treatment.treatmentName = fragBinding.treatmentName.text.toString()
            treatment.amount = fragBinding.treatmentAmount.value.toString().toInt()
            treatment.withdarwal = fragBinding.withdrawal.progress.toString().toInt()
            treatment.affectMilk = fragBinding.affectmilkSwitch.showText
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formatted = current.format(formatter)
            treatment.date = formatted
            if (treatment.treatmentName!!.isEmpty()) {
                Snackbar.make(it,R.string.enter_treatment_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.treatmentsStore.update(treatment.copy())
                } else {
                    app.treatmentsStore.create(treatment.copy())
//                    uploadData()
                }
            }
            Timber.v("add Button Pressed: $treatment")
        }
        fragBinding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }



//        fragBinding.treatmentLocation.setOnClickListener {
//            if (treatment.zoom != 0f) {
//                setLocation.lat =  treatment.lat
//                setLocation.lng = treatment.lng
//                setLocation.zoom = treatment.zoom
//            }
//            val launcherIntent = Intent(this, MapActivity::class.java)
//                .putExtra("location", setLocation)
//            mapIntentLauncher.launch(launcherIntent)
//        }
        registerImagePickerCallback()
//        registerMapCallback()

        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                treatmentAddFragment().apply {
                    arguments = Bundle().apply {}
                }
    }



    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.v("Got Result ${result.data!!.data}")
                            treatment.image = result.data!!.data.toString()!!
                            Picasso.get()
                                .load(Uri.parse(treatment.image))
                                .into(fragBinding.treatmentImage)

                            fragBinding.chooseImage.setText(R.string.change_treatment_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_treatment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
                requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}