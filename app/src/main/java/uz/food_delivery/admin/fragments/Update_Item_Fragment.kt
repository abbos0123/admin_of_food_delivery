package uz.food_delivery.admin.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.UpdateAdapter
import uz.food_delivery.admin.broadcasts.CheckNetworkReceiver
import uz.food_delivery.admin.databinding.FragmentUpdateItemBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.fooddelivery.models.Beverage
import uz.food_delivery.admin.models.Snack

private const val ARG_PARAM1 = "param1"

class Update_Item_Fragment : Fragment() {
    private var position: Int? = null
    private lateinit var binding: FragmentUpdateItemBinding
    private lateinit var list: ArrayList<Any>
    private lateinit var updateAdapter: UpdateAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var checkNetworkReceiver: CheckNetworkReceiver

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNetworkReceiver = CheckNetworkReceiver(requireContext())
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("products")
        arguments?.let {
            position = it.getInt(ARG_PARAM1)

            when (position) {
                0 -> {
                    HomeActivity.changeTitle("Update fast food")
                }
                1 -> {
                    HomeActivity.changeTitle("Update national food")
                }
                2 -> {
                    HomeActivity.changeTitle("Update dessert")
                }
                3 -> {
                    HomeActivity.changeTitle("Update snacks")
                }
                4 -> {
                    HomeActivity.changeTitle("Update beverages")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateItemBinding.inflate(layoutInflater)
        setObservers()
        binding.progress.visibility = View.VISIBLE
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(checkNetworkReceiver, intentFilter)
        checkOnLine()
        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkOnLine() {
        if (checkNetworkReceiver.isOnline(requireContext())) {
            binding.noDataHere.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.noDataHere.setImageResource(R.drawable.ic_no_signal)
            binding.noDataHere.visibility = View.VISIBLE
            binding.progress.visibility = View.INVISIBLE
        }
    }

    override fun onStop() {
        activity?.unregisterReceiver(checkNetworkReceiver)
        super.onStop()
    }

    private fun setObservers() {
        checkNetworkReceiver.isConnected.observe(viewLifecycleOwner, {
            if (!it) {
                binding.noDataHere.visibility = View.INVISIBLE
                binding.rv.visibility = View.VISIBLE
                binding.progress.visibility = View.VISIBLE
                readData()
            } else {
                binding.noDataHere.setImageResource(R.drawable.ic_no_signal)
                binding.noDataHere.visibility = View.VISIBLE
                binding.rv.visibility = View.INVISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        })
    }

    private fun setAdapter() {
        updateAdapter = UpdateAdapter(list, position!!, object : UpdateAdapter.OnUpdateItemClick {
            override fun onUpdateItemClick(myObject: Any, position: Int) {
                var bundle = Bundle()
                when(position){
                 0 ->{
                     bundle.putSerializable("object", myObject as FastFood)
                     bundle.putInt("position", position)
                 }
                 1 ->{
                     bundle.putSerializable("object", myObject as NationalFood)
                     bundle.putInt("position", position)
                 }
                 2 ->{
                     bundle.putSerializable("object", myObject as Dessert)
                     bundle.putInt("position", position)
                 }
                 3 ->{
                     bundle.putSerializable("object", myObject as Snack)
                     bundle.putInt("position", position)
                 }
                 4 ->{
                     bundle.putSerializable("object", myObject as Beverage)
                     bundle.putInt("position", position)
                 }
                }
                findNavController().navigate(R.id.action_nav_updateFragment_to_updatePaceFragment, bundle)
            }

        })
        binding.rv.adapter = updateAdapter
    }

    private fun readData() {
        when (position) {

            0 -> {
                list = getDataFromFirebase(0, "fastFoods")
            }
            1 -> {
                list = getDataFromFirebase(1, "nationalFoods")
            }
            2 -> {
                list = getDataFromFirebase(2, "desserts")
            }
            3 -> {
                list = getDataFromFirebase(3, "snacks")
            }
            4 -> {
                list = getDataFromFirebase(4, "beverages")
            }
        }
    }

    private fun getDataFromFirebase(position: Int, path: String): ArrayList<Any> {
        var list1: ArrayList<Any> = ArrayList()

        reference.child(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() { child ->
                    when (position) {
                        0 -> {
                            list1.add(child.getValue(FastFood::class.java) as Any)
                        }
                        1 -> {
                            list1.add(child.getValue(NationalFood::class.java) as Any)
                        }
                        2 -> {
                            list1.add(child.getValue(Dessert::class.java) as Any)
                        }
                        3 -> {
                            list1.add(child.getValue(Snack::class.java) as Any)
                        }
                        4 -> {
                            list1.add(child.getValue(Beverage::class.java) as Any)
                        }
                    }
                    binding.progress.visibility = View.INVISIBLE
                }
                if (list1.size == 0) {
                    binding.noDataHere.setImageResource(R.drawable.ic_not_available)
                    binding.noDataHere.visibility = View.VISIBLE
                    binding.progress.visibility = View.INVISIBLE
                } else {
                    binding.noDataHere.visibility = View.INVISIBLE
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Update_item_Fragment", "value is: " + error.message)
            }

        })
        return list1
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            Update_Item_Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}