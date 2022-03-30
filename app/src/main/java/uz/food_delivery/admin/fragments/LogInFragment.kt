package uz.food_delivery.admin.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.databinding.FragmentLogInBinding
import uz.food_delivery.admin.models.Admin


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var currentUser: Admin
    private lateinit var sharedPre: SharedPreferences



    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        sharedPre = activity?.getSharedPreferences("current_admin", Context.MODE_PRIVATE)!!
        val login = sharedPre.getString("login", "")
        val password = sharedPre.getString("password", "")
        binding.editUsername.setText(login)
        binding.editPassword.setText(password)


        binding.logIn.setOnClickListener {
            val login = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            checkAdmin(login, password)
        }

        return binding.root
    }

    private fun checkAdmin(login: String, password: String) {
       var isAvailable = false
        var collection = FirebaseFirestore.getInstance().collection("admins")
        collection.get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        val admin = document.toObject(Admin::class.java)
                        if (admin?.login == login && admin.password == password) {
                            currentUser = admin
                            isAvailable = true
                            accessToHome(login, password)
                        }
                    }
                    if (!isAvailable){

                    Toast.makeText(
                        requireContext(),
                        "Incorrect username or password!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                }else{
                    Toast.makeText(requireContext(), "Something is wrong!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun accessToHome(login: String, password: String) {
        val editor = sharedPre.edit()
        editor?.putString("login", login)
        editor?.putString("full_name", currentUser.fullName)
        editor?.putString("password", password)
        editor?.putString("id", currentUser.id)
        editor?.apply()

        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}