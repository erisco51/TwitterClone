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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.emailLET
import kotlinx.android.synthetic.main.activity_signup.emailTIL
import kotlinx.android.synthetic.main.activity_signup.passwordLET
import kotlinx.android.synthetic.main.activity_signup.passwordTIL
import util.DATA_USERS
import util.User

class SignupActivity : AppCompatActivity() {
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setTextChangedListener(emailLET, emailTIL)
        setTextChangedListener(usernameET, usernameTIL)
        setTextChangedListener(passwordLET, passwordTIL)

        signupProcessLayout.setOnTouchListener { view, motionEvent -> true }
    }
    fun setTextChangedListener(et: EditText, til: TextInputLayout) {
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
    fun onSignup(v: View) {
            var proceed =true
        if(usernameET.text.isNullOrEmpty()) {
            usernameTIL.error = "Username Is Required"
            usernameTIL.isErrorEnabled = true
            proceed = false
        }
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
                signupProcessLayout.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(emailLET.text.toString(), passwordLET.text.toString())
                    .addOnCompleteListener {
                        if (!it.isSuccessful){
                            signupProcessLayout.visibility =View.GONE
                            Toast.makeText( this@SignupActivity,"Signup error: ${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()

                        }
                        else {
                            val email = emailLET.text.toString()
                            val name = usernameET.text.toString()
                            val user = User(email, name, imageurl = "", arrayListOf(), arrayListOf())
                            firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)

                        }


                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        signupProcessLayout.visibility =View.GONE
                    }
            }

    }
    fun goToLogin(v: View) {
        startActivity(LoginActivity.newIntent(this))
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

    companion object {
        fun newIntent(context: Context)= Intent(context, SignupActivity::class.java)
    }
}