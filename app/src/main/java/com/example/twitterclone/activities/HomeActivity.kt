package com.example.twitterclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.twitterclone.R
import com.example.twitterclone.activities.fragments.HomeFragment
import com.example.twitterclone.activities.fragments.MyActivityFragment
import com.example.twitterclone.activities.fragments.SearchFragment
import com.example.twitterclone.activities.fragments.TwitterFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var sectionPageAdapter: SectionPageAdapter? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val myActivityFragment = MyActivityFragment()
    private var userid = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseDB  = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sectionPageAdapter = SectionPageAdapter(supportFragmentManager)
        container.adapter = sectionPageAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        logo.setOnClickListener { view ->
            startActivity(ProfileActivity.newIntent(this))

        }

    }


    override fun onResume() {
        super.onResume()
        userid = FirebaseAuth.getInstance().currentUser?.uid
        if (userid == null) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
    }
    inner class SectionPageAdapter(fm: FragmentManager) :FragmentPagerAdapter(fm) {
        override fun getCount()= 3

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> homeFragment
                1 -> searchFragment
                else -> myActivityFragment
            }

        }

    }

    companion object {
    fun newIntent(context:Context)=Intent(context, HomeActivity::class.java)
    }
}