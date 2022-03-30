package uz.food_delivery.admin.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.food_delivery.admin.ProductType
import uz.food_delivery.admin.R
import uz.food_delivery.admin.databinding.ItemDeleteBinding
import uz.food_delivery.admin.databinding.ItemUpdateBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.fooddelivery.models.Beverage
import uz.food_delivery.admin.models.Snack

class MainSectionAdapter(
    var list: List<Any>,
    var pagePosition: Int,
    var onMainSectionItemClick: OnMainSectionItemClick
) :
    RecyclerView.Adapter<MainSectionAdapter.UpdateViewHolder>() {

    inner class UpdateViewHolder(var binding: ItemDeleteBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun onBind(nationalFood: NationalFood, position: Int) {
            if (nationalFood?.isMainSection!!){
                binding.root1.setBackgroundColor(Color.parseColor("#FFF4754D"))
                binding.icon.setImageResource(R.drawable.ic_check)
            }else{
                binding.root1.setBackgroundColor(Color.parseColor("#FF37B56C"))
                binding.icon.setImageResource(R.drawable.ic_main_section)
            }

            binding.text.text = "National Food"
            binding.foodNameText.text = nationalFood.name
            binding.root0.setOnClickListener {
                onMainSectionItemClick.onMainSectionItemClick(nationalFood, pagePosition, position)
            }


        }


        fun onBind(fastFood: FastFood, position: Int) {
            if (fastFood?.isMainSection!!){
                 binding.root1.setBackgroundColor(Color.parseColor("#FFF4754D"))
                binding.icon.setImageResource(R.drawable.ic_check)
            }else{
                binding.root1.setBackgroundColor(Color.parseColor("#FF37B56C"))
                binding.icon.setImageResource(R.drawable.ic_main_section)
            }
            binding.text.text = "Fast Food"
            binding.foodNameText.text = fastFood.name
            binding.root0.setOnClickListener {
                onMainSectionItemClick.onMainSectionItemClick(fastFood, pagePosition, position)
            }
        }


        fun onBind(dessert: Dessert, position: Int) {
            if (dessert?.isMainSection!!){
                 binding.root1.setBackgroundColor(Color.parseColor("#FFF4754D"))
                binding.icon.setImageResource(R.drawable.ic_check)
            }else{
                binding.root1.setBackgroundColor(Color.parseColor("#FF37B56C"))
                binding.icon.setImageResource(R.drawable.ic_main_section)
            }
            binding.text.text = "Dessert"
            binding.foodNameText.text = dessert.name
            binding.root0.setOnClickListener {
                onMainSectionItemClick.onMainSectionItemClick(dessert, pagePosition, position)
            }
        }


        fun onBind(snack: Snack, position: Int) {
            if (snack?.isMainSection!!){
                 binding.root1.setBackgroundColor(Color.parseColor("#FFF4754D"))
                binding.icon.setImageResource(R.drawable.ic_check)
            }else{
                binding.root1.setBackgroundColor(Color.parseColor("#FF37B56C"))
                binding.icon.setImageResource(R.drawable.ic_main_section)
            }
            binding.text.text = "Snack"
            binding.foodNameText.text = snack.name
            binding.root0.setOnClickListener {
                onMainSectionItemClick.onMainSectionItemClick(snack, pagePosition, position)
            }
        }


        fun onBind(beverage: Beverage, position: Int) {
            if (beverage?.isMainSection!!){
                 binding.root1.setBackgroundColor(Color.parseColor("#FFF4754D"))
                binding.icon.setImageResource(R.drawable.ic_check)
            }else{
                binding.root1.setBackgroundColor(Color.parseColor("#FF37B56C"))
                binding.icon.setImageResource(R.drawable.ic_main_section)
            }
            binding.text.text = "Beverage"
            binding.foodNameText.text = beverage.name
            binding.root1.setOnClickListener {
                onMainSectionItemClick.onMainSectionItemClick(beverage, pagePosition, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        return UpdateViewHolder(
            ItemDeleteBinding.inflate(
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

    interface OnMainSectionItemClick {
        fun onMainSectionItemClick(myObject: Any, position: Int, positionOwn: Int)
    }
}