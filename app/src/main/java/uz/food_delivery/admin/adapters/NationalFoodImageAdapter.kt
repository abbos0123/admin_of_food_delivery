package uz.food_delivery.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import uz.food_delivery.admin.R
import uz.food_delivery.admin.databinding.ItemNationalImageBinding
import java.lang.Exception

class NationalFoodImageAdapter(
    var data: MutableLiveData<ArrayList<String>>,
    var onNationalFoodClick: OnNationalFoodClick
) : RecyclerView.Adapter<NationalFoodImageAdapter.NationalImageHolder>() {

    inner class NationalImageHolder(var binding: ItemNationalImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(url: String, position: Int) {

            Picasso.get().load(url)
                .placeholder(R.drawable.place_holder)
                .into(binding.image, object : Callback {
                    override fun onSuccess() {
                        binding.progressCircular.visibility = View.INVISIBLE
                    }

                    override fun onError(e: Exception?) {

                    }

                })
            binding.delete.setOnClickListener {
                onNationalFoodClick.onDeleteClick(url, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NationalImageHolder {
        return NationalImageHolder(
            ItemNationalImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NationalImageHolder, position: Int) {
        holder.onBind(data.value?.get(position)!!, position)
    }

    override fun getItemCount(): Int = data.value?.size!!

    interface OnNationalFoodClick {
        fun onDeleteClick(url: String, position: Int)
    }
}