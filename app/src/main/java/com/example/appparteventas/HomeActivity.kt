package com.example.appparteventas

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import java.sql.Timestamp
import java.util.*
import com.google.firebase.firestore.ServerTimestamp as ServerTimestamp
import java.util.Date as UtilDate

enum class ProviderType {
    BASIC

}

class HomeActivity : AppCompatActivity() {

    private val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Quita la toolbar
        getSupportActionBar()?.hide()

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider= bundle?.getString("provider")
        setup( email?:"", provider?: "")

        //Guardado de datos

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

    }

    private fun setup(email:String, provider: String) {
        title = "Inicio"
        emailtextView.text= email
        providertextView.text= provider


       //boton de logout
        finalizarButton.setOnClickListener {


            //borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        //boton de guardar en firestore
        save_imageButton.setOnClickListener{
           if(clienteET.text.isEmpty() && comentariosMultiLine.text.isEmpty()) {
               val builder = AlertDialog.Builder(this)
               builder.setTitle("Error")
               builder.setMessage("Es necesário poner el cliente y comentarios generales!")
               builder.setPositiveButton("Aceptar", null)
               val dialog: AlertDialog = builder.create()
               dialog.show()

           }else{
               //se crea una variable con el hashmap


               val vendedor = hashMapOf("vendedor" to emailtextView.text.toString(),
                   "cliente" to clienteET.text.toString(),
                   "comentarios" to comentariosMultiLine.text.toString(),
                   "fecha" to FieldValue.serverTimestamp(),
                            )

               //añade a la base de datos
               db.collection("vendedores")
                   .add(vendedor)
                   .addOnSuccessListener { documentReference ->
                       Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                       //pantalla de aviso de guardado
                       val builder = AlertDialog.Builder(this)
                       builder.setTitle("Guardado")
                       builder.setMessage("Se ha guardado el registro")
                       builder.setPositiveButton("Aceptar", null)
                       val dialog: AlertDialog = builder.create()
                       dialog.show()
                   }
                   .addOnFailureListener { e ->
                       Log.w(TAG, "Error adding document", e)
                   }


               //funcion para limpiar cajas
               limpiarCajas()

           }





        }

        edit_imageButton.setOnClickListener{
            db.collection("vendedores").document(email).get().addOnSuccessListener {
                clienteET.setText(it.get("cliente")as String?)
                comentariosMultiLine.setText(it.get("comentarios")as String?)
            }
            //pantalla dfe aviso de guardado
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Guardado")
            builder.setMessage("Se ha modificado el registro")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        delete_imageButton.setOnClickListener{
            db.collection("Users").document(email).delete()

            //pantalla dfe aviso de eliminado
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminado")
            builder.setMessage("Se ha eliminado el registro")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }


        //boton añadir marca
        marcaButton.setOnClickListener{
            val homeIntent = Intent(this, MarcasActivity::class.java).apply {

            }
            startActivity(homeIntent)

        }



    }



    private fun limpiarCajas() {
        //funciona para limpiar cajas
        clienteET.setText("")
        comentariosMultiLine.setText("")
    }
}


