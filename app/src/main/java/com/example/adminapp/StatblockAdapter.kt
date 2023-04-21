package com.example.adminapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.databinding.StateblockBinding
import com.squareup.picasso.Picasso

class StatblockAdapter(val listener: Listener): RecyclerView.Adapter<StatblockAdapter.StatHolder>() {
    val statlist = ArrayList<SatBlockData>()
    class StatHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = StateblockBinding.bind(item)
        fun bind(satBlockData: SatBlockData, listener: Listener) = with(binding){
            textView5.text = satBlockData.title
            Picasso.get().load(satBlockData.imgurl).into(imageView3)
            itemView.setOnClickListener{
                listener.onClick(satBlockData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stateblock, parent, false)
        return StatHolder(view)
    }

    override fun onBindViewHolder(holder: StatHolder, position: Int) {
        holder.bind(statlist[position], listener)
    }

    override fun getItemCount(): Int {
        return statlist.size
    }
    fun addblock(statBlockData: SatBlockData){

        println("Block added")
        statlist.add(statBlockData)
        notifyDataSetChanged()
    }
    interface  Listener{
        fun onClick(statBlockData: SatBlockData)
    }
}