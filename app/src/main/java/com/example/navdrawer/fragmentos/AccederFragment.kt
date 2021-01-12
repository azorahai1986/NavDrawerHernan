package com.example.navdrawer.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.navdrawer.R
import com.example.navdrawer.actividades.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_acceder.*


class AccederFragment : Fragment() {

    var botAcceder:Button? = null
    var tokenParaHome:String? = null
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

        botAcceder?.setOnClickListener {


            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?: "", ProviderType.BIENVENIDO)
                            tokenParaHome = "existe_token"
                           activity?.finish()

                           /* homeFragment = HomeFragment()
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.frame_layout, homeFragment)
                                ?.commit()*/

                        }else{
                            showAlertAcceder()

                        }

                    }

            }

           /* val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        var idToken = task?.result?.token?: ""
                        Log.e("TokenReal", idToken)

                    } else {
                        // Handle error -> task.getException();
                    }
                }*/




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

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(context, MainActivity::class.java)
        homeIntent.putExtra("email", email)
        homeIntent.putExtra("provider", provider.name)
        startActivity(homeIntent)


    }

}