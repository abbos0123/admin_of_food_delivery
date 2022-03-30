package uz.food_delivery.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import uz.food_delivery.admin.HomeActivity
import uz.food_delivery.admin.adapters.MainSectionViewPagerAdapter
import uz.food_delivery.admin.databinding.FragmentMainSectionHomeFragmentBinding

class MainSection_HomeFragmentFragment : Fragment() {
    private lateinit var binding: FragmentMainSectionHomeFragmentBinding
    private lateinit var mainSecionViewPagerAdapter: MainSectionViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        HomeActivity.changeTitle("Main Section")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainSectionHomeFragmentBinding.inflate(layoutInflater)

        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        mainSecionViewPagerAdapter = MainSectionViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = mainSecionViewPagerAdapter

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