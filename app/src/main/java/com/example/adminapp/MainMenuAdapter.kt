package com.example.adminapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.databinding.StatitemBinding

class MainMenuAdapter(val listener: Listener): RecyclerView.Adapter<MainMenuAdapter.StatHolder>() {
    val statlist = ArrayList<Stat>()
    class StatHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = StatitemBinding.bind(item)
        fun bind(stat: Stat, listener: Listener) = with(binding){
            StatyaName.text = stat.title
            itemView.setOnClickListener{
                listener.onClick(stat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statitem, parent, false)
        return StatHolder(view)
    }

    override fun onBindViewHolder(holder: StatHolder, position: Int) {
        holder.bind(statlist[position], listener)
    }

    override fun getItemCount(): Int {
        return statlist.size
    }
    fun addstat(stat:Stat){
        statlist.add(stat)
        notifyDataSetChanged()
    }
    interface  Listener{
        fun onClick(stat: Stat)
    }
}