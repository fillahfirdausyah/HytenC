package com.fillahdev.hytenc_patient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fillahdev.hytenc_patient.data.MedicalRecord
import com.fillahdev.hytenc_patient.databinding.ItemListMedicalRecordBinding

class ListMedicalRecordAdapter(private val listMedicalRecord: List<MedicalRecord>) :
    RecyclerView.Adapter<ListMedicalRecordAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemListMedicalRecordBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListMedicalRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvMedicalRecordNote.text = listMedicalRecord[position].note
        holder.binding.tvMedicalRecordDate.text = listMedicalRecord[position].date
    }

    override fun getItemCount(): Int = listMedicalRecord.size
}