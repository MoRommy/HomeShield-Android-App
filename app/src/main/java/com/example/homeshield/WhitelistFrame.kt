package com.example.homeshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class WhitelistFrame : Fragment(R.layout.frame_whitelist), BackPressHandler {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frame_whitelist, container, false)
        return view
    }

    override fun onBackPressed(): Boolean {
        parentFragmentManager.beginTransaction().remove(this).commit()
        return true
    }
}
