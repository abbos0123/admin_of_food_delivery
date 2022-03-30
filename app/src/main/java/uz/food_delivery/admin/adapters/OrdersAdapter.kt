package uz.food_delivery.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.food_delivery.admin.databinding.ItemOrderBinding
import uz.food_delivery.fooddelivery.models.Order

class OrdersAdapter(var order: Order) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
    var list = order.list

    inner class OrderViewHolder(var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            var myObject = list?.get(position)
            binding.name.text =myObject?.name
            binding.price.text = myObject?.price.toString()
            binding.numberOfProducts.text = myObject?.number.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = list?.size ?: 0
}