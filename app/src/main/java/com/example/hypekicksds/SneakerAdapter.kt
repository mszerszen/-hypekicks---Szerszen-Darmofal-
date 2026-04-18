package com.example.hypekicksds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.hypekicksds.databinding.SneakerViewBinding

class SneakerAdapter(
    private val context: Context,
    private val sneakersList: List<Sneaker>
) : BaseAdapter() {
    override fun getCount(): Int = sneakersList.size

    override fun getItem(position: Int): Any = sneakersList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: SneakerViewBinding
        val view: View

        if(convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = SneakerViewBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as SneakerViewBinding
        }

        val sneaker = sneakersList[position]

        binding.sneakerName.text = sneaker.name
        binding.sneakerPrice.text = "${sneaker.price} PLN + ${sneaker.imageUrl}"

        Glide.with(context)
            .load(sneaker.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.sneakerImage)

        return view
    }
}