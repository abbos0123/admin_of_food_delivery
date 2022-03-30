package uz.food_delivery.admin.fragments

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.ProductType
import uz.food_delivery.admin.adapters.NationalFoodImageAdapter
import uz.food_delivery.admin.databinding.FragmentNationalFoodBinding
import uz.food_delivery.admin.view_models.InsertNationalFoodViewModel
import uz.food_delivery.admin.view_models.InsertViewModelFactry


class NationalFoodFragment : Fragment() {
    private lateinit var binding: FragmentNationalFoodBinding
    private lateinit var adapter: NationalFoodImageAdapter
    private lateinit var insertNationalFoodViewModel: InsertNationalFoodViewModel
    private val ACTIVITY_SELECT_IMAGE1 = 12345

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.changeTitle("Add dessert")
        var viewModelFactory = InsertViewModelFactry(requireContext(), ProductType.NationalFood)
        insertNationalFoodViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(InsertNationalFoodViewModel::class.java)
        insertNationalFoodViewModel.init()
    }

    private fun setDataInitially() {
        binding.editName.setText(InsertNationalFoodViewModel.name)
        binding.editDescription.setText(InsertNationalFoodViewModel.description)
        if (InsertNationalFoodViewModel.price != 0.0)
            binding.editPrice.setText(InsertNationalFoodViewModel.price.toInt().toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNationalFoodBinding.inflate(layoutInflater)
        setAdapter()
        setListeners()
        setObserves()
        setDataInitially()

        InsertNationalFoodViewModel.isNetworkAvailable.observe(viewLifecycleOwner, Observer<Boolean>() {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    private fun setObserves() {
        InsertNationalFoodViewModel.imageCount.observe(viewLifecycleOwner, { number ->
            InsertNationalFoodViewModel.isLoading.observe(viewLifecycleOwner, {
                if (number >= 2 && !it) {
                    binding.addCard.visibility = View.VISIBLE
                } else {
                    binding.addCard.visibility = View.INVISIBLE
                }
            })
        })

        InsertNationalFoodViewModel.imageUrls.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                binding.placeHolderImage.visibility = View.VISIBLE
            } else {
                binding.placeHolderImage.visibility = View.INVISIBLE
            }
        })

        InsertNationalFoodViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.INVISIBLE
            }
        })
    }

    private fun setListeners() {

        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                InsertNationalFoodViewModel.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.editDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                InsertNationalFoodViewModel.description = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.editPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    InsertNationalFoodViewModel.price = s.toString().toDouble()
                else
                    InsertNationalFoodViewModel.price = 0.0
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                InsertNationalFoodViewModel.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.addCard.setOnClickListener {
            var name= binding.editName.text.toString()
            var description= binding.editDescription.text.toString()
            var price= binding.editPrice.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()){
                insertNationalFoodViewModel.addNationalFood()
                releaseData()
            }else{
                Toast.makeText(requireContext(), "Add all fields", Toast.LENGTH_SHORT).show()
            }

        }


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
    }

    private fun releaseData() {
        InsertNationalFoodViewModel.imageUrls.value = ArrayList()
        binding.editName.setText("")
        binding.editDescription.setText("")
        binding.editPrice.setText("")
        InsertNationalFoodViewModel.description=""
        InsertNationalFoodViewModel.name=""
        InsertNationalFoodViewModel.price=0.0
        binding.addCard.visibility = View.INVISIBLE

    }


    private fun setAdapter() {
        InsertNationalFoodViewModel.imageUrls.observe(viewLifecycleOwner, {
            this.adapter = NationalFoodImageAdapter(
                InsertNationalFoodViewModel.imageUrls,
                object : NationalFoodImageAdapter.OnNationalFoodClick {
                    override fun onDeleteClick(url: String, position: Int) {
                        insertNationalFoodViewModel.deleteImage(position)
                        adapter.notifyItemRemoved(
                            position
                        )
                        adapter.notifyItemRangeChanged(
                            position,
                            InsertNationalFoodViewModel.imageUrls.value?.size!!
                        )

                    }
                })
            binding.rv.adapter = adapter
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_SELECT_IMAGE1) {
            var selectedImage = data?.data
            if (selectedImage !=null)
                insertNationalFoodViewModel.addImage(selectedImage)
            else
                Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
        }
    }

}