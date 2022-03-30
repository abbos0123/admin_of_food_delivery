package uz.food_delivery.admin.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import uz.food_delivery.admin.R
import uz.food_delivery.admin.databinding.AddUserDialogBinding
import uz.food_delivery.admin.databinding.FragmentAdminSettingBinding
import uz.food_delivery.admin.models.Admin


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminSettingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAdminSettingBinding
    private lateinit var sharedPre: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAdminSettingBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        sharedPre = activity?.getSharedPreferences("current_admin", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setClicks()
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    private fun setClicks() {
        binding.editFullName.setText(sharedPre.getString("full_name", ""))
        binding.editLogin.setText(sharedPre.getString("login", ""))
        binding.editPassword.setText(sharedPre.getString("password", ""))
        binding.fab.setOnClickListener {
            var dialog = Dialog(requireContext())
            var binding = AddUserDialogBinding.bind(
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.add_user_dialog, null, false)
            )
            dialog.setContentView(binding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.window?.setWindowAnimations(R.style.AnimationForDialog)
            dialog.show()

            binding.cancel.setOnClickListener {
                dialog.cancel()
            }

            binding.buttonAdd.setOnClickListener {
                var login = binding.editLogin.text.toString()
                var fullName = binding.editFullName.text.toString()
                var password = binding.editPassword.text.toString()
                var isAvailable = false

                try {
                    if (login.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                        var collection = FirebaseFirestore.getInstance().collection("admins")
                        var id = collection.document().id
                        var key = id

                        collection
                            .get()
                            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val admin = document.toObject(Admin::class.java)
                                        if (admin?.login == login) {
                                            isAvailable = true
                                            break
                                        }
                                    }
                                    if (!isAvailable) {
                                        var admin = Admin(fullName, login, password, key)

                                        collection.add(admin)
                                            .addOnSuccessListener {
                                                val id = it.id
                                                admin.id = id
                                                collection.document(id).set(admin)
                                                    .addOnCompleteListener {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "User Added",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        dialog.cancel()
                                                    }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Something is wrong!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                dialog.cancel()
                                            }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Invalid Login",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Something wrong!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter any field !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Something wrong !", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }

            }
        }

        binding.buttonChange.setOnClickListener {
            val newLogin = binding.editLogin.text.toString()
            val newFullName = binding.editFullName.text.toString()
            val newPassword = binding.editPassword.text.toString()
            val collection = FirebaseFirestore.getInstance().collection("admins")
            try {
                if (newFullName.isNotEmpty() && newLogin.isNotEmpty() && newPassword.isNotEmpty()) {
                    var isAvailable = false

                    collection.get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    var admin = document.toObject(Admin::class.java)
                                    if (admin.login == newLogin) {
                                        isAvailable = true
                                        break
                                    }
                                }
                                if (!isAvailable) {
                                    val editor = sharedPre.edit()
                                    editor.apply {
                                        this?.putString("login", newLogin)
                                        this?.putString("password", newPassword)
                                        this?.putString("full_name", newFullName)
                                        this?.apply()
                                    }
                                    val id = sharedPre.getString("id", "")
                                    val admin = Admin(newFullName, newLogin, newPassword, id!!)

                                    collection.document(id).set(admin)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Data is Changed!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "this login hav already been taken !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(), "Something is wrong", Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                }
              //  findNavController().popBackStack()
                Toast.makeText(requireContext(), "Changed", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

}