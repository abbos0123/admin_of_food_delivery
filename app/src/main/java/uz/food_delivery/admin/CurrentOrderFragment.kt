package uz.food_delivery.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import uz.food_delivery.admin.adapters.OrdersAdapter
import uz.food_delivery.admin.databinding.FragmentOrders2Binding
import uz.food_delivery.admin.databinding.FragmentOrdersBinding
import uz.food_delivery.fooddelivery.models.Order

class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrders2Binding
    private lateinit var order: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = arguments
        order = bundle?.getSerializable("order") as Order
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrders2Binding.inflate(layoutInflater)
        var adapter = OrdersAdapter(order)
        binding.rv.adapter = adapter

        setClicks()
        return binding.root
    }

    private fun setClicks() {
        binding.buttonAccept.setOnClickListener{
            var database= FirebaseDatabase.getInstance()
            var ref = database.getReference("orders").child(order.id!!)
            ref.removeValue()
                .addOnCompleteListener {
                    Toast.makeText(requireContext(), "Order is done!!", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
