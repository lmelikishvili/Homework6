package com.example.homework6

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.homework6.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        emailFocusListener()
        passFocusListener()
        ageFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()
        getRegisteredUserCount()

        binding.btnAdd.setOnClickListener(){
            val firstName = binding.edfirstName.text.toString()
            val lastName = binding.edLastName.text.toString()
            val age = binding.edAge.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edpass.text.toString()
            var userID: String

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){

                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                    userID = firebaseAuth.currentUser?.uid ?: " "
                    var user = mutableMapOf<String,String>()
                    user["fName"] = firstName
                    user["lName"] = lastName
                    user["age"] = age
                    firestore.collection("users").document(userID).set(user).addOnCompleteListener() {
                        if (it.isSuccessful){
                            getRegisteredUserCount()
                            Toast.makeText(this, "Userdata added to collection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    getRegisteredUserCount()
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }



    private fun emailFocusListener(){
        binding.edEmail.setOnFocusChangeListener{_, focused ->
            if (!focused){
                binding.email.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.edEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return  "Invalid Email"
        }
        return null
    }

    private fun passFocusListener(){
        binding.edpass.setOnFocusChangeListener{_, focused ->
            if (!focused){
                binding.pass.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passText = binding.edpass.text.toString()
        if (passText.length < 8){
            return "Minimum 8 charaqters required"
        }
        return null
    }

    private fun ageFocusListener(){
        binding.edAge.setOnFocusChangeListener{_, focused ->
            if (!focused){
                if(binding.edAge.text.toString().isNullOrEmpty()){
                    binding.age.helperText = "Cant be empty"
                }else{
                    binding.age.helperText = null
                }

            }
        }
    }

    private fun firstNameFocusListener(){
        binding.edfirstName.setOnFocusChangeListener{_, focused ->
            if (!focused){
                if(binding.edfirstName.text.toString().isNullOrEmpty()){
                    binding.firstName.helperText = "Cant be empty"
                }else{
                    binding.firstName.helperText = null
                }
            }
        }
    }

    private fun lastNameFocusListener(){
        binding.edLastName.setOnFocusChangeListener{_, focused ->
            if (!focused){
                if(binding.edLastName.text.toString().isNullOrEmpty()){
                    binding.lastName.helperText = "Cant be empty"
                }else{
                    binding.lastName.helperText = null
                }
            }
        }
    }

    fun getRegisteredUserCount(){
        var count = firestore.collection("users").count()
        count.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Count fetched successfully
                val snapshot = task.result
                binding.tvActivs.text = "Active Users: ${snapshot.count}"
                Log.d(TAG, "Count: ${snapshot.count}")
            } else {
                Log.d(TAG, "Count failed: ", task.getException())
            }
        }
    }
}