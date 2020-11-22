package com.example.temperaturetracking.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.temperaturetracking.R
import com.example.temperaturetracking.data.entity.TemperatureItem
import com.example.temperaturetracking.util.ConstantHelper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private val temperatureViewModel by viewModel<TemperatureViewModel>()

    private lateinit var responseBroadcast: BroadcastReceiver
    private lateinit var temperatureAdapter: TemperatureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBroadcast()
        registerObserver()
        listenBroadcast()
        setViewOnClickListener()
        sayGreeting()
        temperatureViewModel.registerToFCM()
        temperatureViewModel.getLatestTemperature()
        temperatureViewModel.getCurrentThreeDays()
    }

    private fun registerBroadcast() {
        responseBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ConstantHelper.PUSH_NOTIFICATION -> {
                        if (intent != null) {
                            val id = intent.getStringExtra("id")
                            val content = intent.getStringExtra("message")
                            if (!id.isNullOrEmpty() && !content.isNullOrEmpty()) {
                                Log.d("###", "New message: $content")
                                updateMainContainer(content.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun listenBroadcast() {
        activity?.registerReceiver(
            responseBroadcast,
            IntentFilter(ConstantHelper.PUSH_NOTIFICATION)
        )

    }

    private fun registerObserver() {
        temperatureViewModel.getCurrentThreeDaysValue().observe(viewLifecycleOwner, Observer {
            updateSpinnerData(it)
        })
        temperatureViewModel.getTemperatureOfDateValue().observe(viewLifecycleOwner, Observer {
            updateMiniTempContainer(it)
        })
        temperatureViewModel.getLatestTemperatureValue().observe(viewLifecycleOwner, Observer {
            updateMainContainer(it.temperature)
        })
    }

    private fun updateSpinnerData(date: com.example.temperaturetracking.data.entity.Date) {
        val listDay = mutableListOf(
            date.today,
            date.yesterday
        )
        activity?.let {
            ArrayAdapter(it, R.layout.item_layout_spinner, listDay).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDay.adapter = arrayAdapter
            }
        }
        tvNoData.visibility = View.GONE
        swipeLayout.isRefreshing = false
    }

    // Cập nhật nhiệt độ hiện tại và cảnh báo
    private fun updateMainContainer(temperature: String) {
        tvNoCurrentData.visibility = View.GONE
        tvDegree.visibility = View.VISIBLE
        tvTemperature.text = temperature
        tvAlert.text = when (temperature.toFloat()) {
            in 0.0..19.9 -> {
                getString(R.string.alert_lv_2)
            }
            in 20.0..28.0 -> {
                getString(R.string.alert_lv_1)
            }
            in 29.0..37.5 -> {
                getString(R.string.alert_lv0)
            }
            in 37.6..39.0 -> {
                getString(R.string.alert_lv1)
            }
            else -> {
                getString(R.string.alert_lv2)
            }
        }
    }

    // Cập nhật nhiệt độ của một ngày lên UI
    private fun updateMiniTempContainer(temperatureOfDay: MutableList<TemperatureItem>) {
        if (!this::temperatureAdapter.isInitialized) {
            temperatureAdapter = TemperatureAdapter()
        }
        temperatureAdapter.submitList(temperatureOfDay)
        rvMiniInfo.adapter = temperatureAdapter
        pbMiniTemperature.visibility = View.GONE
    }

    // Hiện thỉ lời chào
    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        val date = Date(time)
        val cal = Calendar.getInstance()
        cal.time = date
        when (cal[Calendar.HOUR_OF_DAY]) {
            in 4..10 -> {
                tvGreeting.text = getString(R.string.good_morning)
                Glide.with(this)
                    .load(R.drawable.day)
                    .into(ivHomeBackground)
            }
            in 11..18 -> {
                tvGreeting.text = getString(R.string.good_afternoon)
                Glide.with(this)
                    .load(R.drawable.sample)
                    .into(ivHomeBackground)
            }
            else -> {
                tvGreeting.text = getString(R.string.good_evening)
                Glide.with(this)
                    .load(R.drawable.night)
                    .into(ivHomeBackground)
            }
        }
    }

    private fun setViewOnClickListener() {
        // Khi chọn một ngày trong dropdown box, load nhiệt độ ngày đó lên
        spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("###", "Position: $position")
                Log.d("###", "Id: $id")
                pbMiniTemperature.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    delay(2000)
                    temperatureViewModel.getTemperatureOfDate(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        swipeLayout.setOnRefreshListener {
            swipeLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorStatusBar)
            temperatureViewModel.getCurrentThreeDays()
            temperatureViewModel.getLatestTemperature()
        }
    }
}
