package com.example.instafire

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instafire.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "logInActivity"
class logInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser!= null){
            goPostsActivity()
        }

        binding.logIn.setOnClickListener {
            binding.logIn.isEnabled = false
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(email.isBlank() || password.isBlank()){
                binding.logIn.isEnabled = true
                Toast.makeText(this,"Email/password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

                //firebase authentication check

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                task ->
                binding.logIn.isEnabled = true
                if(task.isSuccessful){
                    Toast.makeText(this,"Success!", Toast.LENGTH_SHORT).show()
                    goPostsActivity()
            }else{
                Log.e(TAG,"signinwithEmail failed", task.exception)
                Toast.makeText(this,"Authentication failed", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }

    private fun goPostsActivity() {
        Log.i(TAG,"goPostsActivity")
        val intent = Intent(this,PostsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
