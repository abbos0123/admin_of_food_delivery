package uz.food_delivery.admin.view_models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.food_delivery.admin.ProductType
import java.lang.IllegalArgumentException

class InsertViewModelFactry(var context: Context, var productType: ProductType): ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsertNationalFoodViewModel::class.java)){
            return InsertNationalFoodViewModel.getInstance(context, productType) as T
        } else if (modelClass.isAssignableFrom(InsertFastFoodViewModel::class.java)){
            return InsertFastFoodViewModel.getInstance(context, productType) as T
        }else if (modelClass.isAssignableFrom(InsertDessertViewModel::class.java)){
            return InsertDessertViewModel.getInstance(context, productType) as T
        }else if (modelClass.isAssignableFrom(InsertSnackViewModel::class.java)){
            return InsertSnackViewModel.getInstance(context, productType) as T
        }else if (modelClass.isAssignableFrom(InsertDrinkingViewModel::class.java)){
            return InsertDrinkingViewModel.getInstance(context, productType) as T
        }

        throw IllegalArgumentException("")
    }
}