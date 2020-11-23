package com.example.temperaturetracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.temperaturetracking.R
import kotlinx.android.synthetic.main.fragment_detail_info.*

/*
* KHÔNG DÙNG TỚI
* */
class DetailInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ibBackFromDetailInfo.setOnClickListener {
            activity?.let {
                it.supportFragmentManager.beginTransaction().detach(this).commit()
            }
        }
    }
}
