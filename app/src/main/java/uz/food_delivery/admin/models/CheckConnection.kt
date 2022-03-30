package uz.food_delivery.admin.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService


class CheckConnection(private var context: Context) {

    fun checkConnection(): Boolean {
        var isConnected = false
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (networkInfo != null) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                isConnected = true
            } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true
            }
        }

        return isConnected
    }

}