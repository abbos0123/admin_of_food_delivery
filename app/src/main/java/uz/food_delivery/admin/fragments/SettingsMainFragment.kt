package uz.food_delivery.admin.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import uz.food_delivery.admin.R
import uz.food_delivery.admin.databinding.FragmentSettingsMainBinding

class SettingsMainFragment : Fragment() {
    private lateinit var binding: FragmentSettingsMainBinding
    private var ischecked = false
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("night", 0)!!
        var sharedPreferences = activity?.getSharedPreferences("night", 0)!!
        var booleanValue = sharedPreferences.getBoolean("night_mode", false)

        ischecked = booleanValue
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsMainBinding.inflate(layoutInflater)
        if (ischecked) {
            binding.themeText.text = getText(R.string.darc_mode)
            binding.themeSwitch.isChecked = true
        }

        connectSwitch()
        setClicks()

        return binding.root
    }

    private fun setClicks() {
        binding.lan3.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_adminSettingFragment)
        }
    }


    private fun connectSwitch() {
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked1 ->
            ischecked = isChecked1
            setTheme()
        }

        binding.lan2.setOnClickListener {
            ischecked = !ischecked
            binding.themeSwitch.isChecked = ischecked
        }
    }

    private fun setTheme() {
        var editor = sharedPreferences.edit()

        if (ischecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(requireContext(), "Dark made", Toast.LENGTH_SHORT).show()
            editor.putBoolean("night_mode", true)
            editor.commit()
            binding.themeText.text = getString(R.string.darc_mode)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(requireContext(), "Light made", Toast.LENGTH_SHORT).show()
            editor.putBoolean("night_mode", false)
            editor.commit()
            binding.themeText.text = getString(R.string.light_mode)
        }
    }


}