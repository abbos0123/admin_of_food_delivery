package uz.food_delivery.admin.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.NationalFoodImageAdapter
import uz.food_delivery.admin.databinding.FragmentUpdatePaceBinding
import uz.food_delivery.admin.models.Dessert
import uz.food_delivery.admin.models.FastFood
import uz.food_delivery.admin.models.NationalFood
import uz.food_delivery.admin.models.Snack
import uz.food_delivery.admin.view_models.InsertNationalFoodViewModel
import uz.food_delivery.fooddelivery.models.Beverage
import java.text.FieldPosition


class UpdatePaceFragment : Fragment() {
    private lateinit var binding: FragmentUpdatePaceBinding
    private lateinit var adapter: NationalFoodImageAdapter
    private lateinit var myObject: Any
    private lateinit var isLoading: MutableLiveData<Boolean>
    private lateinit var imageList: MutableLiveData<ArrayList<String>>
    private var position: Int = 0
    private lateinit var imageCount: MutableLiveData<Int>
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private val ACTIVITY_SELECT_IMAGE1 = 123457
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageList = MutableLiveData()
            myObject = it.getSerializable("object") as Any
            position = it.getInt("position")

            isLoading = MutableLiveData()
            isLoading.value = false

            firebaseStorage = FirebaseStorage.getInstance()
            storageReference = firebaseStorage.getReference("images")
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("products")
            imageCount = MutableLiveData()
            imageCount.value = 0

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdatePaceBinding.inflate(layoutInflater)

        setParams()

