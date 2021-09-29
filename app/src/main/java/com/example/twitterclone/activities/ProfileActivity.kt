package com.example.twitterclone.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.twitterclone.R
import com.example.twitterclone.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import util.DATA_USERS
import util.DATA_USER_EMAIL
import util.DATA_USER_USERNAME
import util.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB  = FirebaseFirestore.getInstance()
    private val userid = FirebaseAuth.getInstance().currentUser?.uid
    private val firebasStorage = FirebaseStorage.getInstance()
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(userid == null) {
            finish()
        }
        profileProcessLayout.setOnTouchListener { view, motionEvent -> true }

        fun populateinfo() {
            profileProcessLayout.visibility = View.VISIBLE
            firebaseDB.collection(DATA_USERS).document(userid!!).get()
                .addOnSuccessListener { DocumentSnapshot ->
                    val user = DocumentSnapshot.toObject(User::class.java)
                    usernameET.setText(user?.username, TextView.BufferType.EDITABLE)
                    emailLET.setText(user?.email, TextView.BufferType.EDITABLE)
                    imageUrl?. let {

                    }
                    profileProcessLayout.visibility = View.GONE

                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    finish()
                }
        }
    }
    fun onApply(v: View) {
        profileProcessLayout.visibility = View.VISIBLE
        val username = usernameET.text.toString()
        val email = emailLET.text.toString()
        val map = HashMap<String, Any>()
        map[DATA_USER_USERNAME] = username
        map[DATA_USER_EMAIL] = email

        firebaseDB.collection(DATA_USERS).document(userid!!).update(map)
            .addOnSuccessListener { Toast.makeText(this, "update successful", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "update failed Please try again", Toast.LENGTH_SHORT).show()
                profileProcessLayout.visibility = View.GONE
            }
    }
    fun onSignout(v: View) {
        firebaseAuth.signOut()
        finish()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_profile)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        fun newIntent(context: Context)= Intent(context, ProfileActivity::class.java)
    }
}