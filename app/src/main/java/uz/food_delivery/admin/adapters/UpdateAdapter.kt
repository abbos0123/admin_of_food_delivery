package uz.food_delivery.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.food_delivery.admin.databinding.ItemUpdateBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.fooddelivery.models.Beverage
import uz.food_delivery.admin.models.Snack

class UpdateAdapter(var list: List<Any>, var pagePosition: Int, var onUpdateItemClick: OnUpdateItemClick) :
    RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder>() {

    inner class UpdateViewHolder(var binding: ItemUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(nationalFood: NationalFood, position: Int) {
            binding.text.text = "National Food"
            binding.foodNameText.text = nationalFood.name
            binding.root0.setOnClickListener {
                onUpdateItemClick.onUpdateItemClick(nationalFood, pagePosition)
            }
        }

        fun onBind(fastFood: FastFood, position: Int) {
            binding.text.text = "Fast Food"
            binding.foodNameText.text = fastFood.name
            binding.root0.setOnClickListener {
                onUpdateItemClick.onUpdateItemClick(fastFood, pagePosition)
            }
        }

        fun onBind(dessert: Dessert, position: Int) {
            binding.text.text = "Dessert"
            binding.foodNameText.text = dessert.name
            binding.root0.setOnClickListener {
                onUpdateItemClick.onUpdateItemClick(dessert, pagePosition)
            }
        }

        fun onBind(snack: Snack, position: Int) {
            binding.text.text = "Snack"
            binding.foodNameText.text = snack.name
            binding.root0.setOnClickListener {
                onUpdateItemClick.onUpdateItemClick(snack, pagePosition)
            }
        }

        fun onBind(beverage: Beverage, position: Int) {
            binding.text.text = "Beverage"
            binding.foodNameText.text = beverage.name
            binding.root0.setOnClickListener {
                onUpdateItemClick.onUpdateItemClick(beverage, pagePosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        return UpdateViewHolder(
            ItemUpdateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {

        when (pagePosition) {

            0 -> {
                val item = list[position] as FastFood
                holder.onBind(item, position)
            }
            1 -> {
                val item = list[position] as NationalFood
                holder.onBind(item, position)
            }
            2 -> {
                val item = list[position] as Dessert
                holder.onBind(item, position)
            }
            3 -> {
                val item = list[position] as Snack
                holder.onBind(item, position)
            }
            4 -> {
                val item = list[position] as Beverage
                holder.onBind(item, position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnUpdateItemClick {
        fun onUpdateItemClick(myObject: Any, position: Int)
    }
}