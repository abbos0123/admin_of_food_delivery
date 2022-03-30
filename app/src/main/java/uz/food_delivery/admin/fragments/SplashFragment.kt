package uz.food_delivery.admin.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import uz.food_delivery.admin.R
import uz.food_delivery.admin.databinding.FragmentLogInBinding
import uz.food_delivery.admin.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
                var sharedPreferences = activity?.getSharedPreferences("night", 0)!!
        var booleanValue = sharedPreferences.getBoolean("night_mode", false)

        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(requireContext(), "Dark made", Toast.LENGTH_SHORT).show()
        }

        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_logInFragment)
        }, 2000)
        return binding.root
    }


}