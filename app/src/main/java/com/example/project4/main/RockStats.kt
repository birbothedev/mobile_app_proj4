package com.example.project4.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project4.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RockStats : BottomSheetDialogFragment() {

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rock_stats, container, false)

        return view
    }

}