package uz.food_delivery.admin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class HomeViewPager(
    fragmentManager: FragmentManager,
    var fr1: Fragment,
    var fr2: Fragment,
    var fr3: Fragment,
    var fr4: Fragment,
    var fr5: Fragment
) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = 5

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return fr1
            }
            1 -> {
                return fr2
            }
            2 -> {
                return fr3
            }
            3 -> {
                return fr4
            }
            else -> {
                return fr5
            }

        }
    }
}