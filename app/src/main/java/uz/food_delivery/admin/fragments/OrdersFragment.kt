package uz.food_delivery.admin.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.MySwipeAdapter
import uz.food_delivery.admin.databinding.FragmentOrdersBinding
import uz.food_delivery.fooddelivery.models.Order

class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var swipeAdapter: MySwipeAdapter
    private lateinit var list: ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        HomeActivity.changeTitle("Orders")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        readData()
    }
    private fun setAdapter() {
        swipeAdapter = MySwipeAdapter(requireContext(), list, object : MySwipeAdapter.OnSwiped {
            override fun onCallCLick(order: Order, position: Int) {
                order.user?.phone?.let { number ->
                    callToService(number)
                }
            }

            override fun onRootClick(order: Order, position: Int) {
                var bundle = Bundle()
                bundle.putSerializable("order", order)
                findNavController().navigate(R.id.action_nav_ordersFragment_to_ordersFragment, bundle)
            }

            override fun onDeleteClick(order: Order, position: Int) {
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val ref = firebaseDatabase.getReference("orders")
                val query = ref.orderByChild("id").equalTo(order.id)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (child in snapshot.children) {
                            child.ref.removeValue()
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

                swipeAdapter.notifyItemRemoved(position)
                swipeAdapter.notifyItemRangeChanged(position, list.size)


                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rv.adapter = swipeAdapter
    }

    private fun readData() {
        list = ArrayList()

        var database = FirebaseDatabase.getInstance()
        var ref = database.getReference("orders")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              list.clear()
                
                for (child in snapshot.children){
                    var order = child.getValue(Order::class.java)
                    list.add(order!!)
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun callToService(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")

        if (Build.VERSION.SDK_INT > 23) {
            startActivity(intent)
        } else {
            if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(requireContext(), "Permission Not Granted ", Toast.LENGTH_SHORT)
                        .show()
            } else {
                val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.CALL_PHONE)
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_STORAGE, 9)
                startActivity(intent)
            }
        }
    }

}