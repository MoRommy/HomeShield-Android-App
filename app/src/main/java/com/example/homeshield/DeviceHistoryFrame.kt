package com.example.homeshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DeviceHistoryFrame(private val mqttHelper: MqttHelper) : Fragment(), BackPressHandler {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frame_device_history, container, false)

        // Dataset for the RecyclerView
        val dataset = arrayOf(
            Event(null, "OPEN", "USER", "99/99/99-99:99:9999"),
            Event(null, "CLOSE", "ANPR", "99/99/99-99:99:9999")
        )
        val customAdapter = DeviceHistoryAdapter(dataset)

        // RecyclerView setup
        val recyclerView: RecyclerView = view.findViewById(R.id.deviceHistoryRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = customAdapter

        // Whitelist setup
        val editWhitelistButton: Button = view.findViewById(R.id.editWhitelistButton)
        editWhitelistButton.setOnClickListener {
            if (savedInstanceState == null) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.whitelistFrame, WhitelistFrame(mqttHelper))
                    .commitNow()
            }
        }

        return view
    }

    override fun onBackPressed(): Boolean {
        // Check if the child fragment is handling the back press
        val childFragment = childFragmentManager.findFragmentById(R.id.whitelistFrame)
        if (childFragment is BackPressHandler && childFragment.onBackPressed()) {
            return true
        }

        // Handle the back button press logic here for the parent fragment
        parentFragmentManager.beginTransaction().remove(this).commit()
        return true // Indicate that the back press event has been handled
    }
}
