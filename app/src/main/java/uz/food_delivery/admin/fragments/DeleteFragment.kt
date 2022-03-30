package uz.food_delivery.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.R
import uz.food_delivery.admin.adapters.DeleteViewPagerAdapter
import uz.food_delivery.admin.adapters.UpdateViewPagerAdapter
import uz.food_delivery.admin.databinding.FragmentDEleteBinding


class DeleteFragment : Fragment() {
    private lateinit var binding: FragmentDEleteBinding
    private lateinit var deleteViewPagerAdapter: DeleteViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        HomeActivity.changeTitle("Delete")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDEleteBinding.inflate(layoutInflater)

        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        deleteViewPagerAdapter = DeleteViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = deleteViewPagerAdapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.bottomBar.itemActiveIndex = position
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.bottomBar.onItemSelected = {
            binding.viewPager.currentItem = it
        }
    }

}