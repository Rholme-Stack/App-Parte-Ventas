package com.example.appparteventas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        // Initialize Firebase Auth
        auth = Firebase.auth
        setup()
        session()



    }

    override fun onStart() {
        super.onStart()
        authLayaout.visibility = View.VISIBLE
    }

    private fun session() {
        //funcion para comprobar sesion activa , recuperar email y provider
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email= prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email != null && provider != null){
            authLayaout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }

    }


    private fun setup() {
        title = "Login"
        registrarButton.setOnClickListener {
            if (emailET.text.isNotEmpty() && passwordET.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailET.text.toString(),
                    passwordET.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                    } else {
                        showAlert()
                    }

                }

            }
        }
        accederButton.setOnClickListener {
            if (emailET.text.isNotEmpty() && passwordET.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailET.text.toString(),
                    passwordET.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)

                    } else {
                        showAlert()
                    }

                }

            }
        }

    }

    private fun showHome( email: String, provider: ProviderType) {



        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            }
        startActivity(homeIntent)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticación de usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }









}