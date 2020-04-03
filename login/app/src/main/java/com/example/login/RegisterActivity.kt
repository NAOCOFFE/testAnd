package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtRegName:EditText
    private lateinit var edtRegLastName:EditText
    private lateinit var edtRegEmail:EditText
    private lateinit var edtRegPass:EditText

    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference:DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtRegName = findViewById(R.id.edtRegName)
        edtRegLastName = findViewById(R.id.edtRegLastName)
        edtRegEmail = findViewById(R.id.edtRegEmail)
        edtRegPass = findViewById(R.id.edtRegPass)

        progressBar= findViewById(R.id.progressBar)

        database= FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")


    }

    fun register(view:View){
        crearNewAccount()

    }


    private fun crearNewAccount(){
        val nombre:String=edtRegName.text.toString()
        val apellido:String=edtRegLastName.text.toString()
        val email:String=edtRegEmail.text.toString()
        val password:String=edtRegPass.text.toString()

        if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){

        progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    task ->

                    if (task.isComplete){
                        val user:FirebaseUser? = auth.currentUser
                        verifyEmail(user)

                        val userBD = dbReference.child(user?.uid!!)

                        userBD.child("Nombre").setValue(nombre)
                        userBD.child("Apellido").setValue(apellido)

                        action()

                    }
                }
        }
    }

    private fun action(){
       startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->
                
                if(task.isComplete){
                    Toast.makeText(this, "Email Enviado!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error al enviar el email!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}