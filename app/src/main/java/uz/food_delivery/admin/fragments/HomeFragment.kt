package uz.food_delivery.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import uz.food_delivery.admin.adapters.HomeViewPager
import uz.food_delivery.admin.databinding.FragmentHome2Binding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHome2Binding
    private lateinit var homeViewPager: HomeViewPager
    private lateinit var fastFood: FastFoodFragment
    private lateinit var nationalFoodFragment: NationalFoodFragment
    private lateinit var dessertFragment: DessertFragment
    private lateinit var snackFragment: SnackFragment
    private var isOpenKeyboard = false
    private lateinit var drinkingFragment: DrinkingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        fastFood = FastFoodFragment()
        nationalFoodFragment = NationalFoodFragment()
        dessertFragment = DessertFragment()
        snackFragment = SnackFragment()
        drinkingFragment = DrinkingFragment()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHome2Binding.inflate(layoutInflater)

        homeViewPager = HomeViewPager(
            childFragmentManager,
            fastFood, nationalFoodFragment, dessertFragment, snackFragment, drinkingFragment
        )
        binding.viewPager.adapter = homeViewPager
        connectViewPagerWithBar()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setEventListener(
            requireActivity(),
            KeyboardVisibilityEventListener {
                if (it) {
                   binding.bottomBar.visibility = View.INVISIBLE
                } else {
                    binding.bottomBar.visibility = View.VISIBLE
                }
                isOpenKeyboard = it
            })
    }

    private fun connectViewPagerWithBar() {

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomBar.itemActiveIndex = position

                if (isOpenKeyboard)
                binding.bottomBar.visibility = View.INVISIBLE
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.bottomBar.onItemSelected = {
            binding.viewPager.currentItem = it
        }
    }

}