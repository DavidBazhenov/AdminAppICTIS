package com.example.adminapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.databinding.StatyablockBinding

class StatsetAdapter(val listener: Listener): RecyclerView.Adapter<StatsetAdapter.StatHolder>() {
    val statlist = ArrayList<StatyaSet>()
    class StatHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = StatyablockBinding.bind(item)
        fun bind(statyaSet: StatyaSet, listener: Listener) = with(binding){
            textView5.text = statyaSet.title
            itemView.setOnClickListener{
                listener.onClick(statyaSet)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statyablock, parent, false)
        return StatHolder(view)
    }
    override fun onBindViewHolder(holder: StatHolder, position: Int) {
        holder.bind(statlist[position], listener)
    }

    override fun getItemCount(): Int {
        return statlist.size
    }
    fun addblock(statyaSet: StatyaSet){
        statlist.add(statyaSet)
        notifyDataSetChanged()
    }
    interface  Listener{
        fun onClick(statyaSet: StatyaSet)
    }
}