        return binding.root
    }

    private fun setParams() {
        when (position) {
            0 -> {
                var currentObject = myObject as FastFood
                imageList.value = currentObject.images as ArrayList<String>
                imageCount.value = imageList.value?.size

                binding.editName.setText(currentObject.name)
                binding.editDescription.setText(currentObject.description)

                if (currentObject.price != 0.0)
                    binding.editPrice.setText(currentObject.price?.toInt().toString())

            }
            1 -> {
                var currentObject = myObject as NationalFood
                imageList.value = currentObject.images as ArrayList<String>
                imageCount.value = imageList.value?.size

                binding.editName.setText(currentObject.name)
                binding.editDescription.setText(currentObject.description)

                if (currentObject.price != 0.0)
                    binding.editPrice.setText(currentObject.price?.toInt().toString())

            }
            2 -> {
                var currentObject = myObject as Dessert
                imageList.value = currentObject.images as ArrayList<String>
                imageCount.value = imageList.value?.size

                binding.editName.setText(currentObject.name)
                binding.editDescription.setText(currentObject.description)

                if (currentObject.price != 0.0)
                    binding.editPrice.setText(currentObject.price?.toInt().toString())
            }
            3 -> {
                var currentObject = myObject as Snack
                imageList.value = currentObject.images as ArrayList<String>
                imageCount.value = imageList.value?.size

                binding.editName.setText(currentObject.name)
                binding.editDescription.setText(currentObject.description)

                if (currentObject.price != 0.0)
                    binding.editPrice.setText(currentObject.price?.toInt().toString())
            }
            4 -> {
                var currentObject = myObject as Beverage
                imageList.value = currentObject.images as ArrayList<String>
                imageCount.value = imageList.value?.size

                binding.editName.setText(currentObject.name)
                binding.editDescription.setText(currentObject.description)
                if (currentObject.price != 0.0)
                    binding.editPrice.setText(currentObject.price?.toInt().toString())
            }
        }

        setAdapter()
        setObserves()
    }

    private fun setObserves() {
        imageList.observe(viewLifecycleOwner, { list ->
            if (list.size == 0) {
                binding.placeHolderImage.visibility = View.VISIBLE
            } else {
                binding.placeHolderImage.visibility = View.INVISIBLE
            }
        })

        isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.INVISIBLE
            }
        })

        imageCount.observe(viewLifecycleOwner, { number ->
            isLoading.observe(viewLifecycleOwner, {
                if (number >= 2 && !it) {
                    binding.addCard.visibility = View.VISIBLE
                } else {
                    binding.addCard.visibility = View.INVISIBLE
                }
            })

        })


        binding.profileImage.setOnClickListener {
            Dexter.withContext(requireContext()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted()!!) {
                        var intent = Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        )
                        startActivityForResult(intent, ACTIVITY_SELECT_IMAGE1)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
        }

        binding.addCard.setOnClickListener {

            var name = binding.editName.text.toString()
            var price = binding.editPrice.text.toString()
            var des = binding.editDescription.text.toString()

            if (name.isNotEmpty() && price.isNotEmpty() && des.isNotEmpty())
                update()
            else{
                Toast.makeText(requireContext(), "add all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter() {
        imageList.observe(viewLifecycleOwner, {
            adapter = NationalFoodImageAdapter(
                imageList,
                object : NationalFoodImageAdapter.OnNationalFoodClick {
                    override fun onDeleteClick(url: String, position: Int) {
                        deleteImage(position)
                    }
                })

            binding.rv.adapter = adapter
        })
    }

    fun addImage(uri: Uri) {
        isLoading.value = true
        val metadata = StorageMetadata.Builder()
            .setContentType("image")
            .build()

        var time = System.currentTimeMillis()
        var uploadTask = storageReference.child("$time.jpg").putFile(uri, metadata)

        uploadTask.addOnSuccessListener {

            var downloadUrl = it.metadata?.reference?.downloadUrl
            downloadUrl?.addOnSuccessListener {
                var url = it.toString()

                var arrayList = imageList.value
                arrayList?.add(url)
                imageList.value = arrayList
                imageCount.value = imageCount.value!! + 1

                isLoading.value = false
            }

        }.addOnFailureListener {
            Log.d("Tag", "${it.message}")
        }
    }

    fun deleteImage(position: Int) {
        var list = imageList.value
        list?.removeAt(position)
        imageList.value = list
        imageCount.value = imageCount.value!! - 1
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, imageList.value?.size!!)
    }

    private fun update() {
        when (position) {
            0 -> {
                val fastFood = myObject as FastFood
                fastFood.price = binding.editPrice.text.toString().toDouble()
                fastFood.name = binding.editName.text.toString()
                fastFood.description = binding.editDescription.text.toString()
                fastFood.images = imageList.value as ArrayList<String?>
                reference.child("fastFoods").child(fastFood?.key!!).setValue(fastFood)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            1 -> {
                val nationalFood = myObject as NationalFood
                nationalFood.price = binding.editPrice.text.toString().toDouble()
                nationalFood.name = binding.editName.text.toString()
                nationalFood.description = binding.editDescription.text.toString()
                nationalFood.images = imageList.value as ArrayList<String?>
                reference.child("nationalFoods").child(nationalFood?.key!!).setValue(nationalFood)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            2 -> {
                val dessert = myObject as Dessert
                dessert.price = binding.editPrice.text.toString().toDouble()
                dessert.name = binding.editName.text.toString()
                dessert.description = binding.editDescription.text.toString()
                dessert.images = imageList.value as ArrayList<String?>
                reference.child("desserts").child(dessert?.key!!).setValue(dessert)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            3 -> {
                val snack = myObject as Snack
                snack.price = binding.editPrice.text.toString().toDouble()
                snack.name = binding.editName.text.toString()
                snack.description = binding.editDescription.text.toString()
                snack.images = imageList.value as ArrayList<String?>
                reference.child("snacks").child(snack?.key!!).setValue(snack)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            4 -> {
                var beverage = myObject as Beverage
                beverage.price = binding.editPrice.text.toString().toDouble()
                beverage.name = binding.editName.text.toString()
                beverage.description = binding.editDescription.text.toString()
                beverage.images = imageList.value as ArrayList<String?>
                reference.child("beverages").child(beverage?.key!!).setValue(beverage)
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_SELECT_IMAGE1) {
            var selectedImage = data?.data
            if (selectedImage != null)
                addImage(selectedImage)
            else
                Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
        }
    }

}