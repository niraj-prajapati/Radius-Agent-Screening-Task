package com.nirajprajapati.radiusagent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nirajprajapati.radiusagent.R
import com.nirajprajapati.radiusagent.data.OptionRealm
import com.nirajprajapati.radiusagent.databinding.ListItemFacilitiesBinding
import com.nirajprajapati.radiusagent.util.Const

class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.ItemHolder>() {

    private var list: List<OptionRealm> = arrayListOf()

    class ItemHolder(view: ListItemFacilitiesBinding) : RecyclerView.ViewHolder(view.root) {
        val ivIcon = view.ivIcon
        val tvName = view.tvName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemFacilitiesBinding.inflate(layoutInflater, parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[holder.adapterPosition]

        holder.apply {
            val drawableResId = Const.iconMap[item.icon] ?: R.mipmap.ic_launcher_foreground
            ivIcon.setImageResource(drawableResId)
            tvName.text = item.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<OptionRealm>) {
        this.list = list.toList()
        notifyDataSetChanged()
    }
}