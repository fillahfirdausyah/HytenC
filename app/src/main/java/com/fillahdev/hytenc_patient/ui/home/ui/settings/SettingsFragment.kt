package com.fillahdev.hytenc_patient.ui.home.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fillahdev.hytenc_patient.R
import com.fillahdev.hytenc_patient.databinding.FragmentSettingsBinding
import com.fillahdev.hytenc_patient.service.AlarmReceiver
import com.fillahdev.hytenc_patient.ui.settings.SettingsActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alarmReceiver = AlarmReceiver()
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuNotification.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}