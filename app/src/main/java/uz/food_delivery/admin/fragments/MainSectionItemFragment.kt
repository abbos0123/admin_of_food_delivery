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
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.DeleteAdapter
import uz.food_delivery.admin.adapters.MainSectionAdapter
import uz.food_delivery.admin.broadcasts.CheckNetworkReceiver
import uz.food_delivery.admin.databinding.DialogFragmentBinding
import uz.food_delivery.admin.databinding.FragmentMainSectionItemBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.admin.models.Snack
import uz.food_delivery.fooddelivery.models.Beverage


private const val ARG_PARAM1 = "param1"


class MainSectionItemFragment : Fragment() {
    private var position: Int? = null
    private lateinit var binding: FragmentMainSectionItemBinding
    private lateinit var list: ArrayList<Any>
    private lateinit var mainSectionAdapter: MainSectionAdapter
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
        binding = FragmentMainSectionItemBinding.inflate(layoutInflater)
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
                binding.progress.visibility = View.INVISIBLE
                binding.rv.visibility = View.INVISIBLE
            }
        })
    }

    private fun setAdapter() {
        mainSectionAdapter = MainSectionAdapter(
            list,
            position!!,
            object : MainSectionAdapter.OnMainSectionItemClick {
                override fun onMainSectionItemClick(
                    myObject: Any,
                    position: Int,
                    positionOwn: Int
                ) {
                    setDialog(myObject, position, positionOwn)
                }

            })

        binding.rv.adapter = mainSectionAdapter
    }

    private fun setDialog(myObject: Any, position: Int, positionOwn: Int) {
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

                if (my.isMainSection!!) {
                    binding.text.text = "Do you want to remove ${my.name}  from the main list?"
                    binding.textButton.text = "Remove"
                } else {
                    binding.text.text = "Do you want to add ${my.name}  to the main list?"
                    binding.textButton.text = "Add"
                }
            }
            1 -> {
                val my = myObject as NationalFood
                if (my.isMainSection!!) {
                    binding.text.text = "Do you want to remove ${my.name}  from the main list?"
                    binding.textButton.text = "Remove"
                } else {
                    binding.text.text = "Do you want to add ${my.name}  to the main list?"
                    binding.textButton.text = "Add"
                }
            }
            2 -> {
                val my = myObject as Dessert
                if (my.isMainSection!!) {
                    binding.text.text = "Do you want to remove ${my.name}  from the main list?"
                    binding.textButton.text = "Remove"
                } else {
                    binding.text.text = "Do you want to add ${my.name}  to the main list?"
                    binding.textButton.text = "Add"
                }
            }
            3 -> {
                val my = myObject as Snack
                if (my.isMainSection!!) {
                    binding.text.text = "Do you want to remove ${my.name}  from the main list?"
                    binding.textButton.text = "Remove"
                } else {
                    binding.text.text = "Do you want to add ${my.name}  to the main list?"
                    binding.textButton.text = "Add"
                }
            }
            4 -> {
                val my = myObject as Beverage
                if (my.isMainSection!!) {
                    binding.text.text = "Do you want to remove ${my.name}  from the main list?"
                    binding.textButton.text = "Remove"
                } else {
                    binding.text.text = "Do you want to add ${my.name}  to the main list?"
                    binding.textButton.text = "Add"
                }
            }
        }


        dialog.show()

        binding.cancel.setOnClickListener {
            dialog.cancel()
        }

        binding.buttonDelete.setOnClickListener {
            when (position) {
                0 -> {
                    var fastFood = myObject as FastFood
                    fastFood.isMainSection = !fastFood.isMainSection!!
                    list[position] = fastFood as Any
                    reference.child("fastFoods").child("${fastFood.key}").setValue(fastFood)
                    mainSectionAdapter.notifyItemChanged(position)
                    if (fastFood.isMainSection!!){
                        Toast.makeText(
                            requireContext(),
                            "${fastFood.name}  is selected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "${fastFood.name}  is unselected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                1 -> {

                    var nationalFood = myObject as NationalFood
                    nationalFood.isMainSection = !nationalFood.isMainSection!!
                    reference.child("nationalFoods").child("${nationalFood.key}")
                        .setValue(nationalFood)

                    if (nationalFood.isMainSection!!){
                        Toast.makeText(
                            requireContext(),
                            "${nationalFood.name}  is selected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "${nationalFood.name}  is unselected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                2 -> {

                    var dessert = myObject as Dessert
                    dessert.isMainSection = !dessert.isMainSection!!
                    reference.child("desserts").child("${dessert.key}").setValue(dessert)
                    if (dessert.isMainSection!!){
                        Toast.makeText(
                            requireContext(),
                            "${dessert.name}  is selected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "${dessert.name}  is unselected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                3 -> {
                    var snack = myObject as Snack
                    snack.isMainSection = !snack.isMainSection!!
                    reference.child("snacks").child("${snack.key}").setValue(snack)
                    if (snack.isMainSection!!){
                        Toast.makeText(
                            requireContext(),
                            "${snack.name}  is selected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "${snack.name}  is unselected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                4 -> {
                    var beverage = myObject as Beverage
                    beverage.isMainSection = !beverage.isMainSection!!
                    reference.child("beverages").child("${beverage.key}").setValue(beverage)
                    if (beverage.isMainSection!!){
                        Toast.makeText(
                            requireContext(),
                            "${beverage.name}  is selected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "${beverage.name}  is unselected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            dialog.cancel()
        }
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
            MainSectionItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}