package ie.wit.treatment.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.treatment.R
import ie.wit.treatment.adapters.TreatmentAdapter
import ie.wit.treatment.adapters.treatmentListener
import ie.wit.treatment.databinding.FragmentReportBinding
import ie.wit.treatment.main.TreatmentApp
import ie.wit.treatment.models.treatmentModel
import ie.wit.treatment.ui.treatmentAdd.treatmentAddFragment

class ReportFragment : Fragment(), treatmentListener {

    lateinit var app: TreatmentApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var _fragBinding: FragmentReportBinding? = null
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController

    private lateinit var reportViewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as TreatmentApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_report)

        reportViewModel =
                ViewModelProvider(this).get(ReportViewModel::class.java)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        reportViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        loadtreatments()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
                requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun loadtreatments() {
        showtreatments(app.treatmentsStore.findAll())
    }

    fun showtreatments (treatments: List<treatmentModel>) {
        fragBinding.recyclerView.adapter = TreatmentAdapter(treatments, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ReportFragment().apply {
                    arguments = Bundle().apply { }
                }
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadtreatments() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun ontreatmentClick(treatment: treatmentModel) {
//        val launcherIntent = Intent(treatment, treatmentAddFragment::class.java)
//        launcherIntent.putExtra("treatment_edit", treatment)
//        refreshIntentLauncher.launch(launcherIntent)
    }
}