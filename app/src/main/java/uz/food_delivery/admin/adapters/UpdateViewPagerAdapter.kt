package uz.food_delivery.admin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.food_delivery.admin.fragments.Update_Item_Fragment

class UpdateViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = 5

    override fun getItem(position: Int): Fragment {
        var fragment = Update_Item_Fragment.newInstance(position)
        when (position) {
            0 -> {
                return fragment
            }
            1 -> {
                return fragment
            }
            2 -> {
                return fragment
            }
            3 -> {
                return fragment
            }
            else -> {
                return fragment
            }
        }
    }
}