package com.example.project4.main

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project4.R
import com.example.project4.databinding.RockStatsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RockStats : BottomSheetDialogFragment() {

    private var _binding: RockStatsBinding? = null
    private val binding get() = _binding!!

    var listener: RockStatsListener? = null

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RockStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            val name = binding.rockNameInput.text.toString()
            listener?.onRockNameEntered(name)
            dismiss()
        }

    }

    interface RockStatsListener {
        fun onRockNameEntered(name: String)
    }

    override fun onStart() {
        super.onStart()

        // make bottom sheet only take up bottom half of screen
        val bottomSheetDialog = dialog as? BottomSheetDialog
        val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            val layoutParams = it.layoutParams
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            layoutParams.height = screenHeight / 2  // half of screen height
            it.layoutParams = layoutParams
        }
    }
}