package com.example.proyectofinal.viewmodels

import android.content.Context
import android.telephony.ims.ImsMmTelManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var nameText: TextView
    private lateinit var lastNameText: TextView
    private lateinit var telText: TextView
    private lateinit var provText: TextView
    private lateinit var cityText: TextView

    private lateinit var name: String
    private lateinit var last: String
    private lateinit var tel: String
    private lateinit var prov: String
    private lateinit var ciu: String


    fun saveData(v: View): Boolean {

        var ok = false
        nameText = v.findViewById(R.id.nameEdit)
        lastNameText = v.findViewById(R.id.lastEdit)
        telText = v.findViewById(R.id.telEdit)
        provText = v.findViewById(R.id.provEdit)
        cityText = v.findViewById(R.id.cityEdit)

        val name = nameText.text.toString()
        val lName = lastNameText.text.toString()
        val tel = telText.text.toString()
        val prov = provText.text.toString()
        val city = cityText.text.toString()

        val user = hashMapOf(
            "nombre" to name,
            "apellido" to lName,
            "telefono" to tel,
            "provincia" to prov,
            "ciudad" to city
        )

        if (name.isNotEmpty() && lName.isNotEmpty() && tel.isNotEmpty() && prov.isNotEmpty() && city.isNotEmpty()) {
            db.collection("users").document(UserRepository.userMailLogin)
                .set(user, SetOptions.merge())
            ok = true
        } else {

        }
        return ok
    }

    fun failSaveDataMessage(v: View, context: Context) {
        val text = "Todo los campos deben estar completos"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    fun saveDataMessage(v: View, context: Context) {
        val text = "Los datos fueron guardados correctamente"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration)
        toast.show()

    }

    fun showData(v: View) {

        nameText = v.findViewById(R.id.nameEdit)
        lastNameText = v.findViewById(R.id.lastEdit)
        telText = v.findViewById(R.id.telEdit)
        provText = v.findViewById(R.id.provEdit)
        cityText = v.findViewById(R.id.cityEdit)

        val docRef = db.collection("users").document(UserRepository.userMailLogin)

        docRef.get().addOnCompleteListener { document ->
            if (document != null) {

                name = document.result.get("nombre").toString()
                last = document.result.get("apellido").toString()
                tel = document.result.get("telefono").toString()
                prov = document.result.get("provincia").toString()
                ciu = document.result.get("ciudad").toString()

                if (name != "null") {
                    nameText.text = name
                } else {
                    nameText.text = ""
                }
                if (last != "null") {
                    lastNameText.text = last
                } else {
                    lastNameText.text = ""
                }
                if (tel != "null") {
                    telText.text = tel
                } else {
                    telText.text = ""
                }
                if (prov != "null") {
                    provText.text = prov
                } else {
                    provText.text = ""
                }
                if (ciu != "null") {
                    cityText.text = ciu
                } else {
                    cityText.text = ""
                }
            }
        }
    }
}