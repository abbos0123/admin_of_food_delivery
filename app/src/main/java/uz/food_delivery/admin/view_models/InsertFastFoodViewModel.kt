package uz.food_delivery.admin.view_models


import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import uz.food_delivery.admin.ProductType
import uz.food_delivery.admin.broadcasts.CheckNetworkReceiver
import uz.food_delivery.admin.models.FastFood

class InsertFastFoodViewModel : ViewModel {

    private var context: Context? = null
    private var productType: ProductType

    private constructor(context: Context, productType: ProductType) {
        this.context = context
        this.productType = productType
    }

    companion object {
        var isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData()
        private lateinit var database: FirebaseDatabase
        private lateinit var referance: DatabaseReference
        private lateinit var storageReference: StorageReference
        private lateinit var firebaseStorage: FirebaseStorage
        private lateinit var fastFood: FastFood
        var name = ""
        var description = ""
        var price: Double  = 0.0
        var imageCount: MutableLiveData<Int> = MutableLiveData()
        lateinit var isLoading: MutableLiveData<Boolean>
         var insertFastFoodViewModel: InsertFastFoodViewModel? = null
        private lateinit var checkNetworkReceiver: CheckNetworkReceiver
        var imageUrls: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getInstance(context: Context, productType: ProductType): InsertFastFoodViewModel {
            if (insertFastFoodViewModel == null) {
                insertFastFoodViewModel = InsertFastFoodViewModel(context, productType)
                imageUrls.value = ArrayList()
            }

            checkNetworkReceiver = CheckNetworkReceiver(context)
            return insertFastFoodViewModel!!
        }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun init() {
        database = FirebaseDatabase.getInstance()
        referance = database.getReference("products")
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference("images")
        isLoading = MutableLiveData()
        isLoading.value = false
        imageCount.value = imageUrls.value?.size

        when (productType) {
            ProductType.FastFood -> {
                fastFood = FastFood()
            }

        }
    }

    fun addFastFood(): Boolean {
        fastFood.name = name
        fastFood.price = price
        fastFood.description = description
        fastFood.images = imageUrls.value as ArrayList<String?>
        var key = referance.push().key
        fastFood.key = key

        referance.child("fastFoods/$key").setValue(fastFood)
        Toast.makeText(context, fastFood.name + " is added!", Toast.LENGTH_SHORT).show()
        return false
    }


    fun addImage(uri: Uri) {
        isLoading.value = true
        val metadata = StorageMetadata.Builder()
            .setContentType("image")
            .build()

        var time = System.currentTimeMillis()
        var uploadTask = storageReference.child("$time.jpg").putFile(uri, metadata)

        uploadTask.addOnSuccessListener {

            var downloadUrl = it.metadata?.reference?.downloadUrl
            downloadUrl?.addOnSuccessListener {
                var url = it.toString()

                var arrayList = imageUrls.value
                arrayList?.add(url)
                imageUrls.value = arrayList
                imageCount.value = imageCount.value!! + 1

                isLoading.value = false
            }

        }.addOnFailureListener {
            Log.d("Tag", "${it.message}")
        }
    }

    fun deleteImage(position: Int) {
        var list = imageUrls.value
        list?.removeAt(position)
        imageUrls.value = list
        imageCount.value = imageCount.value!! - 1
    }
}