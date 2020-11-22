package com.example.temperaturetracking.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.temperaturetracking.R
import com.example.temperaturetracking.util.FunctionalHelper
import kotlinx.android.synthetic.main.item_drawer_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var teamMemberAdapter: TeamMemberAdapter
    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, homeFragment, "home").commit()
        loadMemberInfo()
    }

    private fun loadMemberInfo() {
        val members = FunctionalHelper.getMemberInfo()
        if(!this::teamMemberAdapter.isInitialized) {
            teamMemberAdapter = TeamMemberAdapter()
        }
        teamMemberAdapter.submitList(members)
        rvOurTeam.adapter = teamMemberAdapter
    }
}
