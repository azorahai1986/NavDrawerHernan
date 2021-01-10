package com.example.navdrawer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.navdrawer.fragmentos.HomeFragment
import com.example.navdrawer.fragmentos.ProviderType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_acceder.*


class AccederFragment : Fragment() {

    var botAcceder:Button? = null

    companion object{
        const val VOLVER = "volver"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_acceder, container, false)

        botAcceder = view.findViewById(R.id.btAcceder)
        setup()

        return view
    }


    private fun setup(){


        /* btRegistrarse?.setOnClickListener {
             if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                 FirebaseAuth.getInstance()
                     .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                     .addOnCompleteListener {
                         if (it.isSuccessful){
                             showHome(it.result?.user?.email?: "", ProviderType.BIENVENIDO)

                         }else{
                             showAlert()

                         }
                     }
             }
         }*/
        botAcceder?.setOnClickListener {


            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?: "", ProviderType.BIENVENIDO, idToken = "existe_token")


                        }else{
                            showAlertAcceder()

                        }

                    }

            }

            val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Log.e("TokenActividadRegis", task.toString())
                        val idToken = task?.result?.token?: ""
                        Log.e("TokenT", idToken)

                    } else {
                        // Handle error -> task.getException();
                    }
                }




        }
    }
    //crearé una alerta
    private fun showAlert(){
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(" Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertAcceder(){
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(" El Email ó la Contraseña son erroneos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType, idToken: String){
        HomeFragment.newInstance(idToken)
        Log.e("email", email)
        Log.e("provider", provider.toString())
        Log.e("idToken", idToken)



    }

}