package com.fillahdev.hytenc_patient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fillahdev.hytenc_patient.data.MedicineSchedule
import com.fillahdev.hytenc_patient.databinding.ItemListScheduleBinding

class ListMedicineScheduleAdapter(private val listMedicineSchedule: List<MedicineSchedule>) :
    RecyclerView.Adapter<ListMedicineScheduleAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemListScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvMedicineName.text = listMedicineSchedule[position].medicineName
        holder.binding.tvMedicineSchedule.text = listMedicineSchedule[position].schedule
    }

    override fun getItemCount(): Int = listMedicineSchedule.size
}