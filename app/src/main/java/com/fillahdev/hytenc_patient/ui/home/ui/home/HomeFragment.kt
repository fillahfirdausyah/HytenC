package com.fillahdev.hytenc_patient.ui.home.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        firestore = Firebase.firestore
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val patientName = auth.currentUser?.displayName
        setupActionBar()
        logFcm(auth.currentUser!!)

        binding.btnTellSupervisor.setOnClickListener {
            homeViewModel.tellSupervisor(patientName.toString())
        }

        homeViewModel.getCurrentShcedule(patientName.toString())
        homeViewModel.getAllTips()

        // OBSERVE
        homeViewModel.listTips.observe(viewLifecycleOwner) { listTips ->
            setupAdapter(listTips)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setupLoading(isLoading)
        }

        homeViewModel.isTaken.observe(viewLifecycleOwner) { isTaken ->
            updateBanner(isTaken)
        }

    }

    private fun logFcm(currentUser: FirebaseUser) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            val fcmToken = token
            firestore.collection("Patient").document(currentUser.displayName.toString())
                .update(
                    mapOf(
                        "Uid" to currentUser.uid,
                        "FcmToken" to fcmToken
                    )
                ).addOnFailureListener {
                    Toast.makeText(requireContext(), "Fail to register token", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Exception", it.message.toString())
                }
        }
    }

    private fun updateBanner(isTaken: String) {
        when (isTaken) {
            "false" -> {
                binding.bannerNotifications.background =
                    resources.getDrawable(R.drawable.shape_banner_state_danger)
                binding.tvActionState.text =
                    resources.getString(R.string.text_havenot_take_medicin)
                binding.ivBannerState.setImageResource(R.drawable.pose_5)
                binding.btnTellSupervisor.visibility = View.VISIBLE
            }
            "true" -> {
                binding.bannerNotifications.background =
                    resources.getDrawable(R.drawable.shape_banner_state_safe)
                binding.tvActionState.text =
                    resources.getString(R.string.text_have_take_medicin)
                binding.ivBannerState.setImageResource(R.drawable.pose_7)
                binding.btnTellSupervisor.visibility = View.GONE
            }
            else -> {
                binding.bannerNotifications.background =
                    resources.getDrawable(R.drawable.shape_banner_state_safe)
                binding.tvActionState.text =
                    resources.getString(R.string.text_state_init)
                binding.ivBannerState.setImageResource(R.drawable.pose_6_full)
                binding.btnTellSupervisor.visibility = View.GONE
            }
        }
    }

    private fun setupLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvActionState.visibility = View.GONE
            binding.ivBannerState.visibility = View.GONE
            binding.pbInformation.visibility = View.VISIBLE
        } else {
            binding.tvActionState.visibility = View.VISIBLE
            binding.ivBannerState.visibility = View.VISIBLE
            binding.pbInformation.visibility = View.GONE
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