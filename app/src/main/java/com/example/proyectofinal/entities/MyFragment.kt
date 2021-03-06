package com.example.proyectofinal.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.SplashActivity
import com.example.proyectofinal.entities.UserRepository.userMailLogin
import com.example.proyectofinal.fragments.HomeFragmentDirections
import com.example.proyectofinal.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

class MyFragment: DialogFragment() {

    private val vm: HomeViewModel by viewModels()
    private lateinit var usuario : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_dialog, container, false)
        val cancelButton = rootView.findViewById<Button>(R.id.cancelBtn)
        val acceptButton = rootView.findViewById<Button>(R.id.aceptBtn)
        usuario = rootView.findViewById(R.id.mailUs)
        usuario.text = userMailLogin

        cancelButton.setOnClickListener {
            dismiss()
        }
        acceptButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            cleanLogUser()
            onDestroyView()
            activity?.finish()
        }

        return rootView
    }

    private fun cleanLogUser(){
        userMailLogin = ""
    }
}