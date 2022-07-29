package com.fillahdev.hytenc_patient.ui.home.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.fillahdev.hytenc_patient.R
import com.fillahdev.hytenc_patient.adapter.ListTipsAdapter
import com.fillahdev.hytenc_patient.data.Tips
import com.fillahdev.hytenc_patient.databinding.FragmentHomeBinding
import com.fillahdev.hytenc_patient.service.AlarmReceiver

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
        homeViewModel.getAllTips()
        homeViewModel.listTips.observe(viewLifecycleOwner) { listTips ->
            setupAdapter(listTips)
        }
    }

    private fun setupAdapter(listTips: List<Tips>) {
        val adapter = ListTipsAdapter(listTips)
        binding.rvListTips.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListTips.setHasFixedSize(true)
        binding.rvListTips.adapter = adapter
    }

    private fun setupActionBar() {
        activity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = Color.TRANSPARENT
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
        }
        activity?.actionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}