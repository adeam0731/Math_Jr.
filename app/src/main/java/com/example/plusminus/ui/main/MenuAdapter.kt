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
        Menu.Lv2.Math.Lv2Plus,
        Menu.Lv2.Math.Lv2Minus,
        Menu.Lv2.Math.Lv2PlusMinusMix,
        Menu.Lv2.Math.Lv2Multiplication,
        Menu.Lv2.Math.Lv2Multiplication2,
        Menu.Lv2.Math.Lv2Division,
        Menu.Lv2.Math.Lv2MultiplicationDivisionMix
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
            binding.subject.text = menu.subject
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

sealed class Menu(
    open val level: String,
    open val subject: String,
    open val label: String,
) {
    sealed class Lv2(
        override val label: String,
        override val subject: String,
    ) : Menu(level = "２年生", subject = subject, label = label) {
        sealed class Math(
            override val level: String,
            override val label: String,
        ) : Lv2(subject = "算数", label = label) {
            /** くり上がりの足し算 */
            object Lv2Plus : Math("２年生", "くり上がりの足し算")

            /** くり下がり引き算 */
            object Lv2Minus : Math("２年生", "くり下がり引き算")

            /** くり上がり,くり下がりMIX */
            object Lv2PlusMinusMix : Math("２年生", "くり上がり,くり下がりMIX")

            /** かけ算(九九) */
            object Lv2Multiplication : Math("２年生", "かけ算(九九)")

            /** かけ算 */
            object Lv2Multiplication2 : Math("２年生", "かけ算")

            /** わり算 */
            object Lv2Division : Math("２年生", "わり算")

            /** かけ算、わり算MIX */
            object Lv2MultiplicationDivisionMix : Math("２年生", "かけ算、わり算MIX")
        }

        sealed class NationalLanguage(
            override val level: String,
            override val label: String,
        ) : Lv2(subject = "国語", label = label) {

        }
    }
}
