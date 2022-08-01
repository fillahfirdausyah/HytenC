package com.fillahdev.hytenc_patient.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fillahdev.hytenc_patient.R
import com.fillahdev.hytenc_patient.ui.home.ui.settings.SettingsPreference

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        supportFragmentManager.beginTransaction().add(R.id.frameSettings, SettingsPreference())
            .commit()
    }
}