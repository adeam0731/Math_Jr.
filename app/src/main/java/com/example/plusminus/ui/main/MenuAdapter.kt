package com.example.plusminus.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plusminus.databinding.HolderMenuBinding

class MenuAdapter(context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    var onClickMenuListener: OnClickMenuListener? = null

    private val menuList = mutableListOf(
        Menu(MenuNo.LV2_PLUS, "２年生", "くり上がりの足し算"),
        Menu(MenuNo.LV2_MINUS, "２年生", "くり下がり足し算"),
        Menu(MenuNo.LV2_PLUS_MINUS_MIX, "２年生", "くり上がり,くり下がりMIX"),
        Menu(MenuNo.LV2_MULTIPLICATION, "２年生", "かけ算(九九)"),
        Menu(MenuNo.LV2_DIVISION, "２年生", "わり算")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HolderMenuBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = menuList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(menuList[position], onClickMenuListener)
    }

    class ViewHolder(private val binding: HolderMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun set(menu: Menu, onClickMenuListener: OnClickMenuListener?) {
            binding.level.text = menu.level
            binding.label.text = menu.label
            binding.root.setOnClickListener {
                onClickMenuListener?.onClickMenu(menu)
            }
        }
    }
}

interface OnClickMenuListener {
    fun onClickMenu(menu: Menu)

}

data class Menu(
    val no: MenuNo,
    val level: String,
    val label: String
)

enum class MenuNo {
    LV2_PLUS,
    LV2_MINUS,
    LV2_PLUS_MINUS_MIX,
    LV2_MULTIPLICATION,
    LV2_DIVISION,
}

