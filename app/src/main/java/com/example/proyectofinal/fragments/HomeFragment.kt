package com.example.proyectofinal.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.APIService
import com.example.proyectofinal.entities.Dti
import com.example.proyectofinal.entities.RestEngine
import com.example.proyectofinal.entities.UserRepository.ListDti
import com.example.proyectofinal.entities.UserRepository.userBeachSelect
import com.example.proyectofinal.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private lateinit var v: View
    private val vm: HomeViewModel by viewModels()

    private val db = FirebaseFirestore.getInstance()
    private lateinit var listPopupWindowButton: Button
    private lateinit var pb: ProgressBar
    private lateinit var bOut: Button
    private lateinit var bContacto: Button
    private lateinit var goBeachButton: Button
    private lateinit var listPopupWindow: ListPopupWindow
    private var dtiDocument: String = "1"
    private var listDtiNombres = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        bContacto = v.findViewById(R.id.btnContacto)
        bOut = v.findViewById(R.id.btnOut)
        listPopupWindowButton = v.findViewById(R.id.list_popup_button)
        goBeachButton = v.findViewById(R.id.goBeachBtn)
        listPopupWindow =
            ListPopupWindow(requireContext(), null, androidx.transition.R.attr.listPopupWindowStyle)
        pb = v.findViewById(R.id.progressBar)

        return v

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch(Dispatchers.Main) {

            pb.visibility = View.VISIBLE
            withContext(Dispatchers.IO) { callServiceGetDti() }
            withContext(Dispatchers.IO) { vm.populateFavs() }
            pb.visibility = View.GONE
        }

        listPopupWindow.anchorView = listPopupWindowButton

        val adapter =
            ArrayAdapter(requireContext(), R.layout.list_popup_window_item, listDtiNombres)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->

            vm.showData(position, v)
            dtiDocument = position.toString()
            userBeachSelect = position.toString()
            listPopupWindow.dismiss()
        }

        listPopupWindowButton.setOnClickListener { listPopupWindow.show() }

        goBeachButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBeachFragment(dtiDocument)
            v.findNavController().navigate(action)

        }

        //LOGOUT
        bOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            vm.cleanLogUser()
            activity?.onBackPressed()
        }

        bContacto.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToContactoFragment()
            v.findNavController().navigate(action)
        }
    }

    private fun callServiceGetDti() {
        val dtiService: APIService = RestEngine.getRetrofitDtis().create(APIService::class.java)
        val result: Call<List<Dti>> = dtiService.getDtiList()

        result.enqueue(object : Callback<List<Dti>> {
            override fun onResponse(
                call: Call<List<Dti>>,
                response: Response<List<Dti>>
            ) {

                val r = response.body()
                if (r != null) {
                    ListDti = r
                }

                //Toast.makeText(requireContext(), "DTIs Cargados", Toast.LENGTH_SHORT).show()
                getDtiNames(ListDti)
                vm.showData(userBeachSelect.toInt(), v)
            }

            override fun onFailure(call: Call<List<Dti>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error en lectura", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getDtiNames(list: List<Dti>): MutableList<String> {

        for (l in list) {
            listDtiNombres.addAll(listOf(l.nombre))
        }
        return listDtiNombres
    }

}




