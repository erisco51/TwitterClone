package com.example.twitterclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.twitterclone.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener =FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTextChangedListener(emailLET, emailTIL)
        setTextChangedListener(passwordLET, passwordTIL)

        loginProcessLayout.setOnTouchListener { view, motionEvent -> true }

    }
    fun setTextChangedListener(et:EditText, til:TextInputLayout) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                til.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
    fun onLogin (v: View) {
        var proceed =true
        if(emailLET.text.isNullOrEmpty()) {
            emailTIL.error = "Email Is Required"
            emailTIL.isErrorEnabled = true
            proceed = false
        }
        if(passwordLET.text.isNullOrEmpty()) {
            passwordTIL.error ="Password is Required"
            passwordTIL.isErrorEnabled = true
            proceed = false
        }
        if(proceed) {
            loginProcessLayout.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(emailLET.text.toString(),passwordLET.text.toString())
                .addOnCompleteListener {
                    if (!it.isSuccessful){
                        loginProcessLayout.visibility =View.GONE
                        Toast.makeText( this@LoginActivity,"Login error: ${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()

                    }


                }
                .addOnFailureListener {
                    it.printStackTrace()
                    loginProcessLayout.visibility =View.GONE
                }
        }
    }
    fun goToSignup(v: View){
        startActivity(SignupActivity.newIntent(this))
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
    companion object{
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}