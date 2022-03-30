package uz.food_delivery.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.food_delivery.admin.databinding.ItemSwipeBinding
import uz.food_delivery.fooddelivery.models.Order


class MySwipeAdapter(var context: Context, var list: List<Order>, var onSwiped: OnSwiped): RecyclerView.Adapter<MySwipeAdapter.SwipeViewHolder>() {

    inner class SwipeViewHolder(var binding: ItemSwipeBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int){
            binding.nameTv.text = list[position].list?.get(0)?.name
            binding.time.text = list[position].time

            binding.callCard.setOnClickListener{
                onSwiped.onCallCLick(list[position], position)
            }

            binding.sendMessageCard.setOnClickListener {
                onSwiped.onDeleteClick(list[position], position)
            }

            binding.cardRoot.setOnClickListener{
                onSwiped.onRootClick(list[position], position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
       return SwipeViewHolder(ItemSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
       holder.onBind(position)
    }

    override fun getItemCount(): Int = list.size

    interface OnSwiped{
        fun onCallCLick(order: Order, position: Int)
        fun onDeleteClick(order: Order, position: Int)
        fun onRootClick(order: Order, position: Int)
    }
}