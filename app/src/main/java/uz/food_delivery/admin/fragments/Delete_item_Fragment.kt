package uz.food_delivery.admin.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.DeleteAdapter
import uz.food_delivery.admin.broadcasts.CheckNetworkReceiver
import uz.food_delivery.admin.databinding.DialogFragmentBinding
import uz.food_delivery.admin.databinding.FragmentDeleteItemBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.fooddelivery.models.Beverage
import uz.food_delivery.admin.models.Snack
import uz.food_delivery.fooddelivery.models.MyObject
import uz.food_delivery.fooddelivery.models.User


private const val ARG_PARAM1 = "param1"

class Delete_item_Fragment : Fragment() {
    private var position: Int? = null
    private lateinit var binding: FragmentDeleteItemBinding
    private lateinit var list: ArrayList<Any>
    private lateinit var deleteAdapter: DeleteAdapter
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
                    HomeActivity.changeTitle("Delete fast food")
                }
                1 -> {
                    HomeActivity.changeTitle("Delete national food")
                }
                2 -> {
                    HomeActivity.changeTitle("Delete dessert")
                }
                3 -> {
                    HomeActivity.changeTitle("Delete snacks")
                }
                4 -> {
                    HomeActivity.changeTitle("Delete beverages")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteItemBinding.inflate(layoutInflater)
        binding.progress.visibility = View.VISIBLE
        setObservers()
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
        deleteAdapter = DeleteAdapter(list, position!!, object : DeleteAdapter.OnDeleteItemClick {
            @SuppressLint("SetTextI18n")
            override fun onDeleteItemClick(myObject: Any, position: Int, positionOwn: Int) {

                var dialog = Dialog(requireContext())
                var binding = DialogFragmentBinding.bind(
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_fragment, null, false)
                )
                dialog.setContentView(binding.root)

                dialog.window?.setBackgroundDrawable(ColorDrawable(0))
                dialog.window?.setWindowAnimations(R.style.AnimationForDialog)

                when (position) {
                    0 -> {
                        val my = myObject as FastFood
                        binding.text.text = "Do you want to delete ${my.name} ?"
                    }
                    1 -> {
                        val my = myObject as NationalFood
                        binding.text.text = "Do you want to delete ${my.name} ?"
                    }
                    2 -> {
                        val my = myObject as Dessert
                        binding.text.text = "Do you want to delete ${my.name} ?"
                    }
                    3 -> {
                        val my = myObject as Snack
                        binding.text.text = "Do you want to delete ${my.name} ?"
                    }
                    4 -> {
                        val my = myObject as Beverage
                        binding.text.text = "Do you want to delete ${my.name} ?"
                    }
                }

                dialog.show()

                binding.cancel.setOnClickListener {
                    dialog.cancel()
                }

                binding.buttonDelete.setOnClickListener {
                    when (position) {
                        0 -> {
                            list.removeAt(positionOwn)
                            deleteAdapter.notifyItemRemoved(positionOwn)
                            deleteAdapter.notifyItemRangeChanged(positionOwn, list.size)
                            val fastFood = myObject as FastFood
                            reference.child("fastFoods").child("${fastFood.key}").removeValue()
                            Toast.makeText(
                                requireContext(),
                                "${fastFood.name}  is deleted!",
                                Toast.LENGTH_SHORT
                            ).show()

                            deletedAllLiked(fastFood.key!!)
                        }

                        1 -> {
                            list.removeAt(positionOwn)
                            deleteAdapter.notifyItemRemoved(positionOwn)
                            deleteAdapter.notifyItemRangeChanged(positionOwn, list.size)
                            var nationalFood = myObject as NationalFood
                            reference.child("nationalFoods").child("${nationalFood.key}")
                                .removeValue()
                            Toast.makeText(
                                requireContext(),
                                "${nationalFood.name}  is deleted!",
                                Toast.LENGTH_SHORT
                            ).show()

                            deletedAllLiked(nationalFood.key!!)
                        }
                        2 -> {
                            list.removeAt(positionOwn)
                            deleteAdapter.notifyItemRemoved(positionOwn)
                            deleteAdapter.notifyItemRangeChanged(positionOwn, list.size)
                            var dessert = myObject as Dessert
                            reference.child("desserts").child("${dessert.key}").removeValue()
                            Toast.makeText(
                                requireContext(),
                                "${dessert.name}  is deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                            deletedAllLiked(dessert.key!!)
                        }
                        3 -> {
                            list.removeAt(positionOwn)
                            deleteAdapter.notifyItemRemoved(positionOwn)
                            deleteAdapter.notifyItemRangeChanged(positionOwn, list.size)
                            var snack = myObject as Snack
                            reference.child("snacks").child("${snack.key}").removeValue()
                            Toast.makeText(
                                requireContext(),
                                "${snack.name}  is deleted!",
                                Toast.LENGTH_SHORT
                            ).show()

                            deletedAllLiked(snack.key!!)
                        }
                        4 -> {
                            list.removeAt(positionOwn)
                            deleteAdapter.notifyItemRemoved(positionOwn)
                            deleteAdapter.notifyItemRangeChanged(positionOwn, list.size)
                            var beverage = myObject as Beverage
                            reference.child("beverages").child("${beverage.key}").removeValue()
                            Toast.makeText(
                                requireContext(),
                                "${beverage.name}  is deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                            deletedAllLiked(beverage.key!!)
                        }
                    }

                    dialog.cancel()
                }
            }


        })
        binding.rv.adapter = deleteAdapter
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
                list1.clear()

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
                Log.w("Delete_item_Fragment", "value is: " + error.message)
            }

        })
        return list1
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            Delete_item_Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    private fun deletedAllLiked(key: String) {
        var ref = FirebaseFirestore.getInstance().collection("users")
        ref.get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    for (document in task.result!!){
                        val user = document.toObject(User::class.java)
                        val ref1 = FirebaseFirestore.getInstance().collection("users/${user.id}/likedProducts")
                        ref1.get().addOnCompleteListener{ t ->
                            if (t.isSuccessful){
                               for (document1 in t.result!!){
                                   val product = document1.toObject(MyObject::class.java)
                                   if (product.key == key)
                                       ref1.document(product.keySecond!!).delete()
                               }
                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    "Something is wrong !!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Something is wrong !!!", Toast.LENGTH_SHORT)
                        .show()
                }

            }

    }
}