package com.fillahdev.hytenc_patient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fillahdev.hytenc_patient.data.Tips
import com.fillahdev.hytenc_patient.databinding.ItemListTipsBinding

class ListTipsAdapter(private val listTips: List<Tips>) :
    RecyclerView.Adapter<ListTipsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemListTipsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(listTips[position].thumbnailURL)
            .into(holder.binding.ivTipsThumbnail)
        holder.binding.tvTipsTitle.text = listTips[position].title
    }

    override fun getItemCount(): Int = listTips.size
}